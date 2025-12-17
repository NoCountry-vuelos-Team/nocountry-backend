
---

# README – Backend  

# NoCountry – Backend

Este repositorio contiene el desarrollo del **backend** del proyecto NoCountry.  
Proyecto 3: FlightOnTime ✈️ — Predicción de Retrasos de Vuelos

## Objetivo
Aplicación Back-End (API REST) desarrollada en Java (Spring Boot), que contenga:
Endpoint /predict que recibe información de un vuelo y devuelve la predicción;
Integración con el modelo de DS (directa o vía microservicio separado);
Manejo de errores y respuestas estandarizadas en JSON.

---

# Funcionalidades opcionales

Endpoint GET /stats: devuelve estadísticas agregadas (ej.: % de vuelos retrasados en el día).
Persistencia: guardar historial de predicciones y peticiones en una base de datos (H2/PostgreSQL).
Dashboard visual (Streamlit/HTML): muestra, en tiempo real, la tasa de retrasos prevista.
Integración con API externa de clima: añadir condiciones meteorológicas como feature del modelo.
Batch prediction: aceptar un archivo CSV con varios vuelos y devolver las predicciones en lote.
Explicabilidad: devolver las variables más importantes en la decisión (ej.: "Hora de la tarde y aeropuerto GIG aumentan el riesgo").
Contenerización: ejecutar el sistema completo con Docker/Docker Compose.
Pruebas automatizadas: unitarias y de integración simples.

