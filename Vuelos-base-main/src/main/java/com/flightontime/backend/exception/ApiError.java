package com.flightontime.backend.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ApiError(LocalDateTime timestamp, int status, String error, String message, List<String> details,
		String path) {

}
