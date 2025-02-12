package com.springproject.ecommercecore.service;

import com.springproject.ecommercecore.model.postgresql.OrdenCompra;
import com.springproject.ecommercecore.model.postgresql.Usuario;
import com.springproject.ecommercecore.repository.postgresql.OrdenCompraRepository;
import com.springproject.ecommercecore.repository.postgresql.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompraService {

    private final OrdenCompraRepository ordenCompraRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * 🔹 Generar una nueva orden de compra
     */
    @Transactional
    public OrdenCompra generarCompra(Integer idUsuario, LocalDateTime fechaSolicitada) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        OrdenCompra orden = new OrdenCompra();
        orden.setUsuario(usuario);
        orden.setFechaEmision(LocalDateTime.now()); // 🔹 Usamos LocalDateTime en lugar de Date
        orden.setFechaSolicitada(fechaSolicitada);
        orden.setEstado(OrdenCompra.EstadoOrden.PENDIENTE);

        return ordenCompraRepository.save(orden);
    }

    /**
     * 🔹 Obtener todas las órdenes de un usuario
     */
    public List<OrdenCompra> obtenerOrdenesPorUsuario(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ordenCompraRepository.findByUsuario(usuario);
    }

    /**
     * 🔹 Obtener una orden específica por ID
     */
    public OrdenCompra obtenerOrdenPorId(Integer idOrden) {
        return ordenCompraRepository.findById(idOrden)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
    }

    /**
     * 🔹 Actualizar el estado de una orden
     */
    @Transactional
    public OrdenCompra actualizarEstadoOrden(Integer idOrden, OrdenCompra.EstadoOrden nuevoEstado) {
        OrdenCompra orden = ordenCompraRepository.findById(idOrden)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        orden.setEstado(nuevoEstado);
        if (nuevoEstado == OrdenCompra.EstadoOrden.ENTREGADO) {
            orden.setFechaEntrega(LocalDateTime.now()); // Marca la entrega con la fecha actual
        }

        return ordenCompraRepository.save(orden);
    }

    /**
     * 🔹 Cancelar una orden de compra
     */
    @Transactional
    public void cancelarOrden(Integer idOrden) {
        OrdenCompra orden = ordenCompraRepository.findById(idOrden)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        orden.setEstado(OrdenCompra.EstadoOrden.CANCELADO);
        ordenCompraRepository.save(orden);
    }
}
