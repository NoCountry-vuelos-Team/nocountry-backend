package com.flightontime.backend.validation;

import com.flightontime.backend.dto.request.PredictionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PredictValidatorTest {

    private PredictValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PredictValidator();
    }

    // ========== TESTS PARA AEROLÍNEA ==========

    @Test
    void shouldFailWhenAirlineDoesNotExist() {
        PredictionRequest request = buildRequest("INVALID_AIRLINE", "MAD", "GRU");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validAreoline(request)
        );

        assertTrue(exception.getMessage().contains("no existe en el catálogo"));
    }

    @Test
    void shouldPassWhenAirlineExists() {
        // Usa un código que esté en catalog/airlines.csv (p.ej. AA)
        PredictionRequest request = buildRequest("AA", "MAD", "GRU");

        assertDoesNotThrow(() -> validator.validAreoline(request));
    }

    @Test
    void shouldFailWhenAirlineIsNull() {
        PredictionRequest request = buildRequest(null, "MAD", "GRU");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validAreoline(request)
        );

        assertEquals("La aerolínea es obligatoria", exception.getMessage());
    }

    @Test
    void shouldFailWhenAirlineIsBlank() {
        PredictionRequest request = buildRequest("   ", "MAD", "GRU");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validAreoline(request)
        );

        assertEquals("La aerolínea es obligatoria", exception.getMessage());
    }

    // ========== TESTS PARA ORIGEN ==========

    @Test
    void shouldFailWhenOriginDoesNotExist() {
        PredictionRequest request = buildRequest("AA", "INVALID_ORIGIN", "GRU");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validOrigin(request)
        );

        assertTrue(exception.getMessage().contains("no existe en el catálogo"));
    }

    @Test
    void shouldFailWhenOriginIsNull() {
        PredictionRequest request = buildRequest("AA", null, "GRU");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validOrigin(request)
        );

        assertEquals("El aeropuerto de origen es obligatorio", exception.getMessage());
    }

    @Test
    void shouldFailWhenOriginIsBlank() {
        PredictionRequest request = buildRequest("AA", "   ", "GRU");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validOrigin(request)
        );

        assertEquals("El aeropuerto de origen es obligatorio", exception.getMessage());
    }

    @Test
    void shouldPassWhenOriginExists() {
        // Usa un código que esté en catalog/origen-destino.csv (p.ej. MAD)
        PredictionRequest request = buildRequest("AA", "MAD", "GRU");

        assertDoesNotThrow(() -> validator.validOrigin(request));
    }

    // ========== TESTS PARA DESTINO ==========

    @Test
    void shouldFailWhenDestinationDoesNotExist() {
        PredictionRequest request = buildRequest("AA", "MAD", "INVALID_DESTINATION");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validDestination(request)
        );

        assertTrue(exception.getMessage().contains("no existe en el catálogo"));
    }

    @Test
    void shouldFailWhenDestinationIsNull() {
        PredictionRequest request = buildRequest("AA", "MAD", null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validDestination(request)
        );

        assertEquals("El aeropuerto de destino es obligatorio", exception.getMessage());
    }

    @Test
    void shouldFailWhenDestinationIsBlank() {
        PredictionRequest request = buildRequest("AA", "MAD", "   ");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validDestination(request)
        );

        assertEquals("El aeropuerto de destino es obligatorio", exception.getMessage());
    }

    @Test
    void shouldPassWhenDestinationExists() {
        // Usa un código que esté en catalog/origen-destino.csv (p.ej. GRU)
        PredictionRequest request = buildRequest("AA", "MAD", "GRU");

        assertDoesNotThrow(() -> validator.validDestination(request));
    }

    // ========== TEST COMBINADO ==========

    @Test
    void shouldPassWhenBothAirportsExist() {
        PredictionRequest request = buildRequest("AA", "MAD", "GRU");

        assertDoesNotThrow(() -> {
            validator.validAreoline(request);
            validator.validOrigin(request);
            validator.validDestination(request);
        });
    }

    // ========== MÉTODO AUXILIAR ==========

    private PredictionRequest buildRequest(String airlineCode, String origin, String destination) {
        return new PredictionRequest(
                airlineCode,
                origin,
                destination,
                LocalDateTime.now().plusDays(1),
                100.0
        );
    }
}