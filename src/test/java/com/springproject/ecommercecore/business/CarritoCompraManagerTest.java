package com.springproject.ecommercecore.business;

import com.springproject.ecommercecore.model.mongodb.CarritoCompra;
import com.springproject.ecommercecore.model.mongodb.ProductoCarrito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CarritoCompraManagerTest {

    private CarritoCompraManager carritoCompraManager;
    private CarritoCompra carrito;

    @BeforeEach
    void setUp() {
        carritoCompraManager = new CarritoCompraManager();
        carrito = new CarritoCompra("user123", "user123", new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void testAgregarProductoNuevo() {
        ProductoCarrito producto = new ProductoCarrito("P001", 2, 1200.00);
        carritoCompraManager.agregarProducto(carrito, producto);

        assertEquals(1, carrito.getProductos().size());
        assertEquals(2, carrito.getProductos().get(0).getCantidad());
    }

    @Test
    void testEliminarProducto() {
        ProductoCarrito producto = new ProductoCarrito("P001", 2, 1200.00);
        carrito.getProductos().add(producto);

        assertTrue(carritoCompraManager.eliminarProducto(carrito, "P001"));
        assertEquals(0, carrito.getProductos().size());
    }

    @Test
    void testCrearCarrito() {
        CarritoCompra nuevoCarrito = carritoCompraManager.crearCarrito("user456");

        assertNotNull(nuevoCarrito);
        assertEquals("user456", nuevoCarrito.getIdUsuario());
        assertEquals(0, nuevoCarrito.getProductos().size());
    }
}
