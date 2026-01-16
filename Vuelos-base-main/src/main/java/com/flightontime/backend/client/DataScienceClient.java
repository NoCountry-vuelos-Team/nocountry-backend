package com.flightontime.backend.client;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpServerErrorException;

import com.flightontime.backend.dto.request.PredictionRequest;
import com.flightontime.backend.dto.request.DataSciencePredictionRequest;
import com.flightontime.backend.dto.response.PredictionResponse;

import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataScienceClient {

    private final RestTemplate restTemplate;

    @Value("${datascience.api.url:}")
    private String dataScienceApiUrl;

    private static final DateTimeFormatter DS_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public PredictionResponse predictDelay(PredictionRequest request) {
        // Si no está configurada la URL del modelo, devolvemos un mock como en el prototipo original
        if (dataScienceApiUrl == null || dataScienceApiUrl.isBlank()) {
            return new PredictionResponse("RETRASADO", 0.78);
        }

        DataSciencePredictionRequest dsRequest = mapToDsRequest(request);
        return callDataScienceApi(dsRequest);
    }

    /**
     * Llama a la API de Data Science con reintentos automáticos.
     * Reintenta máximo 3 veces (1 intento inicial + 2 reintentos) con backoff exponencial:
     * - Primer reintento: después de 500ms
     * - Segundo reintento: después de 1000ms
     * 
     * Solo reintenta en errores 5xx del servidor y errores de conexión/timeout.
     * No reintenta en errores 4xx (errores del cliente).
     */
    @Retryable(
            maxAttempts = 3,
            backoff = @Backoff(delay = 500, multiplier = 2.0, maxDelay = 2000),
            retryFor = {
                    HttpServerErrorException.class,
                    ResourceAccessException.class,
                    RestClientException.class
            },
            noRetryFor = {
                    org.springframework.web.client.HttpClientErrorException.class
            }
    )
    private PredictionResponse callDataScienceApi(DataSciencePredictionRequest dsRequest) {
        log.debug("Llamando a la API de Data Science: {}", dataScienceApiUrl);
        try {
            PredictionResponse response = restTemplate.postForObject(dataScienceApiUrl, dsRequest, PredictionResponse.class);
            log.debug("Respuesta recibida exitosamente de la API de Data Science");
            return response;
        } catch (HttpServerErrorException e) {
            log.warn("Error 5xx del servidor de Data Science (será reintentado): Status {}, Mensaje: {}", 
                    e.getStatusCode(), e.getMessage());
            throw e;
        } catch (ResourceAccessException e) {
            log.warn("Error de conexión/timeout con la API de Data Science (será reintentado): {}", e.getMessage());
            throw e;
        } catch (RestClientException e) {
            log.warn("Error del cliente REST (será reintentado): {}", e.getMessage());
            throw e;
        }
    }

    private DataSciencePredictionRequest mapToDsRequest(PredictionRequest request) {
        return new DataSciencePredictionRequest(
                request.aerolinea(),
                request.origen(),
                request.destino(),
                request.fecha_Partida().format(DS_DATE_FORMAT),
                request.distancia_km()
        );
    }
}


