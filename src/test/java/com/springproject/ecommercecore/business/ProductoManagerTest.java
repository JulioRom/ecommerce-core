package com.springproject.ecommercecore.business;

import com.springproject.ecommercecore.model.postgresql.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class ProductoManagerTest {

    private ProductoManager productoManager;

    @BeforeEach
    void setUp() {
        productoManager = new ProductoManager();
    }

    @Test
    void testActualizarDatosProducto() {
        Producto producto = new Producto("P001", 1200.00, 10);
        Producto nuevosDatos = new Producto("P001", 1500.00, 5);

        productoManager.actualizarDatosProducto(producto, nuevosDatos);

        assertEquals(BigDecimal.valueOf(1500.00).setScale(2, RoundingMode.HALF_UP), producto.getPrecioUnitario());
        assertEquals(5, producto.getStock());
    }

    @Test
    void testValidarStock_Suficiente() {
        Producto producto = new Producto("P001", 1200.00, 10);
        assertDoesNotThrow(() -> productoManager.validarStock(producto, 5));
    }

    @Test
    void testValidarStock_Insuficiente() {
        Producto producto = new Producto("P001", 1200.00, 5);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> productoManager.validarStock(producto, 10));

        assertEquals("Stock insuficiente para el producto: P001", exception.getMessage());
    }

    @Test
    void testReducirStock() {
        Producto producto = new Producto("P001", 1200.00, 10);
        productoManager.reducirStock(producto, 5);

        assertEquals(5, producto.getStock());
    }
}
