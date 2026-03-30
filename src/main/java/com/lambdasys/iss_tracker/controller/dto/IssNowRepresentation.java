package com.lambdasys.iss_tracker.controller.dto;

import com.lambdasys.iss_tracker.client.data.IssNowResponse;
import org.springframework.hateoas.EntityModel;

/**
 * # 📦 Representação HATEOAS - Posição da ISS
 *
 * Esta classe representa a **resposta da API ISS** encapsulada em um modelo HATEOAS,
 * seguindo o padrão **EntityModel** do Spring HATEOAS.
 *
 * ## 🎯 Quando Usar EntityModel vs RepresentationModel
 *
 * ### ✅ EntityModel (Esta classe)
 * - **Ideal para**: Uma entidade principal com links HATEOAS
 * - **Serialização**: Automática via Spring HATEOAS
 * - **Padrão**: HAL (Hypertext Application Language)
 * - **Uso**: APIs REST que seguem convenções HATEOAS
 *
 * ### ❌ RepresentationModel (Genérico)
 * - **Ideal para**: Estruturas complexas customizadas
 * - **Serialização**: Manual (você controla tudo)
 * - **Flexibilidade**: Máxima, mas mais código
 *
 * ## 🔗 Estrutura da Resposta
 *
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
 *       "href": "http://localhost:8080/api/iss/now",
 *       "title": "Posição atual da ISS"
 *     },
 *     "external": {
 *       "href": "https://api.open-notify.org/iss-now.json",
 *       "title": "API original Open Notify"
 *     }
 *   }
 * }
 * ```
 *
 * ## 🏗️ Como Funciona
 *
 * 1. **Recebe** `IssNowResponse` (dados brutos da API)
 * 2. **Encapsula** em `EntityModel` para suporte HATEOAS
 * 3. **Permite** adição de links via `add(Link)` ou `add(WebMvcLinkBuilder)`
 * 4. **Serializa** automaticamente em JSON HAL
 *
 * ## 📋 Exemplo de Uso
 * ```java
 * IssNowResponse response = issService.getCurrentIssPosition();
 * IssNowRepresentation representation = new IssNowRepresentation(response);
 *
 * // Adicionar links
 * representation.add(linkTo(methodOn(IssController.class).getPosition()).withSelfRel());
 * ```
 *
 * @see EntityModel Classe base do Spring HATEOAS
 * @see IssNowResponse Dados brutos da API ISS
 * @see IssNowRepresentationAssembler Como montar a representação completa
 *
 * @author Sistema de Rastreamento ISS
 * @version 1.0.0
 * @since Java 21
 */
public class IssNowRepresentation extends EntityModel<IssNowResponse> {

    /**
     * ## 🏗️ Construtor da Representação
     *
     * Cria uma nova instância de representação HATEOAS para a resposta ISS.
     *
     * @param issNowResponse resposta bruta da API ISS (não pode ser null)
     *
     * @throws IllegalArgumentException se `issNowResponse` for null
     *
     * @see IssNowResponse Estrutura dos dados encapsulados
     */
    public IssNowRepresentation(IssNowResponse issNowResponse) {
        super(issNowResponse);
    }
}
