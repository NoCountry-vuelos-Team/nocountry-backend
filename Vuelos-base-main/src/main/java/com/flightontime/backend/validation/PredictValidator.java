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

@Component
@RequiredArgsConstructor
public class PredictValidator {

    /**
     * Valida que la aerolínea del request exista en el catálogo
     * de airlines definido en resources/catalog/airlines.csv.
     *
     * Si la aerolínea no existe, lanza IllegalArgumentException para
     * cortar el flujo y que el controlador/manejador de errores responda al cliente.
     */
    public void validAreoline(PredictionRequest request) {
        validateField(
                request,
                request != null ? request.aerolinea() : null,
                "La aerolínea es obligatoria",
                loadCatalogFromFile("catalog/airlines.csv", "aerolíneas"),
                "aerolínea"
        );
    }

    /**
     * Valida que el origen del request exista en el catálogo
     * de origen-destino definido en resources/catalog/origen-destino.csv.
     *
     * Si el origen no existe, lanza IllegalArgumentException para
     * cortar el flujo y que el controlador/manejador de errores responda al cliente.
     */
    public void validOrigin(PredictionRequest request) {
        validateField(
                request,
                request != null ? request.origen() : null,
                "El aeropuerto de origen es obligatorio",
                loadCatalogFromFile("catalog/origen-destino.csv", "origen-destino"),
                "aeropuerto de origen"
        );
    }

    /**
     * Valida que el destino del request exista en el catálogo
     * de origen-destino definido en resources/catalog/origen-destino.csv.
     *
     * Si el destino no existe, lanza IllegalArgumentException para
     * cortar el flujo y que el controlador/manejador de errores responda al cliente.
     */
    public void validDestination(PredictionRequest request) {
        validateField(
                request,
                request != null ? request.destino() : null,
                "El aeropuerto de destino es obligatorio",
                loadCatalogFromFile("catalog/origen-destino.csv", "origen-destino"),
                "aeropuerto de destino"
        );
    }

    /**
     * Método genérico para validar un campo contra un catálogo.
     */
    private void validateField(PredictionRequest request, String fieldValue,
                               String emptyMessage, Set<String> catalog, String fieldName) {
        if (request == null || fieldValue == null || fieldValue.isBlank()) {
            throw new IllegalArgumentException(emptyMessage);
        }

        String codigo = fieldValue.trim().toUpperCase();

        if (!catalog.contains(codigo)) {
            throw new IllegalArgumentException(
                    "El " + fieldName + " '" + codigo + "' no existe en el catálogo");
        }
    }

    /**
     * Lee un archivo CSV desde el classpath y devuelve
     * el conjunto de códigos válidos.
     *
     * Formato esperado del archivo (una columna):
     *   code
     *   AA
     *   DL
     *   ...
     */
    private Set<String> loadCatalogFromFile(String filePath, String catalogName) {
        ClassPathResource resource = new ClassPathResource(filePath);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

            return reader.lines()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .skip(1) // saltar cabecera "code"
                    .map(String::toUpperCase)
                    .collect(Collectors.toSet());

        } catch (IOException e) {
            throw new IllegalStateException(
                    "No se pudo leer el catálogo de " + catalogName + " (" + filePath + ")", e);
        }
    }
}