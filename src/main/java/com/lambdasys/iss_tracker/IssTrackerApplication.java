package com.lambdasys.iss_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * # 🚀 Aplicação Spring Boot - ISS Tracker
 *
 * Aplicação principal para rastreamento da Estação Espacial Internacional (ISS).
 * Fornece APIs REST para obter a posição atual da ISS e atualizações em tempo real.
 *
 * ## ⚙️ Configurações Habilitadas
 * - ✅ **Feign Clients**: Para chamadas HTTP declarativas
 * - ✅ **Scheduling**: Para polling automático da posição ISS
 * - ✅ **Actuator**: Para monitoramento e métricas
 * - ✅ **HATEOAS**: Para navegação RESTful
 *
 * ## 🔄 Funcionalidades
 * - **API REST**: Endpoints para posição atual da ISS
 * - **Polling Automático**: Atualização a cada 10 segundos
 * - **Cache**: Armazenamento temporário de posições
 * - **Broadcasting**: Notificações em tempo real
 * - **Documentação**: OpenAPI/Swagger integrada
 *
 * ## 🏃‍♂️ Execução
 * ```bash
 * mvn spring-boot:run
 * ```
 *
 * @see IssTrackerController Controller REST principal
 * @see IssPositionPollerService Serviço de polling automático
 * @see IssPositionCacheService Serviço de cache de posições
 *
 * @author leoluzh
 * @version 1.0.0
 * @since 29/03/2026
 */
@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class IssTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(IssTrackerApplication.class, args);
	}

}
