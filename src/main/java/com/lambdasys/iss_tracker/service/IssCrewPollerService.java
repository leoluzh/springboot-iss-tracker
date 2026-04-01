package com.lambdasys.iss_tracker.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * # 🔄 Serviço de Polling - Tripulação da ISS
 *
 * Serviço responsável por **atualizar automaticamente** a tripulação da Estação Espacial
 * Internacional através de polling periódico da API externa.
 *
 * ## ⏰ Configuração de Agendamento
 * - **Intervalo**: A cada 30 minutos (fixedRate = 1800000ms)
 * - **Execução Sequencial**: Uma execução por vez (Spring gerencia)
 * - **Framework**: Spring Scheduler
 *
 * ## 🔄 Fluxo de Funcionamento
 * 1. **Executa** a cada 30 minutos automaticamente
 * 2. **Busca** posição atual da ISS via `IssTrackerService`
 * 3. **Valida** resposta da API externa
 * 4. **Atualiza** cache com nova posição
 * 5. **Registra** logs de debug/erro
 *
 * ## 🛡️ Tratamento de Erros
 * - **Resposta inválida**: Log de warning e continua
 * - **Exceção**: Log de erro e continua execução
 * - **Execução concorrente**: Pula automaticamente
 *
 * ## 📊 Monitoramento
 * ```bash
 * # Verificar status via Actuator
 * curl http://localhost:8080/actuator/health
 *
 * # Verificar métricas
 * curl http://localhost:8080/actuator/metrics
 * ```
 *
 * @see IssTrackerService Serviço para obter posição da ISS
 * @see Scheduled Anotação de agendamento do Spring
 *
 * @author leoluzh
 * @version 1.0.0
 * @since 29/03/2026
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IssCrewPollerService {

    private final IssTrackerService issTrackerService;
    private final IssCrewCacheService issCrewCacheService;

    private static final String SUCCESS = "success";


    /**
     * ## 📡 Método de Polling
     *
     * Executado automaticamente a cada 30 minutos para atualizar a posição da ISS.
     * Esta é a implementação principal do polling periódico.
     *
     * ### Configuração:
     * - `@Scheduled(fixedRate = 1800000)`: Executa a cada 30 minutos (1800000ms)
     * - **Execução Sequencial**: Uma por vez (Spring gerencia concorrência)
     *
     * ### Tratamento:
     * - ✅ **Sucesso**: Atualiza cache e transmite posição
     * - ⚠️ **Resposta inválida**: Log de warning
     * - ❌ **Exceção**: Log de erro
     *
     * @implNote Método void - não retorna valor, apenas executa side effects
     */
    @Scheduled(fixedRate = 1800000)
    void poll() {
        try {
            var response = issTrackerService.getCurrentIssCrew();

            if (!SUCCESS.equalsIgnoreCase(response.getMessage())) {
                log.warn("Unexpected upstream response message: {}", response.getMessage());
                return;
            }

            var crew = issCrewCacheService.update(response);

            log.debug("ISS crew updated - number={} crew={}",
                    crew.getNumber(),
                    crew.getMembers());

        } catch (Exception ex) {
            log.error("Failed to fetch ISS position", ex);
        }
    }

}
