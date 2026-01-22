package com.flightontime.backend.client;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
     * Llama a la API de Data Science de forma sincrónica.
     * 
     * Con la normalización implementada en el validador, los datos siempre llegan
     * correctamente formateados a esta API, minimizando errores 4xx.
     * 
     * En caso de errores de conexión o del servidor, se propagan hacia el
     * GlobalExceptionHandler para ser gestionados apropiadamente.
     */
    private PredictionResponse callDataScienceApi(DataSciencePredictionRequest dsRequest) {
        log.debug("Llamando a la API de Data Science: {}", dataScienceApiUrl);
        try {
            PredictionResponse response = restTemplate.postForObject(dataScienceApiUrl, dsRequest, PredictionResponse.class);
            log.debug("Respuesta recibida exitosamente de la API de Data Science");
            return response;
        } catch (Exception e) {
            log.error("Error al llamar a la API de Data Science: {}", e.getMessage(), e);
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


