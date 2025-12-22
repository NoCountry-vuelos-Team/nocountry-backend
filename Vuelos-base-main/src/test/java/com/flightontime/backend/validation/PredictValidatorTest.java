package com.flightontime.backend.validation;

import com.flightontime.backend.dto.request.PredictionRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PredictValidatorTest {

    private final PredictValidator validator = new PredictValidator();

    private PredictionRequest buildRequest(String airlineCode) {
        return new PredictionRequest(
                airlineCode,
                "AAA",
                "BBB",
                LocalDateTime.now().plusDays(1),
                100.0
        );
    }

    @Test
    void shouldFailWhenAirlineDoesNotExist() {
        PredictionRequest request = buildRequest("ZZ");

        assertThrows(IllegalArgumentException.class,
                () -> validator.validAreoline(request),
                "Se esperaba una excepción cuando la aerolínea no existe en el catálogo");
    }

    @Test
    void shouldPassWhenAirlineExists() {
        // Usa un código que esté en catalog/airlines.csv (p.ej. AA)
        PredictionRequest request = buildRequest("AA");

        assertDoesNotThrow(
                () -> validator.validAreoline(request),
                "No debería lanzar excepción cuando la aerolínea existe en el catálogo"
        );
    }
}


