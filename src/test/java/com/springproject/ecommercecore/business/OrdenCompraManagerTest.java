package com.springproject.ecommercecore.business;

import com.springproject.ecommercecore.model.postgresql.OrdenCompra;
import com.springproject.ecommercecore.model.postgresql.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OrdenCompraManagerTest {

    private OrdenCompraManager ordenCompraManager;

    @BeforeEach
    void setUp() {
        ordenCompraManager = new OrdenCompraManager();
    }

    @Test
    void testGenerarOrden() {
        Usuario usuario = new Usuario();
        LocalDateTime fechaSolicitada = LocalDateTime.now().plusDays(3);

        OrdenCompra orden = ordenCompraManager.generarOrden(usuario, fechaSolicitada);

        assertNotNull(orden);
        assertEquals(OrdenCompra.EstadoOrden.PENDIENTE, orden.getEstado());
    }

    @Test
    void testActualizarEstadoOrden() {
        OrdenCompra orden = new OrdenCompra();
        ordenCompraManager.actualizarEstadoOrden(orden, OrdenCompra.EstadoOrden.ENTREGADO);

        assertEquals(OrdenCompra.EstadoOrden.ENTREGADO, orden.getEstado());
    }
}
