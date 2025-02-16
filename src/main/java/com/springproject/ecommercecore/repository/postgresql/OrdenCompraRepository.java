package com.springproject.ecommercecore.repository.postgresql;

import com.springproject.ecommercecore.model.postgresql.OrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Integer> {
    // Alternativa: Buscar órdenes por el username del usuario
    List<OrdenCompra> findByUsuario_Username(String username);

    List<OrdenCompra> findByUsuario_Id(Long usuario_id);

    Optional<OrdenCompra> findTopByUsuario_IdOrderByFechaEmisionDesc(Integer usuarioId); // Última orden
}
