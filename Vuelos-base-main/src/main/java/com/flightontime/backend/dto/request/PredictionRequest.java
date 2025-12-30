package com.flightontime.backend.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record PredictionRequest(

		@NotBlank(message = "La aerolínea es obligatoria")
        @Size(min = 2, max = 2, message = "La aerolínea debe tener exactamente 2 caracteres")
        @Pattern(
            regexp = "^[A-Za-z]+$",
            message = "La aerolínea debe contener solo letras"
        )
		
//		Decisión recomendada para el proyecto
//		Criterio	                               Decisión
//		UX (user experience)	                  ✔️ Aceptar minúsculas
//		Contrato DS	                              ✔️ Enviar mayúsculas
//		Lugar de conversión	                      ✔️ Service .toUpperCase();
		
		
		
        String aerolinea,

        @NotBlank(message = "El origen es obligatorio")
        @Size(min = 3, max = 3, message = "El origen debe tener exactamente 3 caracteres")
        @Pattern(
            regexp = "^[A-Za-z]+$",
            message = "El origen debe contener solo letras"
        )
        String origen,

        @NotBlank(message = "El destino es obligatorio")
        @Size(min = 3, max = 3, message = "El destino debe tener exactamente 3 caracteres")
        @Pattern(
            regexp = "^[A-Za-z]+$",
            message = "El destino debe contener solo letras"
        )
        String destino,

        @NotNull(message = "La fecha de partida es obligatoria")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime fechaPartida,

        @NotNull(message = "La distancia es obligatoria")
        @Positive(message = "La distancia debe ser mayor a 0")
		//Longitud de distancia máxima: 7 caracteres
        @Digits(integer = 7, fraction = 2, message = "Formato inválido para distancia_km")
        Double distanciaKm
) {
}


