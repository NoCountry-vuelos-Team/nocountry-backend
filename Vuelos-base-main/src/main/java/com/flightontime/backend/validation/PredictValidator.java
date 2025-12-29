package com.flightontime.backend.validation;

import com.flightontime.backend.dto.request.PredictionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.stream.Collectors;
import java.time.LocalDateTime;


@Component
@RequiredArgsConstructor
public class PredictValidator {

	/**
	 * Valida el request convirtiendo todos los strings a mayúsculas antes de
	 * enviarlo al validador. Esto asegura que la validación se haga con los valores
	 * normalizados.
	 */
	public void validation(PredictionRequest request) {

		PredictionRequest normalizedRequest = normalizeToUpperCase(request);
		validAreoline(normalizedRequest.aerolinea());
		validAirport(normalizedRequest.origen()); // validation origin airport
		validAirport(normalizedRequest.destino()); // validation destination airport
		validateDepartureDateIsNotPast(request.fechaPartida());
	}

	/**
	 * Valida que la aerolínea del request exista en el catálogo de airlines
	 * definido en resources/catalog/airlines.csv.
	 */
	public void validAreoline(String aerolinea) {
		validateField(aerolinea, loadCatalogFromFile("catalog/airlines.csv"), "airlines.csv");
	}

	/**
	 * Valida que el origen o el destino exista en el catálogo de airports
	 * definido en resources/catalog/airports.csv.
	 */
	public void validAirport(String airport) {
		validateField(airport, loadCatalogFromFile("catalog/airports.csv"), "airports.csv");
	}

	/**
	 * Método genérico para validar un campo contra un catálogo.
	 */
	private void validateField(String fieldName, Set<String> catalog, String catalogName) {
		if (fieldName == null || fieldName.isBlank() || catalog.isEmpty()) {
			throw new IllegalArgumentException("Campo o catalogo es null o vacio");
		}

		if (!catalog.contains(fieldName)) {
			throw new IllegalArgumentException("El codigo " + fieldName + " no existe en el catálogo " + catalogName);
		}
	}

	/**
	 * Lee un archivo CSV desde el classpath y devuelve el conjunto de códigos
	 * válidos.
	 *
	 * Formato esperado del archivo (una columna): code AA DL ...
	 */
	private Set<String> loadCatalogFromFile(String filePath) {
		ClassPathResource resource = new ClassPathResource(filePath);

		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

			return reader.lines().map(String::trim).filter(line -> !line.isEmpty()).skip(1) // saltar cabecera "code"
					.map(String::toUpperCase).collect(Collectors.toSet());

		} catch (IOException e) {
			throw new IllegalStateException("No se pudo leer el catálogo de  (" + filePath + ")", e);
		}
	}

	   /**
     * Valida que la fecha de partida no esté en el pasado.
     *
     * Reglas:
     * - fechas anteriores a now() → error
     * - fecha igual a now() → permitido
     * - fechas futuras → permitido
     */
	   public void validateDepartureDateIsNotPast(LocalDateTime fechaPartida) {
        if (fechaPartida == null) {
            throw new IllegalArgumentException("La fecha de partida es obligatoria");
        }

        LocalDateTime now = LocalDateTime.now().withNano(0);
        LocalDateTime departure = fechaPartida.withNano(0);

        if (departure.isBefore(now)) {
            throw new IllegalArgumentException(
                    "La fecha de partida debe ser futura"
            );
        }
    }

	/**
	 * Crea un nuevo PredictionRequest con todos los strings convertidos a
	 * mayúsculas. Los valores numéricos y fechas se mantienen igual.
	 */
	private PredictionRequest normalizeToUpperCase(PredictionRequest request) {
		if (request == null) {
			return null;
		}

		return new PredictionRequest(request.aerolinea() != null ? request.aerolinea().toUpperCase() : null,
				request.origen() != null ? request.origen().toUpperCase() : null,
				request.destino() != null ? request.destino().toUpperCase() : null, request.fechaPartida(),
				request.distanciaKm());
	}

}
