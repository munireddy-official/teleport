package com.teleport.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class GlobalExceptionHandler {
    @ExceptionHandler(TrackingNumberGenerationException.class)
    public ResponseEntity<Map<String, Object>> handleTrackingException(TrackingNumberGenerationException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", ex.getClass().getSimpleName());
        body.put("message", ex.getMessage());
        body.put("timestamp", OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "ValidationError");
        body.put("message", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        body.put("timestamp", OffsetDateTime.now());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "InternalServerError");
        body.put("message", ex.getMessage());
        body.put("timestamp", OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

}
