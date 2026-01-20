package com.flightontime.backend.dto.request;

public record DataSciencePredictionRequest(
        String aerolinea,
        String origen,
        String destino,
        String fecha_partida,
        Double distancia_km
) {
}



