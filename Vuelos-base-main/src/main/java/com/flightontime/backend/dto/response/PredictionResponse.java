package com.flightontime.backend.dto.response;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record PredictionResponse(
		 @NotNull
	        String prevision,

	        @NotNull
	        @DecimalMin(value = "0.0", inclusive = true)
	        @DecimalMax(value = "1.0", inclusive = true)
	        Double probabilidad
) {
}


