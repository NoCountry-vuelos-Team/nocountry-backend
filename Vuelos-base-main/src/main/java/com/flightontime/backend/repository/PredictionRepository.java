package com.flightontime.backend.repository;


import com.flightontime.backend.persistence.entity.PredictionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PredictionRepository extends JpaRepository<PredictionEntity, Long> {
}