package com.lambdasys.iss_tracker.controller.assembler;

import com.lambdasys.iss_tracker.controller.IssTrackerController;
import com.lambdasys.iss_tracker.controller.dto.PositionFixRepresentation;
import com.lambdasys.iss_tracker.data.PositionFix;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * # 🔧 Assembler HATEOAS - Posição Fixa da ISS
 *
 * Responsável por **montar representações HATEOAS completas** para objetos `PositionFix`.
 *
 * ## 🎯 Funcionalidades
 *
 * ### ✅ Adição de Links
 * - **self**: Link para o stream de posições
 * - **current**: Link para a posição atual
 * - Links adicionais conforme necessário
 *
 * ### ✅ Transformação de Dados
 * - Converte `PositionFix` em `PositionFixRepresentation`
 * - Mantém dados originais intactos
 * - Adiciona contexto de navegação HATEOAS
 *
 * ## 🏗️ Padrão de Design
 *
 * Implementa o padrão **RepresentationModelAssembler** do Spring HATEOAS:
 * ```java
 * // Transforma entidade em representação com links
 * PositionFixRepresentation rep = assembler.toModel(positionFix);
 * ```
 *
 * ## 📋 Exemplo de Uso
 *
 * ```java
 * @Autowired
 * private PositionFixRepresentationAssembler assembler;
 *
 * public void procesarPosicion(PositionFix fix) {
 *     PositionFixRepresentation rep = assembler.toModel(fix);
 *     // Enviar via SSE com links HATEOAS
 *     return rep;
 * }
 * ```
 *
 * ## 📊 Links Gerados
 *
 * ```json
 * {
 *   "latitude": -45.5,
 *   "longitude": 120.3,
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
 * @see RepresentationModelAssembler Interface do Spring HATEOAS
 * @see PositionFixRepresentation Representação HATEOAS
 * @see PositionFix Entidade de domínio
 * @see IssTrackerController Controller associado
 *
 * @author Sistema de Rastreamento ISS
 * @version 1.0.0
 * @since Java 21
 */
@Component
public class PositionFixRepresentationAssembler implements RepresentationModelAssembler<PositionFix, PositionFixRepresentation> {

    /**
     * ## 🔄 Converte PositionFix em PositionFixRepresentation
     *
     * Transforma uma entidade `PositionFix` em sua representação HATEOAS completa,
     * adicionando links de navegação.
     *
     * ### Fluxo:
     * 1. Cria nova `PositionFixRepresentation` encapsulando o `PositionFix`
     * 2. Adiciona link "self" para o stream de posições
     * 3. Adiciona link "current" para a posição atual
     * 4. Retorna representação completa
     *
     * @param entity `PositionFix` a ser transformado (não pode ser null)
     * @return `PositionFixRepresentation` com links HATEOAS
     *
     * @throws IllegalArgumentException se `entity` for null
     *
     * @see PositionFix Entidade original
     * @see PositionFixRepresentation Representação com links
     */
    @Override
    public PositionFixRepresentation toModel(PositionFix entity) {
        PositionFixRepresentation representation = new PositionFixRepresentation(entity);

        // Link para o stream de posições
        representation.add(
                linkTo(methodOn(IssTrackerController.class).streamIssPositions())
                        .withSelfRel()
                        .withTitle("Stream de posições da ISS")
        );

        // Link para a posição atual
        representation.add(
                linkTo(methodOn(IssTrackerController.class).getCurrentIssPosition())
                        .withRel("current")
                        .withTitle("Posição atual da ISS")
        );

        return representation;
    }
}

