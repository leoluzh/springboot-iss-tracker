package com.lambdasys.iss_tracker.controller;

import com.lambdasys.iss_tracker.data.IssCrew;
import com.lambdasys.iss_tracker.data.PositionFix;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "ISS Tracker", description = "API para rastreamento da Estação Espacial Internacional (ISS)")
public interface IssTrackerControllerDocs {

    @Operation(
            summary = "Obter posição atual da ISS",
            description = "Retorna a posição geográfica atual (latitude e longitude) da Estação Espacial Internacional com links HATEOAS"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Posição da ISS obtida com sucesso",
                    content = @Content(
                            mediaType = "application/hal+json",
                            schema = @Schema(implementation = PositionFix.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro ao obter dados da API externa"
            )
    })
    @GetMapping("/position")
    Mono<ResponseEntity<PositionFix>> getCurrentIssPosition();

    @Operation(
            summary = "Stream de posições da ISS",
            description = "Retorna um stream Server-Sent Events com atualizações em tempo real da posição da ISS"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Stream de posições iniciado",
                    content = @Content(mediaType = "text/event-stream")
            )
    })
    @GetMapping(value = "/stream", produces = "text/event-stream")
    Flux<ServerSentEvent<PositionFix>> streamIssPositions();

    @GetMapping("/crew")

    Mono<ResponseEntity<IssCrew>> getCurrentIssCrew();

    @GetMapping(value = "/crew/stream", produces = "text/event-stream")
    Flux<ServerSentEvent<IssCrew>> streamIssCrew();


}
