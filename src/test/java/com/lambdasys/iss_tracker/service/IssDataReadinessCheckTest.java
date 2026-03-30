package com.lambdasys.iss_tracker.service;

import com.lambdasys.iss_tracker.data.PositionFix;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes para o IssDataReadinessCheck
 *
 * Valida que o health check retorna os estados corretos dependendo
 * da disponibilidade de dados no cache.
 */
@SpringBootTest
@DisplayName("IssDataReadinessCheck - Testes de Readiness")
class IssDataReadinessCheckTest {

    private IssDataReadinessCheck readinessCheck;

    @MockitoBean
    private IssPositionCacheService cacheService;

    @BeforeEach
    void setUp() {
        readinessCheck = new IssDataReadinessCheck(cacheService);
    }

    @Test
    @DisplayName("Deve retornar UP quando cache tem dados")
    void testHealthUpWhenCacheHasData() {
        // Arrange - Preparar dados mock
        PositionFix positionFix = PositionFix.builder()
                .latitude(51.6435)
                .longitude(36.5678)
                .pixelX(100)
                .pixelY(200)
                .timestamp(1712000000L)
                .updateAt(Instant.now())
                .build();

        when(cacheService.hasData()).thenReturn(true);
        when(cacheService.getLatestPosition()).thenReturn(positionFix);

        // Act - Executar verificação de saúde
        Health health = readinessCheck.health();

        // Assert - Validar resultado
        assertEquals(Status.UP.getCode(), health.getStatus().getCode());
        assertEquals("ISS data available and polling active",
                health.getDetails().get("message"));
        assertTrue(health.getDetails().get("latest_position").toString()
                .contains("51.6435"));

        verify(cacheService, times(2)).getLatestPosition();
    }

    @Test
    @DisplayName("Deve retornar OUT_OF_SERVICE quando cache está vazio")
    void testHealthOutOfServiceWhenCacheEmpty() {
        // Arrange
        when(cacheService.hasData()).thenReturn(false);
        when(cacheService.getLatestPosition()).thenReturn(null);

        // Act
        Health health = readinessCheck.health();

        // Assert
        assertEquals("OUT_OF_SERVICE", health.getStatus().getCode());
        assertEquals("Waiting for ISS data to be cached...",
                health.getDetails().get("message"));
        assertEquals("First polling cycle has not completed yet",
                health.getDetails().get("reason"));
    }

    @Test
    @DisplayName("Deve formatar posição com 4 casas decimais")
    void testPositionFormattingPrecision() {
        // Arrange
        PositionFix positionFix = PositionFix.builder()
                .latitude(51.643567890)
                .longitude(36.567890123)
                .pixelX(100)
                .pixelY(200)
                .timestamp(1712000000L)
                .updateAt(Instant.parse("2026-03-29T21:00:00Z"))
                .build();

        when(cacheService.hasData()).thenReturn(true);
        when(cacheService.getLatestPosition()).thenReturn(positionFix);

        // Act
        Health health = readinessCheck.health();

        // Assert
        String position = (String) health.getDetails().get("latest_position");
        assertTrue(position.contains("LAT: 51.6436"), "Latitude com 4 casas decimais");
        assertTrue(position.contains("LON: 36.5679"), "Longitude com 4 casas decimais");
    }

    @Test
    @DisplayName("Deve retornar 'No data available' quando posição é nula")
    void testNoDataAvailableWhenPositionNull() {
        // Arrange
        when(cacheService.hasData()).thenReturn(false);
        when(cacheService.getLatestPosition()).thenReturn(null);

        // Act
        Health health = readinessCheck.health();

        // Assert
        assertEquals("OUT_OF_SERVICE", health.getStatus().getCode());
    }

    @Test
    @DisplayName("Deve incluir detalhes de posição no UP status")
    void testUpStatusIncludesPositionDetails() {
        // Arrange
        PositionFix positionFix = PositionFix.builder()
                .latitude(-22.8945)
                .longitude(-43.1942)  // Rio de Janeiro
                .pixelX(250)
                .pixelY(180)
                .timestamp(1712000000L)
                .updateAt(Instant.now())
                .build();

        when(cacheService.hasData()).thenReturn(true);
        when(cacheService.getLatestPosition()).thenReturn(positionFix);

        // Act
        Health health = readinessCheck.health();

        // Assert
        assertNotNull(health.getDetails().get("latest_position"));
        assertEquals(2, health.getDetails().size());  // message + latest_position
    }
}

