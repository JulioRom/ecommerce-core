package com.springproject.ecommercecore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springproject.ecommercecore.exception.GlobalExceptionHandler;
import com.springproject.ecommercecore.exception.RecursoNoEncontradoException;
import com.springproject.ecommercecore.model.mongodb.CarritoCompra;
import com.springproject.ecommercecore.model.mongodb.ProductoCarrito;
import com.springproject.ecommercecore.service.CarritoCompraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CarritoCompraControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CarritoCompraService carritoCompraService;

    @InjectMocks
    private CarritoCompraController carritoCompraController;

    private ObjectMapper objectMapper;
    private CarritoCompra carritoEjemplo;
    private ProductoCarrito productoEjemplo;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(carritoCompraController)
                .setControllerAdvice(new GlobalExceptionHandler()) // 👈 Agregar manejo global de excepciones
                .build();        objectMapper = new ObjectMapper();

        // Datos de prueba
        productoEjemplo = new ProductoCarrito("PROD001", 2);
        carritoEjemplo = new CarritoCompra("usuario123", new java.util.ArrayList<>());
    }

    @Test
    void testAgregarProductoAlCarrito() throws Exception {
        doNothing().when(carritoCompraService).agregarProducto(anyString(), any(ProductoCarrito.class));

        mockMvc.perform(post("/api/carrito/usuario123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoEjemplo)))
                .andExpect(status().isOk());

        verify(carritoCompraService, times(1)).agregarProducto(eq("usuario123"), any(ProductoCarrito.class));
    }

    @Test
    void testObtenerCarrito_Existe() throws Exception {
        when(carritoCompraService.obtenerCarrito("usuario123")).thenReturn(carritoEjemplo);

        mockMvc.perform(get("/api/carrito/usuario123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value("usuario123"));

        verify(carritoCompraService, times(1)).obtenerCarrito("usuario123");
    }

    @Test
    void testObtenerCarrito_NoExiste() throws Exception {
        doThrow(new RecursoNoEncontradoException("Carrito de compras no encontrado para usuario: usuario123"))
                .when(carritoCompraService).obtenerCarrito("usuario123");

        // Ejecutar y capturar la respuesta
        String jsonResponse = mockMvc.perform(get("/api/carrito/usuario123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) // Verifica código 404
                .andExpect(jsonPath("$.mensaje").value("Carrito de compras no encontrado para usuario: usuario123")) // Verifica JSON
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.status").value(404))
                .andReturn()
                .getResponse()
                .getContentAsString(); // Obtiene la respuesta como String

        // 🔍 Imprimir la respuesta en consola para verificar el JSON
        System.out.println("Respuesta del servidor: " + jsonResponse);

        verify(carritoCompraService, times(1)).obtenerCarrito("usuario123");
    }

    @Test
    void testEliminarProductoDelCarrito() throws Exception {
        doNothing().when(carritoCompraService).eliminarProducto("usuario123", "PROD001");

        mockMvc.perform(delete("/api/carrito/usuario123/PROD001"))
                .andExpect(status().isNoContent()); // Cambié isOk() por isNoContent()

        verify(carritoCompraService, times(1)).eliminarProducto("usuario123", "PROD001");
    }

    @Test
    void testVaciarCarrito() throws Exception {
        doNothing().when(carritoCompraService).vaciarCarrito("usuario123");

        mockMvc.perform(delete("/api/carrito/usuario123"))
                .andExpect(status().isNoContent()); // Cambié isOk() por isNoContent()

        verify(carritoCompraService, times(1)).vaciarCarrito("usuario123");
    }
}
