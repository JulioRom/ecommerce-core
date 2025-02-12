package com.springproject.ecommercecore.repository.postgresql;

import com.springproject.ecommercecore.model.postgresql.OrdenCompra;
import com.springproject.ecommercecore.model.postgresql.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Integer> {
    List<OrdenCompra> findByUsuario(Usuario usuario);
}
