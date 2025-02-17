package com.springproject.ecommercecore.service;

import com.springproject.ecommercecore.dataaccess.CarritoCompraDataAccess;
import com.springproject.ecommercecore.model.mongodb.CarritoCompra;
import com.springproject.ecommercecore.model.mongodb.ProductoCarrito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarritoCompraServiceTest {

    @Mock
    private CarritoCompraDataAccess carritoCompraDataAccess;

    @InjectMocks
    private CarritoCompraService carritoCompraService;

    private CarritoCompra carrito;

    @BeforeEach
    void setUp() {
        ProductoCarrito productoCarrito = new ProductoCarrito("P001", 1, 2000.0);
        carrito = new CarritoCompra("1", "usuario123", List.of(productoCarrito), LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void agregarProducto_DeberiaAgregarProductoSiCarritoExiste() {
        carrito.setProductos(new ArrayList<>());
        when(carritoCompraDataAccess.buscarPorIdOUsuario("usuario123")).thenReturn(Optional.of(carrito));

        carritoCompraService.agregarProducto("usuario123", new ProductoCarrito("P001", 2, 2000.0));

        verify(carritoCompraDataAccess).guardarCarrito(any(CarritoCompra.class));
    }


    @Test
    void agregarProducto_DeberiaCrearNuevoCarritoSiNoExiste() {
        when(carritoCompraDataAccess.buscarPorIdOUsuario("usuario123")).thenReturn(Optional.empty());
        when(carritoCompraDataAccess.guardarCarrito(any(CarritoCompra.class))).thenAnswer(invocation -> invocation.getArgument(0));

        carritoCompraService.agregarProducto("usuario123", new ProductoCarrito("P002", 1, 2000.0));

        verify(carritoCompraDataAccess, times(2)).guardarCarrito(any(CarritoCompra.class));
    }


    @Test
    void removerProductoDelCarrito_DeberiaEliminarProductoSiEstaEnCarrito() {
        when(carritoCompraDataAccess.buscarPorIdOUsuario("usuario123")).thenReturn(Optional.of(carrito));

        boolean resultado = carritoCompraService.removerProductoDelCarrito("usuario123", "P001");

        assertTrue(resultado);
        verify(carritoCompraDataAccess).guardarCarrito(any(CarritoCompra.class));
    }

    @Test
    void removerProductoDelCarrito_DeberiaRetornarFalseSiProductoNoEstaEnCarrito() {
        when(carritoCompraDataAccess.buscarPorIdOUsuario("usuario123")).thenReturn(Optional.of(carrito));

        boolean resultado = carritoCompraService.removerProductoDelCarrito("usuario123", "P002");

        assertFalse(resultado);
    }

    @Test
    void obtenerCarritoPorIdOUsuario_DeberiaRetornarCarritoSiExiste() {
        when(carritoCompraDataAccess.buscarPorIdOUsuario("usuario123")).thenReturn(Optional.of(carrito));

        Optional<CarritoCompra> resultado = carritoCompraService.obtenerCarritoPorIdOUsuario("usuario123");

        assertTrue(resultado.isPresent());
        assertEquals("usuario123", resultado.get().getIdUsuario());
    }

    @Test
    void obtenerCarritoPorIdOUsuario_DeberiaRetornarOptionalVacioSiNoExiste() {
        when(carritoCompraDataAccess.buscarPorIdOUsuario("usuario123")).thenReturn(Optional.empty());

        Optional<CarritoCompra> resultado = carritoCompraService.obtenerCarritoPorIdOUsuario("usuario123");

        assertFalse(resultado.isPresent());
    }

    @Test
    void vaciarCarrito_DeberiaEliminarCarritoSiExiste() {
        when(carritoCompraDataAccess.buscarPorIdOUsuario("usuario123")).thenReturn(Optional.of(carrito));
        doNothing().when(carritoCompraDataAccess).eliminarCarritoPorUsuarioId("usuario123");

        boolean resultado = carritoCompraService.vaciarCarrito("usuario123");

        assertTrue(resultado);
        verify(carritoCompraDataAccess).eliminarCarritoPorUsuarioId("usuario123");
    }

    @Test
    void vaciarCarrito_DeberiaRetornarTrueSiCarritoNoExiste() {
        lenient().when(carritoCompraDataAccess.buscarPorIdOUsuario("usuario123")).thenReturn(Optional.empty());

        boolean resultado = carritoCompraService.vaciarCarrito("usuario123");

        assertFalse(resultado);
    }

}
