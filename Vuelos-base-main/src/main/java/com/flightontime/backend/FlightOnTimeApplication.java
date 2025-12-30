package com.flightontime.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlightOnTimeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlightOnTimeApplication.class, args);
        System.out.println(">>> La aplicación FlightOnTime ya está corriendo.");
    }
}


