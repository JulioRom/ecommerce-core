package com.springproject.ecommercecore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //  Manejo de excepción genérica
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor: " + ex.getMessage());
    }

    //  Manejo de excepciones personalizadas
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleRecursoNoEncontradoException(RecursoNoEncontradoException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", ex.getMessage()); // Mensaje de error específico
        response.put("error", "Not Found"); // Tipo de error
        response.put("status", HttpStatus.NOT_FOUND.value()); // Código HTTP

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    //  Manejo de errores de validación en DTOs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidaciones(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}