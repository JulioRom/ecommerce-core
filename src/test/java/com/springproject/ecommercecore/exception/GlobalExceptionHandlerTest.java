package com.springproject.ecommercecore.exception;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void manejarRecursoNoEncontrado_DeberiaRetornar404() {
        RecursoNoEncontradoException exception = new RecursoNoEncontradoException("Producto no encontrado");

        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleRecursoNoEncontradoException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Producto no encontrado", response.getBody().get("mensaje"));
    }

    @Test
    void manejarValidaciones_DeberiaRetornar400() {
        // Simular un error de validación con un mensaje específico
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(
                List.of(new FieldError("registerRequest", "email", "El email no es válido"))
        );

        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.manejarValidaciones(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("errores"));

        // Obtener el mapa de errores correctamente
        @SuppressWarnings("unchecked")
        Map<String, String> errores = (Map<String, String>) response.getBody().get("errores");

        assertNotNull(errores);
        assertEquals("El email no es válido", errores.get("email"));
    }

    @Test
    void manejarExcepcionGenerica_DeberiaRetornar500() {
        Exception exception = new Exception("Error inesperado");

        ResponseEntity<String> response = globalExceptionHandler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error interno del servidor: Error inesperado", response.getBody());
    }
}
