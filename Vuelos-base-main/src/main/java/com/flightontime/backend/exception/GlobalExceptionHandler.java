package com.flightontime.backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                "Error de validación en los datos de entrada",
                details,
                extractPath(request)
        );

        return ResponseEntity.badRequest().body(apiError);
    }

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
	        HttpMessageNotReadableException ex,
	        HttpHeaders headers,
	        HttpStatusCode status,
	        WebRequest request) {

	    ApiError apiError = new ApiError(
	            LocalDateTime.now(),
	            HttpStatus.BAD_REQUEST.value(),
	            "INVALID_REQUEST",
	            "Formato de fecha inválido. Use yyyy-MM-dd HH:mm:ss",
	            List.of("fechaPartida"),
	            extractPath(request)
	    );

	    return ResponseEntity.badRequest().body(apiError);
	}

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                "Error de validación en los datos de entrada",
                List.of(ex.getMessage()),
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleIllegalStateException(
            IllegalStateException ex,
            HttpServletRequest request) {

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_ERROR",
                "Error interno del sistema",
                List.of(ex.getMessage()),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntimeException(
            RuntimeException ex,
            HttpServletRequest request) {

        String message = ex.getMessage();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String errorCode = "INTERNAL_ERROR";

        // Verificar si es un error de disponibilidad del servicio (503)
        if (message != null && message.contains("no está disponible")) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
            errorCode = "SERVICE_UNAVAILABLE";
        }
        // Verificar si es un error de procesamiento del modelo (422)
        else if (message != null && message.contains("no pudo procesar")) {
            status = HttpStatus.valueOf(422);
            errorCode = "UNPROCESSABLE_ENTITY";
        }

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                status.value(),
                errorCode,
                message != null ? message : "Ocurrió un error inesperado",
                List.of(message != null ? message : ex.getClass().getSimpleName()),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_ERROR",
                "Ocurrió un error inesperado",
                List.of(ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName()),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    private String extractPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}


