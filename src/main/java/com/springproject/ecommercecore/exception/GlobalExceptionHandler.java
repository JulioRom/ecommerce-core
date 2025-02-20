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
    public ResponseEntity<Map<String, Object>> manejarValidaciones(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage()));

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Error en la validación de datos");
        response.put("errores", errores);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(RecursoExistenteException.class)
    public ResponseEntity<Map<String, Object>> handleRecursoExistenteException(RecursoExistenteException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", ex.getMessage()); // Mensaje de error
        response.put("error", "Conflict"); // Tipo de error
        response.put("status", HttpStatus.CONFLICT.value()); // Código HTTP 409

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

}