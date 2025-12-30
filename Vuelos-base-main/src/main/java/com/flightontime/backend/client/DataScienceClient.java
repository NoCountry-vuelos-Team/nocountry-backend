package com.flightontime.backend.client;

import com.flightontime.backend.dto.request.PredictionRequest;
import com.flightontime.backend.dto.response.PredictionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class DataScienceClient {

    private final RestTemplate restTemplate;

    @Value("${datascience.api.url:}")
    private String dataScienceApiUrl;

    public PredictionResponse predictDelay(PredictionRequest request) {
        // Si no está configurada la URL del modelo, devolvemos un mock como en el prototipo original
        if (dataScienceApiUrl == null || dataScienceApiUrl.isBlank()) {
            return new PredictionResponse("Retrasado", 0.78);
        }

        String url = dataScienceApiUrl + "/predict";
        return restTemplate.postForObject(url, request, PredictionResponse.class);
    }
}


