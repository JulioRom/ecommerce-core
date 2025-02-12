package com.springproject.ecommercecore.business;

import com.springproject.ecommercecore.model.postgresql.OrdenCompra;
import com.springproject.ecommercecore.model.postgresql.Usuario;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OrdenCompraManager {

    /**
     *  Generar una nueva orden de compra.
     */
    public OrdenCompra generarOrden(Usuario usuario, LocalDateTime fechaSolicitada) {
        OrdenCompra orden = new OrdenCompra();
        orden.setUsuario(usuario);
        orden.setFechaEmision(LocalDateTime.now());
        orden.setFechaSolicitada(fechaSolicitada);
        orden.setEstado(OrdenCompra.EstadoOrden.PENDIENTE);
        return orden;
    }

    /**
     *  Actualizar el estado de una orden.
     */
    public void actualizarEstadoOrden(OrdenCompra orden, OrdenCompra.EstadoOrden nuevoEstado) {
        orden.setEstado(nuevoEstado);
        if (nuevoEstado == OrdenCompra.EstadoOrden.ENTREGADO) {
            orden.setFechaEntrega(LocalDateTime.now());
        }
    }

    /**
     *  Cancelar una orden de compra.
     */
    public void cancelarOrden(OrdenCompra orden) {
        orden.setEstado(OrdenCompra.EstadoOrden.CANCELADO);
    }
}
