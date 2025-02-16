package com.springproject.ecommercecore.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecursoNoEncontradoExceptionTest {

    @Test
    void deberiaRetornarMensajeCorrecto() {
        String mensaje = "Recurso no encontrado";
        RecursoNoEncontradoException exception = new RecursoNoEncontradoException(mensaje);

        assertEquals(mensaje, exception.getMessage());
    }
}
