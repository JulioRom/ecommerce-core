package com.springproject.ecommercecore.repository.mongodb;

import com.springproject.ecommercecore.model.mongodb.CarritoCompra;
import jakarta.transaction.Transactional;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CarritoCompraRepository extends MongoRepository<CarritoCompra, String> {
    Optional<CarritoCompra> findByIdUsuario(String idUsuario);

    @Transactional
    void deleteByIdUsuario(String idUsuario);
}
