CREATE TABLE prediction_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    aerolinea VARCHAR(10) NOT NULL,
    origen VARCHAR(10) NOT NULL,
    destino VARCHAR(10) NOT NULL,
    fecha_partida TIMESTAMP NOT NULL,
    distancia_km INT NOT NULL,
    created_at TIMESTAMP NOT NULL
);
