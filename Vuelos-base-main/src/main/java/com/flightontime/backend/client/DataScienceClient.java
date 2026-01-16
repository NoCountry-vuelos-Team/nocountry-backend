package com.flightontime.backend.client;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.flightontime.backend.dto.request.PredictionRequest;
import com.flightontime.backend.dto.request.DataSciencePredictionRequest;
import com.flightontime.backend.dto.response.PredictionResponse;

import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
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
        return restTemplate.postForObject(dataScienceApiUrl, dsRequest, PredictionResponse.class);
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


