package com.lambdasys.iss_tracker.controller.dto;

import com.lambdasys.iss_tracker.data.PositionFix;
import org.springframework.hateoas.EntityModel;

/**
 * # 📦 Representação HATEOAS - Posição Fixa da ISS
 *
 * Esta classe representa uma **posição fixa da ISS** encapsulada em um modelo HATEOAS,
 * seguindo o padrão **EntityModel** do Spring HATEOAS.
 *
 * ## 🎯 Propósito
 * - Encapsular dados da posição fixa com suporte a links HATEOAS
 * - Facilitar navegação entre recursos relacionados
 * - Fornecer representação padronizada para streaming de eventos
 *
 * ## 🔗 Estrutura da Resposta
 *
 * ```json
 * {
 *   "latitude": -45.5,
 *   "longitude": 120.3,
 *   "pixelX": 512,
 *   "pixelY": 384,
 *   "timestamp": 1711747200,
 *   "updateAt": "2026-03-29T22:00:00Z",
 *   "_links": {
 *     "self": {
 *       "href": "http://localhost:8080/api/iss/stream",
 *       "title": "Stream de posições da ISS"
 *     },
 *     "current": {
 *       "href": "http://localhost:8080/api/iss/now",
 *       "title": "Posição atual da ISS"
 *     }
 *   }
 * }
 * ```
 *
 * ## 🏗️ Como Funciona
 *
 * 1. **Encapsula** `PositionFix` em `EntityModel` para suporte HATEOAS
 * 2. **Permite** adição de links via `add(Link)` ou `add(WebMvcLinkBuilder)`
 * 3. **Serializa** automaticamente em JSON HAL
 * 4. **Compatível** com streaming reativo via `Flux<ServerSentEvent<PositionFixRepresentation>>`
 *
 * ## 📋 Exemplo de Uso
 * ```java
 * PositionFix fix = positionCacheService.getLatestPosition();
 * PositionFixRepresentation representation = new PositionFixRepresentation(fix);
 *
 * // Adicionar links
 * representation.add(linkTo(methodOn(IssController.class).streamIssPositions()).withSelfRel());
 * ```
 *
 * ## ⚡ Uso em Streaming
 *
 * ```java
 * Flux<ServerSentEvent<PositionFixRepresentation>> stream = 
 *     positionFlux.map(fix -> new PositionFixRepresentation(fix))
 *                 .map(rep -> ServerSentEvent.builder()
 *                     .id(UUID.randomUUID().toString())
 *                     .event("position-update")
 *                     .data(rep)
 *                     .build());
 * ```
 *
 * @see EntityModel Classe base do Spring HATEOAS
 * @see PositionFix Dados de posição fixa da ISS
 * @see PositionFixRepresentationAssembler Como montar a representação completa
 *
 * @author Sistema de Rastreamento ISS
 * @version 1.0.0
 * @since Java 21
 */
public class PositionFixRepresentation extends EntityModel<PositionFix> {

    /**
     * ## 🏗️ Construtor da Representação
     *
     * Cria uma nova instância de representação HATEOAS para a posição fixa.
     *
     * @param positionFix dados da posição fixa (não pode ser null)
     *
     * @throws IllegalArgumentException se `positionFix` for null
     *
     * @see PositionFix Estrutura dos dados encapsulados
     */
    public PositionFixRepresentation(PositionFix positionFix) {
        super(positionFix);
    }
}

