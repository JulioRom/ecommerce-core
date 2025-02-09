package com.springproject.ecommercecore.repository.postgresql;

import com.springproject.ecommercecore.model.postgresql.DetalleCompra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Integer> {
}
