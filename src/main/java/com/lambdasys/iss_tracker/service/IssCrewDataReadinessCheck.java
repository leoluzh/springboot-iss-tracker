package com.lambdasys.iss_tracker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * # 🔍 Readiness Check - ISS Tracker
 *
 * Implementação de {@link HealthIndicator} para verificar se a aplicação
 * está **pronta para receber tráfego** (readiness probe).
 *
 * ## ✅ Critérios de Prontidão
 * - Cache de posição ISS populado: Dados já foram obtidos da API
 * - Serviço de polling ativo: Sistema pode atualizar posições continuamente
 *
 * ## 🔗 Endpoints
 * - **Readiness**: `GET /actuator/health/readiness`
 * - **Liveness**: `GET /actuator/health/liveness`
 * - **All Health**: `GET /actuator/health`
 *
 * ## 🐳 Uso em Kubernetes
 * ```yaml
 * readinessProbe:
 *   httpGet:
 *     path: /actuator/health/readiness
 *     port: 8080
 *   initialDelaySeconds: 20
 *   periodSeconds: 10
 * ```
 *
 * ## 📊 Resposta de Sucesso
 * ```json
 * {
 *   "status": "UP",
 *   "components": {
 *     "issDataReadiness": {
 *       "status": "UP",
 *       "details": {
 *         "message": "ISS data available and polling active"
 *       }
 *     }
 *   }
 * }
 * ```
 *
 * ## 📊 Resposta de Falha
 * ```json
 * {
 *   "status": "OUT_OF_SERVICE",
 *   "components": {
 *     "issDataReadiness": {
 *       "status": "OUT_OF_SERVICE",
 *       "details": {
 *         "message": "Waiting for ISS data to be cached..."
 *       }
 *     }
 *   }
 * }
 * ```
 *
 * @see HealthIndicator Interface para indicadores de saúde
 * @see IssPositionCacheService Serviço de cache monitorado
 * @see IssPositionPollerService Serviço de polling monitorado
 *
 * @author leoluzh
 * @version 1.0.0
 * @since 29/03/2026
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class IssCrewDataReadinessCheck implements HealthIndicator {

    private final IssCrewCacheService cacheService;

    @Override
    public Health health() {
        log.debug("Verificando readiness da aplicação ISS Tracker");

        // Verifica se há dados no cache
        if (cacheService.hasData()) {
            log.info("✅ Aplicação pronta: Cache populado com dados ISS");
            return Health.up()
                    .withDetail("message", "ISS data available and polling active")
                    .withDetail("latest_position", formatPosition())
                    .build();
        }

        log.warn("⚠️ Aplicação em estado de espera: Aguardando dados ISS");
        return Health.outOfService()
                .withDetail("message", "Waiting for ISS data to be cached...")
                .withDetail("reason", "First polling cycle has not completed yet")
                .build();
    }

    /**
     * Formata a posição atual para exibição nos detalhes de saúde
     */
    private String formatPosition() {
        var position = cacheService.getLatestCrew();
        if (position != null) {
            return String.format("Number: %s",
                    position.getNumber());
        }
        return "No data available";
    }
}
