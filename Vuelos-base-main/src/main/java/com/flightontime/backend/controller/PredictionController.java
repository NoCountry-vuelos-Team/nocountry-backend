package com.flightontime.backend.controller;

import com.flightontime.backend.dto.request.PredictionRequest;
import com.flightontime.backend.dto.response.PredictionResponse;
import com.flightontime.backend.dto.response.PredictionHistoryItem;
import com.flightontime.backend.service.PredictionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
		name = "Predicción de Vuelos",
		description = "Endpoints para predecir retrasos de vuelos utilizando modelos de Machine Learning. Contrato definido con el equipo de Data Science."
)
@RestController
@RequestMapping("/predict")
@RequiredArgsConstructor
public class PredictionController {

	private final PredictionService predictionService;

	@Operation(
			summary = "Verificar estado del servicio",
			description = "Endpoint de healthcheck para verificar que el servicio está funcionando correctamente"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Servicio funcionando correctamente",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "OK")
					)
			)
	})
	@GetMapping("/ping")
	public ResponseEntity<String> ping() {
		return ResponseEntity.ok("OK");
	}

	@Operation(
			summary = "Predecir retraso de vuelo",
			description = """
            Recibe los datos de un vuelo y devuelve una predicción de si sufrirá retraso o no.
            
            **Contrato Backend ↔ Data Science:**
            - Los códigos de aerolínea se convierten automáticamente a mayúsculas
            - La fecha debe estar en formato 'yyyy-MM-dd HH:mm:ss'
            - La distancia acepta hasta 7 dígitos enteros y 2 decimales
            
            **Respuesta:**
            - `prevision`: "A TIEMPO" o "RETRASADO"
            - `probabilidad`: Confianza del modelo (0.0 a 1.0)
            """
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Predicción realizada exitosamente",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = PredictionResponse.class),
							examples = @ExampleObject(
									name = "Vuelo predicho a tiempo",
									value = """
                        {
                          "prevision": "A TIEMPO",
                          "probabilidad": 0.85
                        }
                        """
							)
					)
			),
			@ApiResponse(
					responseCode = "400",
					description = "Error de validación en los datos de entrada",
					content = @Content(
							mediaType = "application/json",
							examples = {
									@ExampleObject(
											name = "Aerolínea inválida",
											value = """
                            {
                              "timestamp": "2024-01-15T10:30:00",
                              "status": 400,
                              "error": "Bad Request",
                              "message": "La aerolínea debe tener exactamente 2 caracteres",
                              "path": "/predict"
                            }
                            """
									),
									@ExampleObject(
											name = "Formato de fecha inválido",
											value = """
                            {
                              "timestamp": "2024-01-15T10:30:00",
                              "status": 400,
                              "error": "Bad Request",
                              "message": "Formato de fecha inválido. Use yyyy-MM-dd HH:mm:ss",
                              "path": "/predict"
                            }
                            """
									),
									@ExampleObject(
											name = "Campo obligatorio faltante",
											value = """
                            {
                              "timestamp": "2024-01-15T10:30:00",
                              "status": 400,
                              "error": "Bad Request",
                              "message": "La fecha de partida es obligatoria",
                              "path": "/predict"
                            }
                            """
									),
									@ExampleObject(
											name = "Distancia negativa",
											value = """
                            {
                              "timestamp": "2024-01-15T10:30:00",
                              "status": 400,
                              "error": "Bad Request",
                              "message": "La distancia debe ser mayor a 0",
                              "path": "/predict"
                            }
                            """
									)
							}
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Error interno del servidor",
					content = @Content(
							mediaType = "application/json",
							examples = @ExampleObject(
									value = """
                        {
                          "timestamp": "2024-01-15T10:30:00",
                          "status": 500,
                          "error": "Internal Server Error",
                          "message": "Error al procesar la predicción",
                          "path": "/predict"
                        }
                        """
							)
					)
			)
	})
	@PostMapping
	public ResponseEntity<PredictionResponse> predict(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = "Datos del vuelo para realizar la predicción",
					required = true,
					content = @Content(
							schema = @Schema(implementation = PredictionRequest.class),
							examples = @ExampleObject(
									name = "Ejemplo de vuelo válido",
									value = """
                            {
                              "aerolinea": "AA",
                              "origen": "SFO",
                              "destino": "LAX",
                              "fechaPartida": "2024-01-15 14:30:00",
                              "distanciaKm": 559.23
                            }
                            """
							)
					)
			)
			@RequestBody @Valid PredictionRequest request
	) {
		PredictionResponse response = predictionService.predict(request);
		return ResponseEntity.ok(response);
	}

	@Operation(
			summary = "Obtener historial de predicciones",
			description = "Retorna el historial completo de todas las predicciones realizadas, ordenado por fecha de creación descendente (más recientes primero)"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Historial obtenido exitosamente",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = PredictionHistoryItem.class),
							examples = @ExampleObject(
									name = "Historial con múltiples predicciones",
									value = """
                            [
                              {
                                "id": 1,
                                "aerolinea": "AA",
                                "origen": "SFO",
                                "destino": "LAX",
                                "fechaPartida": "2024-01-15T14:30:00",
                                "distanciaKm": 559,
                                "prevision": "A TIEMPO",
                                "probabilidad": 0.85,
                                "createdAt": "2024-01-15T10:00:00",
                                "updatedAt": "2024-01-15T10:00:00"
                              },
                              {
                                "id": 2,
                                "aerolinea": "UA",
                                "origen": "JFK",
                                "destino": "LAX",
                                "fechaPartida": "2024-01-16T08:00:00",
                                "distanciaKm": 2475,
                                "prevision": "RETRASADO",
                                "probabilidad": 0.72,
                                "createdAt": "2024-01-15T09:30:00",
                                "updatedAt": "2024-01-15T09:30:00"
                              }
                            ]
                            """
							)
					)
			),
			@ApiResponse(
					responseCode = "200",
					description = "Historial vacío (sin predicciones aún)",
					content = @Content(
							mediaType = "application/json",
							examples = @ExampleObject(
									name = "Historial vacío",
									value = "[]"
							)
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Error interno del servidor",
					content = @Content(
							mediaType = "application/json",
							examples = @ExampleObject(
									value = """
                        {
                          "timestamp": "2024-01-15T10:30:00",
                          "status": 500,
                          "error": "Internal Server Error",
                          "message": "Error al obtener el historial de predicciones",
                          "path": "/predict/history"
                        }
                        """
							)
					)
			)
	})
	@GetMapping("/history")
	public ResponseEntity<List<PredictionHistoryItem>> getHistory() {
		List<PredictionHistoryItem> history = predictionService.findAllHistory();
		return ResponseEntity.ok(history);
	}
}
