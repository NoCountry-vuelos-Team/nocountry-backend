package com.flightontime.backend.validation;

import com.flightontime.backend.dto.request.PredictionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
                () -> validator.validAreoline(request.aerolinea())
        );

        assertTrue(exception.getMessage().contains("no existe en el catálogo"));
    }

    @Test
    void shouldPassWhenAirlineExists() {
        // Usa un código que esté en catalog/airlines.csv (p.ej. AA)
        PredictionRequest request = buildRequest("AA", "MAD", "GRU");

        assertDoesNotThrow(() -> validator.validAreoline(request.aerolinea()));
    }

    @Test
    void shouldFailWhenAirlineIsNull() {
        PredictionRequest request = buildRequest(null, "MAD", "GRU");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validAreoline(request.aerolinea())
        );

        assertEquals("Campo o catalogo es null o vacio", exception.getMessage());
    }

    @Test
    void shouldFailWhenAirlineIsBlank() {
        PredictionRequest request = buildRequest("   ", "MAD", "GRU");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validAreoline(request.aerolinea())
        );

        assertEquals("Campo o catalogo es null o vacio", exception.getMessage());
    }

    // ========== TESTS PARA ORIGEN ==========

    @Test
    void shouldFailWhenOriginDoesNotExist() {
        PredictionRequest request = buildRequest("AA", "INVALID_ORIGIN", "GRU");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validAirport(request.origen())
        );

        assertTrue(exception.getMessage().contains("no existe en el catálogo"));
    }

    @Test
    void shouldFailWhenOriginIsNull() {
        PredictionRequest request = buildRequest("AA", null, "GRU");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validAirport(request.origen())
        );

        assertEquals("Campo o catalogo es null o vacio", exception.getMessage());
    }

    @Test
    void shouldFailWhenOriginIsBlank() {
        PredictionRequest request = buildRequest("AA", "   ", "GRU");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validAirport(request.origen())
        );

        assertEquals("Campo o catalogo es null o vacio", exception.getMessage());
    }

    @Test
    void shouldPassWhenOriginExists() {
        // Usa un código que esté en catalog/origen-destino.csv (p.ej. MAD)
        PredictionRequest request = buildRequest("AA", "MAD", "GRU");

        assertDoesNotThrow(() -> validator.validAirport(request.origen()));
    }

    // ========== TESTS PARA DESTINO ==========

    @Test
    void shouldFailWhenDestinationDoesNotExist() {
        PredictionRequest request = buildRequest("AA", "MAD", "INVALID_DESTINATION");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validAirport(request.destino())
        );

        assertTrue(exception.getMessage().contains("no existe en el catálogo"));
    }

    @Test
    void shouldFailWhenDestinationIsNull() {
        PredictionRequest request = buildRequest("AA", "MAD", null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validAirport(request.destino())
        );

        assertEquals("Campo o catalogo es null o vacio", exception.getMessage());
    }

    @Test
    void shouldFailWhenDestinationIsBlank() {
        PredictionRequest request = buildRequest("AA", "MAD", "   ");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validAirport(request.destino())
        );

        assertEquals("Campo o catalogo es null o vacio", exception.getMessage());
    }

    @Test
    void shouldPassWhenDestinationExists() {
        // Usa un código que esté en catalog/origen-destino.csv (p.ej. GRU)
        PredictionRequest request = buildRequest("AA", "MAD", "GRU");

        assertDoesNotThrow(() -> validator.validAirport(request.destino()));
    }

    // ========== TEST COMBINADO ==========

    @Test
    void shouldPassWhenBothAirportsExist() {
        PredictionRequest request = buildRequest("AA", "MAD", "GRU");

        assertDoesNotThrow(() -> {
            validator.validAreoline(request.aerolinea());
            validator.validAirport(request.origen());
            validator.validAirport(request.destino());
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

    @Test
    void shouldFailWhenDateIsInThePast() {
        LocalDateTime pastDate = LocalDateTime.now().minusMinutes(1);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validateDepartureDateIsNotPast(pastDate)
        );

        assertEquals("La fecha de partida debe ser futura", ex.getMessage());
    }

    @Test
    void shouldPassWhenDateIsNow() {
        LocalDateTime now = LocalDateTime.now().withNano(0);

        assertDoesNotThrow(
                () -> validator.validateDepartureDateIsNotPast(now)
        );
    }

    @Test
    void shouldPassWhenDateIsFuture() {
        LocalDateTime futureDate = LocalDateTime.now().plusMinutes(1);

        assertDoesNotThrow(
                () -> validator.validateDepartureDateIsNotPast(futureDate)
        );
    }

    @Test
    void shouldFailWhenOriginEqualsDestination() {
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> validator.validateOriginAndDestinationAreDifferent("EZE", "eze")
        );

        assertEquals(
                "El origen y el destino no pueden ser iguales",
                exception.getReason()
        );
    }

    @Test
    void shouldPassWhenOriginAndDestinationAreDifferent() {
        assertDoesNotThrow(
                () -> validator.validateOriginAndDestinationAreDifferent("EZE", "MDZ")
        );
    }

}


