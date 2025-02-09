package com.springproject.ecommercecore.repository.mongodb;

import com.springproject.ecommercecore.model.mongodb.CarritoCompra;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CarritoCompraRepository extends MongoRepository<CarritoCompra, String> {
}
