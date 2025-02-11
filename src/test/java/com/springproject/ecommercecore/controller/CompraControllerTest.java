package com.springproject.ecommercecore.controller;

import com.springproject.ecommercecore.service.CompraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompraControllerTest {

    @InjectMocks
    private CompraController compraController;

    @Mock
    private CompraService compraService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generarCompra_CuandoCompraEsExitosa_DeberiaRetornarMensajeExitoso() {
        // Arrange
        String idUsuario = "123";
        String mensajeEsperado = "Compra realizada con éxito. ID de Orden: 1";

        when(compraService.generarCompra(idUsuario)).thenReturn(mensajeEsperado);

        // Act
        ResponseEntity<String> response = compraController.generarCompra(idUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(mensajeEsperado, response.getBody());

        // Verificar que el método en el servicio se haya llamado una vez con el ID correcto
        verify(compraService, times(1)).generarCompra(idUsuario);
    }

    @Test
    void generarCompra_CuandoCarritoEstaVacio_DeberiaLanzarExcepcion() {
        // Arrange
        String idUsuario = "123";
        when(compraService.generarCompra(idUsuario))
                .thenThrow(new RuntimeException("El carrito está vacío. No se puede generar la compra."));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> compraController.generarCompra(idUsuario));

        assertEquals("El carrito está vacío. No se puede generar la compra.", exception.getMessage());

        // Verificar que el servicio fue llamado una vez
        verify(compraService, times(1)).generarCompra(idUsuario);
    }
}