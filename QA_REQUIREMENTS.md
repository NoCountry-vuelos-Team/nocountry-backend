# FlightOnTime – Backend Quality & Integration Requirements

Este documento define los requisitos mínimos de calidad, integración y validación que debe cumplir la implementación del Back-End para el proyecto FlightOnTime.
Su objetivo es garantizar estabilidad, consistencia y facilidad de testeo durante el hackathon.

# 1. Alcance del Back-End
   
El Back-End debe exponer una API REST capaz de recibir información de un vuelo y devolver una predicción de puntualidad basada en un modelo de Data Science.

2. Endpoint obligatorio
POST /predict

Request (JSON)
{
  "aerolinea": "AZ",
  "origen": "GIG",
  "destino": "GRU",
  "fecha_partida": "2025-11-10T14:30:00",
  "distancia_km": 350
}

# Reglas de validación

Todos los campos son obligatorios

aerolinea: string, 2–3 caracteres, se normaliza a MAYÚSCULAS

origen: string, 3 caracteres , MAYÚSCULAS

destino: string, 3 caracteres , MAYÚSCULAS

fecha_partida: formato ISO-8601 válido (YYYY-MM-DDTHH:mm:ss)

distancia_km: número mayor a 0

Response (200 OK)
{
  "prevision": "Retrasado",
  "probabilidad": 0.78
}

# Reglas de respuesta

prevision: solo "Puntual" o "Retrasado"

probabilidad: número decimal entre 0 y 1

No se permiten valores booleanos, strings numéricos ni códigos alternativos

# Manejo de errores

Todos los errores deben devolver JSON estructurado.

Errores de validación (400)
{
  "error": "VALIDATION_ERROR",
  "details": ["fecha_partida inválida"]
}


Casos que deben producir 400:

Campo faltante

Tipo incorrecto

Fecha inválida

distancia_km <= 0

Errores de integración o modelo

Si el modelo no puede procesar la entrada → 422

Si el modelo no está disponible → 503

500 solo para errores internos no controlados

# Normalización de datos

Antes de enviar datos al modelo:

trim() y conversión a MAYÚSCULAS

Decisión explícita para:

origen == destino

Aerolíneas o aeropuertos no conocidos

Nunca permitir que estos casos generen error 500

# Documentación obligatoria (README)

Versión de Java y herramienta de build

Pasos exactos para ejecutar localmente

3 ejemplos funcionales:

Vuelo puntual

Vuelo retrasado

Error de validación

Ejemplo real de respuesta JSON

# Requisitos para demo

Puerto definido

Endpoint funcional sin pasos manuales adicionales

Logs visibles para:

Request recibido

Error de validación

Predicción exitosa

Error de integración

# Criterios de aceptacion por parte de QA.
Se deben cumplir todos los puntos mencionados en este documento para asi asegurar la funcionalidad de dicho proyecto



