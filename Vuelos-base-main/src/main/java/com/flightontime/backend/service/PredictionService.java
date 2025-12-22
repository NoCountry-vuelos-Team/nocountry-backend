package com.flightontime.backend.service;

import com.flightontime.backend.client.DataScienceClient;
//import com.flightontime.backend.domain.Prediction;
import com.flightontime.backend.dto.request.PredictionRequest;
import com.flightontime.backend.dto.response.PredictionResponse;
//import com.flightontime.backend.repository.PredictionRepository;
import com.flightontime.backend.validation.PredictValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PredictionService {

    // Repository comentado temporalmente - no se usa base de datos por el momento
    //private final PredictionRepository repository;
    private final DataScienceClient dataScienceClient;
    private final PredictValidator predictValidator;
    
    /**
     * Valida el request convirtiendo todos los strings a mayúsculas antes de enviarlo al validador.
     * Esto asegura que la validación se haga con los valores normalizados.
     */
    public String validation(PredictionRequest request) {
        // Convertir todos los strings a mayúsculas antes de validar
        PredictionRequest normalizedRequest = normalizeToUpperCase(request);
        
        // Enviar al validador con los valores normalizados
        predictValidator.validAreoline(normalizedRequest);
        
        return "serviciofuncionando";
    }
    
    /**
     * Crea un nuevo PredictionRequest con todos los strings convertidos a mayúsculas.
     * Los valores numéricos y fechas se mantienen igual.
     */
    private PredictionRequest normalizeToUpperCase(PredictionRequest request) {
        if (request == null) {
            return null;
        }
        
        return new PredictionRequest(
            request.aerolinea() != null ? request.aerolinea().toUpperCase() : null,
            request.origen() != null ? request.origen().toUpperCase() : null,
            request.destino() != null ? request.destino().toUpperCase() : null,
            request.fechaPartida(),
            request.distanciaKm()
        );
    }
    
    
    
    

    // Persistencia a base de datos comentada temporalmente
    //@Transactional
    public PredictionResponse predict(PredictionRequest request) {
        /*
        Prediction prediction = new Prediction();
        prediction.setAerolinea(request.aerolinea());
        prediction.setOrigen(request.origen());
        prediction.setDestino(request.destino());
        prediction.setFechaPartida(request.fechaPartida());
        prediction.setDistanciaKm(request.distanciaKm());
        repository.save(prediction);
        */

        // Llamada (o mock) al modelo de Data Science
        return dataScienceClient.predictDelay(request);
    }
}


