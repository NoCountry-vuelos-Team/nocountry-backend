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
        if (request == null || request.aerolinea() == null || request.aerolinea().isBlank()) {
            throw new IllegalArgumentException("La aerolínea es obligatoria");
        }

        String codigo = request.aerolinea().trim().toUpperCase();

        Set<String> codigosCatalogo = loadAirlinesFromCatalog();

        if (!codigosCatalogo.contains(codigo)) {
            throw new IllegalArgumentException(
                    "La aerolínea '" + codigo + "' no existe en el catálogo de aerolíneas");
        }
    }

    /**
     * Valida que el origen del request exista en el catálogo
     * de origen-destino definido en resources/catalog/origen-destino.csv.
     *
     * Si el origen no existe, lanza IllegalArgumentException para
     * cortar el flujo y que el controlador/manejador de errores responda al cliente.
     */
    public void validOrigin(PredictionRequest request){

        if(request == null || request.origen() == null || request.origen().isBlank()){
            throw new IllegalArgumentException("El aeropuerto de origen es obligatorio");
        }

        String codigo = request.origen().trim().toUpperCase();

        Set<String> codigoOrigen = loadOrigenDestinoFromCatalog();

        if(!codigoOrigen.contains(codigo)){
            throw new IllegalArgumentException("El aeropuerto de origen no existe");
        }
    }

    /**
     * Valida que el destino del request exista en el catálogo
     * de origen-destino definido en resources/catalog/origen-destino.csv.
     *
     * Si el destino no existe, lanza IllegalArgumentException para
     * cortar el flujo y que el controlador/manejador de errores responda al cliente.
     */
    public void validDestination(PredictionRequest request){

        if(request == null || request.destino() == null || request.destino().isBlank()){
            throw new IllegalArgumentException("El aeropuerto de destino es obligatorio");
        }

        String codigo = request.destino().trim().toUpperCase();

        Set<String> codigoDestino = loadOrigenDestinoFromCatalog();

        if(!codigoDestino.contains(codigo)){
            throw new IllegalArgumentException("El aeropuerto de destino no existe");
        }
    }

    /**
     * Lee el archivo airlines.csv desde el classpath y devuelve
     * el conjunto de códigos de aerolínea válidos.
     *
     * Formato esperado actual del archivo (una columna):
     *   code
     *   AA
     *   DL
     *   ...
     */
    private Set<String> loadAirlinesFromCatalog() {
        ClassPathResource resource = new ClassPathResource("catalog/airlines.csv");

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
                    "No se pudo leer el catálogo de aerolíneas (catalog/airlines.csv)", e);
        }
    }

    /**
     * Lee el archivo origen-destino.csv desde el classpath y devuelve
     * el conjunto de códigos de origenes y destinos válidos.
     *
     * Formato esperado actual del archivo (una columna):
     *   code
     *  MAD
     *  GRU
     *   ...
     */
    private Set<String> loadOrigenDestinoFromCatalog() {
        ClassPathResource resource = new ClassPathResource("catalog/origen-destino.csv");

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
                    "No se pudo leer el catálogo de origen-destino (catalog/origen-destino.csv)", e);
        }
    }
}