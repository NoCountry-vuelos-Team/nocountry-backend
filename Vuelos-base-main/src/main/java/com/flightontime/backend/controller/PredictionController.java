package com.flightontime.backend.controller;

import com.flightontime.backend.dto.request.PredictionRequest;
import com.flightontime.backend.dto.response.PredictionResponse;
import com.flightontime.backend.service.PredictionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/predict")
@RequiredArgsConstructor
public class PredictionController {

    private final PredictionService predictionService;
    
    //Endpoint para verificar que el controller esta funcionando
    @GetMapping("/ping")
    public ResponseEntity<String> ping(){
    	return ResponseEntity.ok("OK");
    } 
    
    //Endpoint principal 

    @PostMapping
    public ResponseEntity<String> predict(
            @RequestBody @Valid PredictionRequest request
    ) {
        //PredictionResponse response = predictionService.predict(request);
        return ResponseEntity.ok("Endpoint principal funcionando correctamente");
    }
}


