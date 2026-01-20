package com.flightontime.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Item del historial de predicciones de vuelos")
public record PredictionHistoryItem(
        @Schema(description = "ID único de la predicción", example = "1")
        Long id,

        @Schema(description = "Código IATA de la aerolínea", example = "AA")
        String aerolinea,

        @Schema(description = "Código IATA del aeropuerto de origen", example = "SFO")
        String origen,

        @Schema(description = "Código IATA del aeropuerto de destino", example = "LAX")
        String destino,

        @Schema(description = "Fecha y hora programada de partida del vuelo", example = "2024-01-15T14:30:00")
        LocalDateTime fechaPartida,

        @Schema(description = "Distancia del vuelo en kilómetros", example = "559")
        Integer distanciaKm,

        @Schema(
                description = "Resultado de la predicción del vuelo",
                example = "A TIEMPO",
                allowableValues = {"A TIEMPO", "RETRASADO"}
        )
        String prevision,

        @Schema(
                description = "Nivel de confianza de la predicción (0.0 = 0%, 1.0 = 100%)",
                example = "0.85",
                minimum = "0.0",
                maximum = "1.0"
        )
        Double probabilidad,

        @Schema(description = "Fecha y hora en que se creó el registro", example = "2024-01-15T10:30:00")
        LocalDateTime createdAt,

        @Schema(description = "Fecha y hora de la última actualización del registro", example = "2024-01-15T10:30:00")
        LocalDateTime updatedAt
) {
}


