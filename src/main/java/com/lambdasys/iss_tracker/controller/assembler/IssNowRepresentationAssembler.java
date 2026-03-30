package com.lambdasys.iss_tracker.controller.assembler;

import com.lambdasys.iss_tracker.client.data.IssNowResponse;
import com.lambdasys.iss_tracker.controller.IssTrackerController;
import com.lambdasys.iss_tracker.controller.dto.IssNowRepresentation;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

/**
 * # 🔧 Assembler HATEOAS - Representação da Posição ISS
 *
 * Esta classe é responsável por **montar a representação HATEOAS** das respostas
 * da API de rastreamento da Estação Espacial Internacional (ISS).
 *
 * ## 🎯 Responsabilidades
 * - ✅ Converter `IssNowResponse` em `IssNowRepresentation`
 * - ✅ Adicionar links HATEOAS para navegação
 * - ✅ Seguir o padrão **Assembler** do Spring HATEOAS
 * - ✅ Centralizar lógica de construção de links
 *
 * ## 🔗 Links Adicionados
 * - **`self`** - Link para o próprio endpoint da posição atual
 * - **`external`** - Link para a API original Open Notify
 *
 * ## 📋 Exemplo de Uso
 * ```java
 * // Injeção via Spring
 * private final IssNowRepresentationAssembler assembler;
 *
 * IssNowResponse response = issService.getCurrentIssPosition();
 * IssNowRepresentation representation = assembler.toModel(response);
 * ```
 *
 * ## 🏗️ Padrão de Design
 * Implementa o padrão **Assembler** para:
 * - Separar responsabilidades entre controller e lógica de links
 * - Facilitar testes unitários
 * - Permitir reutilização em outros controllers
 *
 * @see IssNowRepresentation Modelo de representação HATEOAS
 * @see IssTrackerController Controller que utiliza este assembler
 * @see IssNowResponse Dados brutos da API ISS
 *
 * @author leoluzh
 * @version 1.0.0
 * @since 29/03/2026
 */
@Component
public class IssNowRepresentationAssembler {

    /**
     * ## 🔄 Monta Representação HATEOAS
     *
     * Converte uma resposta bruta da API ISS em uma representação HATEOAS
     * completa com links para navegação.
     *
     * ### Processo:
     * 1. Cria instância de `IssNowRepresentation`
     * 2. Adiciona link **self** para o endpoint atual
     * 3. Adiciona link **external** para API original
     * 4. Retorna representação pronta
     *
     * ### Links Gerados:
     * - `self` → `GET /api/iss/now`
     * - `external` → `https://api.open-notify.org/iss-now.json`
     *
     * @param issNowResponse resposta bruta da API ISS (não pode ser null)
     * @return representação HATEOAS completa com links de navegação
     *
     * @throws IllegalArgumentException se `issNowResponse` for null
     *
     * @see IssNowRepresentation Estrutura da representação retornada
     * @see WebMvcLinkBuilder Como os links são construídos
     */
    public IssNowRepresentation toModel(IssNowResponse issNowResponse) {
        var representation = new IssNowRepresentation(issNowResponse);

        // Adicionar link self
        representation.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(IssTrackerController.class).getCurrentIssPosition()
            ).withSelfRel().withTitle("Posição atual da ISS")
        );

        // Adicionar link externo para a API original
        representation.add(
            Link.of("https://api.open-notify.org/iss-now.json", "external")
                .withTitle("API original Open Notify")
        );

        return representation;
    }
}
