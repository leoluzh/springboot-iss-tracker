package com.lambdasys.iss_tracker.service;

import com.lambdasys.iss_tracker.client.data.IssNowResponse;
import com.lambdasys.iss_tracker.client.mapper.PositionMapper;
import com.lambdasys.iss_tracker.data.PositionFix;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class IssPositionCacheService {

    private final AtomicReference<PositionFix> latest = new AtomicReference<>(null);
    private final MercatorProjectionService mercatorProjectionService;
    private final PositionMapper positionMapper;
    
    // Configura o Sink para multicast sem fechar caso ocorra backpressure 
    // ou se algum cliente for desconectado. Sinks.many().multicast().directBestEffort()
    // costuma ser mais robusto para SSE
    private final Sinks.Many<PositionFix> processor = Sinks.many().multicast().directBestEffort();

    public PositionFix update(IssNowResponse response) {
        var position = positionMapper.toPositionDouble(response.getPosition());
        int[] pixels = mercatorProjectionService.toPixel(position.getLatitude(),position.getLongitude());

        var fix = PositionFix.builder()
                .latitude(position.getLatitude())
                .longitude(position.getLongitude())
                .pixelX(pixels[0])
                .pixelY(pixels[1])
                .timestamp(response.getTimestamp())
                .updateAt(Instant.now())
                .build();

        latest.set(fix);
        return fix;
    }

    public PositionFix getLatestPosition() {
        return latest.get();
    }

    public boolean hasData() {
        return latest.get() != null;
    }

    public Flux<PositionFix> stream() {
        return processor.asFlux();
    }

    public void broadcast(PositionFix fix) {
        // Usa tryEmitNext sem falhar se não houver subscribers
        processor.tryEmitNext(fix);
    }

    public Flux<ServerSentEvent<PositionFix>> streamSSE() {
        return stream()
                .map(position -> ServerSentEvent.<PositionFix>builder()
                        .id(String.valueOf(System.currentTimeMillis()))
                        .event("position-update")
                        .data(position)
                        .build());
    }
}
