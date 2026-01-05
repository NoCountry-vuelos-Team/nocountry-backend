package com.flightontime.backend.persistence.entity;

// Imports de JPA comentados temporalmente
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.PrePersist;
//import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Anotaciones de JPA comentadas temporalmente
//@Entity
//@Table(name = "prediction_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prediction {

    // Anotaciones de JPA comentadas temporalmente
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aerolinea;
    private String origen;
    private String destino;
    private LocalDateTime fechaPartida;
    private Integer distanciaKm;
    private LocalDateTime createdAt;

    // Método de callback de JPA comentado temporalmente
    //@PrePersist
    public void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}


