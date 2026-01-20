package com.flightontime.backend.dto.response;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resultado de la predicción de retraso de vuelo")
public record PredictionResponse(

		@Schema(
				description = "Resultado de la predicción del vuelo",
				example = "A TIEMPO",
				allowableValues = {"A TIEMPO", "RETRASADO"}
		)
		@NotNull
		String prevision,

		@Schema(
				description = "Nivel de confianza de la predicción (0.0 = 0%, 1.0 = 100%)",
				example = "0.85",
				minimum = "0.0",
				maximum = "1.0"
		)
		@NotNull
		@DecimalMin(value = "0.0", inclusive = true)
		@DecimalMax(value = "1.0", inclusive = true)
		Double probabilidad
) {
}