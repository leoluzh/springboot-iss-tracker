package com.lambdasys.iss_tracker.service;


import com.lambdasys.iss_tracker.client.data.IssAstrosResponse;
import com.lambdasys.iss_tracker.client.mapper.CrewMapper;
import com.lambdasys.iss_tracker.data.IssCrew;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class IssCrewCacheService {

    private final AtomicReference<IssCrew> latest = new AtomicReference<>(null);
    private final CrewMapper crewMapper;
    // Configura o Sink para multicast sem fechar caso ocorra backpressure
    // ou se algum cliente for desconectado. Sinks.many().multicast().directBestEffort()
    // costuma ser mais robusto para SSE
    private final Sinks.Many<IssCrew> processor = Sinks.many().multicast().directBestEffort();

    public IssCrew update(IssAstrosResponse response) {
        var crew = crewMapper.toIssCrew(response);
        latest.set(crew);
        return crew;
    }

    public IssCrew getLatestCrew() {
        return latest.get();
    }

    public boolean hasData() {
        return latest.get() != null;
    }

    public Flux<IssCrew> stream() {
        return processor.asFlux();
    }

    public void broadcast(IssCrew crew) {
        // Usa tryEmitNext sem falhar se não houver subscribers
        processor.tryEmitNext(crew);
    }

    public Flux<ServerSentEvent<IssCrew>> streamSSE() {
        return stream()
                .map(crew -> ServerSentEvent.<IssCrew>builder()
                        .id(String.valueOf(System.currentTimeMillis()))
                        .event("crew-update")
                        .data(crew)
                        .build());
    }

}
