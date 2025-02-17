package com.springproject.ecommercecore.service;

import com.springproject.ecommercecore.dataaccess.OrdenCompraDataAccess;
import com.springproject.ecommercecore.dataaccess.UsuarioDataAccess;
import com.springproject.ecommercecore.exception.RecursoNoEncontradoException;
import com.springproject.ecommercecore.model.postgresql.OrdenCompra;
import com.springproject.ecommercecore.model.postgresql.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompraService {

    private final OrdenCompraDataAccess ordenCompraDataAccess;
    private final UsuarioDataAccess usuarioDataAccess;

    /**
     * Generar una nueva orden de compra.
     */
    @Transactional
    public OrdenCompra generarOrden(Integer usuarioId, LocalDateTime fechaSolicitada) {
        Usuario usuario = usuarioDataAccess.buscarPorId(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        OrdenCompra orden = new OrdenCompra(usuario, fechaSolicitada);
        return ordenCompraDataAccess.guardarOrden(orden);
    }

    /**
     * Actualizar el estado de una orden de compra.
     */
    @Transactional
    public OrdenCompra actualizarEstadoOrden(Integer ordenId, OrdenCompra.EstadoOrden nuevoEstado) {
        OrdenCompra orden = ordenCompraDataAccess.buscarPorId(ordenId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Orden de compra no encontrada"));

        orden.setEstado(nuevoEstado);
        if (nuevoEstado == OrdenCompra.EstadoOrden.ENTREGADO) {
            orden.setFechaEntrega(LocalDateTime.now());
        }

        return ordenCompraDataAccess.guardarOrden(orden);
    }

    /**
     * Cancelar una orden de compra.
     */
    @Transactional
    public void cancelarOrden(Integer ordenId) {
        ordenCompraDataAccess.cancelarOrden(ordenId);
    }

    /**
     * Obtener todas las órdenes de un usuario por ID.
     */
    public List<OrdenCompra> obtenerOrdenesPorUsuarioId(Long usuarioId) {
        return ordenCompraDataAccess.buscarPorUsuarioId(usuarioId);
    }

    /**
     * Obtener todas las órdenes de un usuario por username.
     */
    public List<OrdenCompra> obtenerOrdenesPorUsername(String username) {
        return ordenCompraDataAccess.buscarPorUsername(username);
    }

    /**
     * Obtener una orden de compra por ID.
     */
    public Optional<OrdenCompra> obtenerOrdenPorId(Integer ordenId) {
        return ordenCompraDataAccess.buscarPorId(ordenId);
    }
}
