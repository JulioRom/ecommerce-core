package com.springproject.ecommercecore.dataaccess;

import com.springproject.ecommercecore.model.postgresql.OrdenCompra;
import com.springproject.ecommercecore.repository.postgresql.OrdenCompraRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrdenCompraDataAccess {

    private final OrdenCompraRepository ordenCompraRepository;

    public OrdenCompra guardarOrden(OrdenCompra orden) {
        return ordenCompraRepository.save(orden);
    }

    public Optional<OrdenCompra> buscarPorId(Integer ordenId) {
        return ordenCompraRepository.findById(ordenId);
    }

    public List<OrdenCompra> buscarPorUsuarioId(Long usuarioId) {
        return ordenCompraRepository.findByUsuario_Id(usuarioId);
    }

    public List<OrdenCompra> buscarPorUsername(String username) {
        return ordenCompraRepository.findByUsuario_Username(username);
    }

    public Optional<OrdenCompra> buscarUltimaOrdenPorUsuarioId(Integer usuarioId) {
        return ordenCompraRepository.findTopByUsuario_IdOrderByFechaEmisionDesc(usuarioId);
    }

    @Transactional
    public void cancelarOrden(Integer ordenId) {
        Optional<OrdenCompra> orden = ordenCompraRepository.findById(ordenId);
        if (orden.isPresent()) {
            OrdenCompra ordenCancelada = orden.get();
            ordenCancelada.setEstado(OrdenCompra.EstadoOrden.CANCELADO);
            ordenCompraRepository.save(ordenCancelada);
        } else {
            throw new IllegalArgumentException("Orden de compra no encontrada");
        }
    }

}
