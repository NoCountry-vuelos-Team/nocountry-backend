package com.flightontime.backend.service;

import com.flightontime.backend.client.DataScienceClient;
import com.flightontime.backend.persistence.entity.PredictionEntity;
import com.flightontime.backend.dto.request.PredictionRequest;
import com.flightontime.backend.dto.response.PredictionResponse;
import com.flightontime.backend.persistence.entity.PredictionResult;
import com.flightontime.backend.repository.PredictionRepository;
import com.flightontime.backend.validation.PredictValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PredictionService {

    private final PredictionRepository repository;
    private final DataScienceClient dataScienceClient;
    private final PredictValidator predictValidator;
    


    public PredictionResponse predict(PredictionRequest request) {
        log.debug("Iniciando predict metodo: aerolinea={}, origen={}, destino={}", 
                request.aerolinea(), request.origen(), request.destino());
        
        try {
            // Validación de datos de entrada
            predictValidator.validation(request);
            // Llamada (o mock) al modelo de Data Science
            PredictionResponse response = dataScienceClient.predictDelay(request);
            log.info("Predicción completada exitosamente: prevision={}, probabilidad={}", 
                    response.prevision(), response.probabilidad());

            savePrediction(request, response);

            log.info("Predicción completada exitosamente: prevision={}, probabilidad={}",
                    response.prevision(), response.probabilidad());
            return response;
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.warn("Error de validación del request: {}", e.getMessage(), e);
            // Re-lanzamos la excepción para que el GlobalExceptionHandler la maneje como 400
            throw e;
            
        } catch (HttpClientErrorException e) {
            log.error("Error del cliente HTTP al consultar modelo de Data Science. Status: {}, Response: {}", 
                    e.getStatusCode(), e.getResponseBodyAsString(), e);
            // Para 422 (Unprocessable Entity), se propaga con el status code original
            if (e.getStatusCode().value() == 422) {
                throw new RuntimeException("El modelo de Data Science no pudo procesar la solicitud: " + e.getMessage(), e);
            }
            // Para otros errores 4xx, se propaga como error de servicio
            throw new RuntimeException("Error al consultar el modelo de Data Science: " + e.getMessage(), e);
            
        } catch (HttpServerErrorException e) {
            log.error("Error del servidor HTTP en el modelo de Data Science. Status: {}, Response: {}", 
                    e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new RuntimeException("Error interno en el modelo de Data Science: " + e.getMessage(), e);
            
        } catch (ResourceAccessException e) {
            log.error("No se pudo conectar con el servicio de Data Science: {}", e.getMessage(), e);
            throw new RuntimeException("El servicio de Data Science no está disponible temporalmente", e);
            
        } catch (RestClientException e) {
            log.error("Error inesperado al comunicarse con el modelo de Data Science: {}", e.getMessage(), e);
            throw new RuntimeException("Error al comunicarse con el modelo de Data Science: " + e.getMessage(), e);
            
        } catch (Exception e) {
            log.error("Error inesperado durante la predicción: {}", e.getMessage(), e);
            throw new RuntimeException("Error inesperado al procesar la predicción: " + e.getMessage(), e);
        }
    }


    @Transactional
    private void savePrediction(PredictionRequest request, PredictionResponse response) {
        try {
            PredictionEntity entity = new PredictionEntity();

            entity.setAerolinea(request.aerolinea().toUpperCase());
            entity.setOrigen(request.origen().toUpperCase());
            entity.setDestino(request.destino().toUpperCase());
            entity.setFechaPartida(request.fecha_Partida());
            entity.setDistanciaKm(request.distancia_km().intValue());
            entity.setPrevision(PredictionResult.valueOf(response.prevision().toUpperCase()));
            entity.setProbabilidad(response.probabilidad());

            repository.save(entity);

            log.debug("Predicción persistida exitosamente con ID: {}", entity.getId());

        } catch (Exception e) {
            log.error("Error al persistir la predicción en la base de datos. La predicción se completó pero no se guardó en historial.", e);
        }
    }
}



