package com.springproject.ecommercecore.repository.mongodb;

import com.springproject.ecommercecore.model.mongodb.CarritoCompra;
import com.springproject.ecommercecore.model.mongodb.ProductoCarrito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Testcontainers
@ExtendWith(SpringExtension.class)
class CarritoCompraRepositoryTest {

    @Autowired
    private CarritoCompraRepository carritoCompraRepository;

    private CarritoCompra carritoEjemplo;

    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:5.0");

    @BeforeEach
    void setUp() {
        carritoEjemplo = new CarritoCompra("usuario123", new java.util.ArrayList<>());
        carritoEjemplo.getProductos().add(new ProductoCarrito("PROD001", 2));

        carritoEjemplo = carritoCompraRepository.save(carritoEjemplo);
    }

    @Test
    void testGuardarCarrito() {
        assertThat(carritoEjemplo).isNotNull();
        assertThat(carritoEjemplo.getIdUsuario()).isEqualTo("usuario123");
        assertThat(carritoEjemplo.getProductos()).isNotEmpty();
    }

    @Test
    void testBuscarCarritoPorId() {
        Optional<CarritoCompra> carritoEncontrado = carritoCompraRepository.findById("usuario123");

        assertThat(carritoEncontrado).isPresent();
        assertThat(carritoEncontrado.get().getIdUsuario()).isEqualTo("usuario123");
    }

    @Test
    void testActualizarCarrito() {
        carritoEjemplo.getProductos().add(new ProductoCarrito("PROD002", 1));
        carritoCompraRepository.save(carritoEjemplo);

        Optional<CarritoCompra> carritoActualizado = carritoCompraRepository.findById("usuario123");

        assertThat(carritoActualizado).isPresent();
        assertThat(carritoActualizado.get().getProductos()).hasSize(2);
    }

    @Test
    void testEliminarCarrito() {
        carritoCompraRepository.deleteById("usuario123");

        Optional<CarritoCompra> carritoEliminado = carritoCompraRepository.findById("usuario123");

        assertThat(carritoEliminado).isEmpty();
    }
}
