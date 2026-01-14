package com.flightontime.backend.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "predictions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2)
    private String aerolinea;

    @Column(nullable = false, length = 3)
    private String origen;

    @Column(nullable = false, length = 3)
    private String destino;

    @Column(nullable = false)
    private LocalDateTime fechaPartida;

    @Column(nullable = false)
    private Integer distanciaKm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PredictionResult prevision;

    @Column(nullable = false)
    private Double probabilidad;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
