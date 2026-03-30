/**
 * # Controller REST - Rastreamento da Estação Espacial Internacional (ISS)
 *
 * Este controller fornece endpoints HTTP para obter a posição atual da ISS
 * a partir da [API Open Notify](https://api.open-notify.org/).
 *
 * ## Funcionalidades
 * - ✅ Obtenção da posição atual da ISS (latitude/longitude)
 * - ✅ Respostas com links HATEOAS para descoberta de recursos
 * - ✅ Documentação OpenAPI/Swagger integrada
 * - ✅ Tratamento de erros da API externa
 *
 * ## Endpoints Disponíveis
 * - `GET /api/iss/now` - Posição atual da ISS
 *
 * ## Tecnologias Utilizadas
 * - **Spring Boot** - Framework web
 * - **Spring HATEOAS** - Links de navegação
 * - **OpenAPI/Swagger** - Documentação da API
 * - **Feign Client** - Cliente HTTP declarativo
 *
 * ## Exemplo de Resposta
 * ```json
 * {
 *   "message": "success",
 *   "timestamp": 1711747200,
 *   "iss_position": {
 *     "latitude": "-45.5",
 *     "longitude": "120.3"
 *   },
 *   "_links": {
 *     "self": {
 *       "href": "http://localhost:8080/api/iss/now"
 *     },
 *     "external": {
 *       "href": "https://api.open-notify.org/iss-now.json"
 *     }
 *   }
 * }
 * ```
 *
 * @see IssTrackerControllerDocs Documentação OpenAPI/Swagger dos endpoints
 * @see IssNowRepresentationAssembler Assembler responsável por montar a representação HATEOAS
 * @see IssTrackerService Serviço de negócio para recuperar dados da ISS
 *
 * @author leoluzh
 * @version 1.0.0
 * @since 29/03/2026
 */
package com.lambdasys.iss_tracker.controller;

import com.lambdasys.iss_tracker.data.PositionFix;
import com.lambdasys.iss_tracker.service.IssPositionCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/iss")
@RequiredArgsConstructor
public class IssTrackerController implements IssTrackerControllerDocs {

    /** Serviço para acessar as atualizações de posição reativa da ISS */
    private final IssPositionCacheService positionCacheService;

    /**
     * ## 📍 Obtém a Posição Atual da ISS
     *
     * Este método recupera a posição geográfica atual da Estação Espacial Internacional
     * através do serviço de negócio e monta uma representação HATEOAS com links
     * para facilitar a navegação do cliente.
     *
     * ### Fluxo de Execução:
     * 1. Chama o serviço para obter dados da API externa
     * 2. Usa o assembler para criar representação HATEOAS
     * 3. Retorna resposta HTTP 200 com dados e links
     *
     * ### Possíveis Erros:
     * - `HttpClientErrorException` - Erro na API externa (4xx)
     * - `HttpServerErrorException` - Problema na API externa (5xx)
     *
     * @return {@link ResponseEntity} contendo a representação HATEOAS da posição atual da ISS
     *         com status HTTP 200 (OK)
     *
     * @throws org.springframework.web.client.HttpClientErrorException se a API externa retornar erro de cliente
     * @throws org.springframework.web.client.HttpServerErrorException se a API externa tiver problemas de servidor
     *
     * @see PositionFix Estrutura da resposta
     */
    @Override
    public Mono<ResponseEntity<PositionFix>> getCurrentIssPosition() {
        return Mono.fromCallable(() -> ResponseEntity.ok(positionCacheService.getLatestPosition()));
    }

    /**
     * ## 📡 Stream de Posições da ISS
     *
     * Este método retorna um Flux reativo que emite atualizações em tempo real
     * da posição da Estação Espacial Internacional via Server-Sent Events.
     *
     * ### Como Funciona:
     * - Retorna um Flux<PositionFix> que é automaticamente convertido em SSE pelo WebFlux
     * - Cada nova posição é emitida para todos os clientes conectados
     * - Usa Sinks.Many para multicast das atualizações
     *
     * ### Uso no Cliente:
     * ```javascript
     * const eventSource = new EventSource('/api/iss/stream');
     * eventSource.onmessage = (event) => {
     *   const position = JSON.parse(event.data);
     *   console.log('Nova posição:', position);
     * };
     * ```
     *
     * @return {@link Flux} de {@link PositionFix} para streaming reativo
     */
    @Override
    public Flux<ServerSentEvent<PositionFix>> streamIssPositions() {
        return positionCacheService.streamSSE();
    }
}
