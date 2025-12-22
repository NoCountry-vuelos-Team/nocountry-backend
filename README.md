
FlightOnTime – Backend
📋 Descripción del Proyecto
FlightOnTime es una aplicación Back-End que predice si un vuelo viene atrasado o no. El sistema expone una API REST capaz de recibir información de un vuelo (aerolínea, origen, destino, fecha de partida y distancia) y devolver una predicción de puntualidad basada en un modelo de Data Science.

🎯 Alcance del Back-End
El Back-End debe exponer una API REST capaz de recibir información de un vuelo y devolver una predicción de puntualidad basada en un modelo de Data Science.

Endpoint Obligatorio
POST /predict

Request (JSON):

{
  "aerolinea": "AA",
  "origen": "JFK",
  "destino": "LAX",
  "fechaPartida": "2025-11-10T14:30:00",
  "distanciaKm": 350.0
}
Response (JSON):

{
  "prevision": "Retrasado",
  "probabilidad": 0.78
}
🛠️ Tecnologías y Frameworks
Este proyecto está construido con las siguientes tecnologías:

Java 17 - Lenguaje de programación
Spring Boot 4.0.0 - Framework principal
Spring Web - Para la construcción de la API REST
Spring Validation - Para validación de datos de entrada
Lombok - Para reducir código boilerplate
JUnit 5 - Framework de testing (incluido en spring-boot-starter-test)
Maven - Gestor de dependencias y construcción del proyecto
Dependencias Principales
- spring-boot-starter-web
- spring-boot-starter-validation
- lombok
- spring-boot-starter-test (para tests unitarios)
📦 Requisitos Previos
Antes de ejecutar el proyecto, asegúrate de tener instalado:

Java JDK 17 o superior
Maven 3.6+ (o usar el wrapper incluido mvnw)
Git (para clonar el repositorio)
Verificar Instalación
java -version    # Debe mostrar Java 17 o superior
mvn -version     # Debe mostrar Maven 3.6+ o usar ./mvnw -version
🚀 Instalación y Ejecución
1. Clonar el Repositorio
git clone <url-del-repositorio>
cd Vuelos-base-main
2. Compilar el Proyecto
Usando Maven wrapper (recomendado):

# En Windows
.\mvnw.cmd clean install

# En Linux/Mac
./mvnw clean install
O usando Maven instalado globalmente:

mvn clean install
3. Ejecutar la Aplicación
Opción 1: Usando Maven wrapper

# Windows
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
Opción 2: Usando Maven

mvn spring-boot:run
Opción 3: Ejecutar el JAR compilado

java -jar target/demo-0.0.1-SNAPSHOT.jar
4. Verificar que la Aplicación Está Corriendo
La aplicación se ejecutará por defecto en: http://localhost:8080

Puedes verificar el estado con:

curl http://localhost:8080/predict/ping
Deberías recibir: OK

📁 Estructura del Proyecto
src/
├── main/
│   ├── java/com/flightontime/backend/
│   │   ├── controller/          # Controladores REST
│   │   │   └── PredictionController.java
│   │   ├── service/             # Lógica de negocio
│   │   │   └── PredictionService.java
│   │   ├── repository/          # Repositorios (actualmente comentado - sin BD)
│   │   │   └── PredictionRepository.java
│   │   ├── client/              # Cliente para API de Data Science
│   │   │   └── DataScienceClient.java
│   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── request/
│   │   │   │   └── PredictionRequest.java
│   │   │   └── response/
│   │   │       └── PredictionResponse.java
│   │   ├── exception/           # Manejo de excepciones
│   │   │   ├── ApiError.java
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── validation/          # Validadores personalizados
│   │   │   └── PredictValidator.java
│   │   ├── config/              # Configuraciones
│   │   │   └── RestTemplateConfig.java
│   │   ├── domain/              # Entidades de dominio
│   │   │   └── Prediction.java
│   │   └── FlightOnTimeApplication.java  # Clase principal
│   └── resources/
│       ├── application.properties
│       └── catalog/             # Catálogos de datos
│           ├── airlines.csv
│           └── airports.csv
└── test/
    └── java/com/flightontime/backend/
        └── validation/
            └── PredictValidatorTest.java
🔌 Endpoints Disponibles
POST /predict
Predice si un vuelo viene atrasado o no.

Request:

{
  "aerolinea": "AA",
  "origen": "JFK",
  "destino": "LAX",
  "fechaPartida": "2025-11-10T14:30:00",
  "distanciaKm": 350.0
}
Response (200 OK):

{
  "prevision": "Retrasado",
  "probabilidad": 0.78
}
Errores posibles:

400 Bad Request: Error de validación en los datos de entrada
500 Internal Server Error: Error interno del servidor
GET /predict/ping
Endpoint de salud para verificar que el servicio está funcionando.

Response (200 OK):

OK
🧪 Ejecutar Tests
Para ejecutar los tests unitarios:

# Usando Maven wrapper
.\mvnw.cmd test

# O usando Maven
mvn test
Los tests se encuentran en: src/test/java/com/flightontime/backend/validation/

📝 Validaciones Implementadas
El sistema incluye las siguientes validaciones:

Validación de Aerolínea: Verifica que el código de aerolínea exista en el catálogo catalog/airlines.csv
Validación de Formato:
Aerolínea: 2 caracteres, solo letras
Origen/Destino: 3 caracteres, solo letras (códigos IATA)
Fecha: Formato yyyy-MM-dd HH:mm:ss
Distancia: Número positivo con máximo 7 dígitos enteros y 2 decimales
⚙️ Configuración
application.properties
El archivo de configuración se encuentra en src/main/resources/application.properties:

spring.application.name=FlightOnTime
Configuración de API de Data Science (Opcional)
Si deseas conectar con un modelo de Data Science externo, configura la URL en application.properties:

datascience.api.url=aun por definir
Si no se configura, el sistema devolverá una respuesta mock por defecto.

🔍 Catálogos de Datos
El proyecto incluye catálogos en formato CSV en src/main/resources/catalog/:

airlines.csv: Lista de códigos de aerolíneas válidas
airports.csv: Lista de códigos de aeropuertos válidos
Estos archivos son utilizados por el validador para verificar que los datos de entrada sean correctos.

📚 Notas Adicionales
Lombok: Asegúrate de tener habilitado el procesamiento de anotaciones en tu IDE para que Lombok funcione correctamente.
Puerto: Por defecto la aplicación corre en el puerto 8080. Puedes cambiarlo en application.properties con server.port=8081
👥 Contribuidores
Proyecto desarrollado para el hackathon FlightOnTime.

📄 Licencia
[Especificar licencia si aplica]

# Funcionalidades opcionales

Endpoint GET /stats: devuelve estadísticas agregadas (ej.: % de vuelos retrasados en el día).
Persistencia: guardar historial de predicciones y peticiones en una base de datos (H2/PostgreSQL).
Dashboard visual (Streamlit/HTML): muestra, en tiempo real, la tasa de retrasos prevista.
Integración con API externa de clima: añadir condiciones meteorológicas como feature del modelo.
Batch prediction: aceptar un archivo CSV con varios vuelos y devolver las predicciones en lote.
Explicabilidad: devolver las variables más importantes en la decisión (ej.: "Hora de la tarde y aeropuerto GIG aumentan el riesgo").
Contenerización: ejecutar el sistema completo con Docker/Docker Compose.
Pruebas automatizadas: unitarias y de integración simples.

