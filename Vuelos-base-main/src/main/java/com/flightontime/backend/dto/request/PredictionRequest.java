package com.flightontime.backend.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de entrada para predecir si un vuelo sufrirá retraso")
public record PredictionRequest(

        @Schema(
                description = "Código IATA de la aerolínea (2 letras). Se acepta mayúsculas o minúsculas",
                example = "AA",
                minLength = 2,
                maxLength = 2
        )
        @NotBlank(message = "La aerolínea es obligatoria")
        @Size(min = 2, max = 2, message = "La aerolínea debe tener exactamente 2 caracteres")
        @Pattern(regexp = "^[A-Za-z]+$", message = "La aerolínea debe contener solo letras")
        String aerolinea,

        @Schema(
                description = "Código IATA del aeropuerto de origen (3 letras)",
                example = "SFO",
                minLength = 3,
                maxLength = 3
        )
        @NotBlank(message = "El origen es obligatorio")
        @Size(min = 3, max = 3, message = "El origen debe tener exactamente 3 caracteres")
        @Pattern(regexp = "^[A-Za-z]+$", message = "El origen debe contener solo letras")
        String origen,

        @Schema(
                description = "Código IATA del aeropuerto de destino (3 letras)",
                example = "LAX",
                minLength = 3,
                maxLength = 3
        )
        @NotBlank(message = "El destino es obligatorio")
        @Size(min = 3, max = 3, message = "El destino debe tener exactamente 3 caracteres")
        @Pattern(regexp = "^[A-Za-z]+$", message = "El destino debe contener solo letras")
        String destino,

        @Schema(
                description = "Fecha y hora programada de partida del vuelo",
                example = "2024-01-15 14:30:00",
                type = "string",
                pattern = "yyyy-MM-dd HH:mm:ss"
        )
        @NotNull(message = "La fecha de partida es obligatoria")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime fechaPartida,

        @Schema(
                description = "Distancia del vuelo en kilómetros (máximo 7 dígitos enteros, 2 decimales)",
                example = "559.23",
                minimum = "0.01",
                maximum = "9999999.99"
        )
        @NotNull(message = "La distancia es obligatoria")
        @Positive(message = "La distancia debe ser mayor a 0")
        @Digits(integer = 7, fraction = 2, message = "Formato inválido para distancia_km")
        Double distanciaKm
) {
}