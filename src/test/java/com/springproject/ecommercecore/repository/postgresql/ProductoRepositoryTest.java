package com.springproject.ecommercecore.repository.postgresql;

import com.springproject.ecommercecore.model.postgresql.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class ProductoRepositoryTest {

    @Autowired
    private ProductoRepository productoRepository;

    private Producto productoEjemplo;

    @BeforeEach
    void setUp() {
        productoEjemplo = new Producto();
        productoEjemplo.setCodigoProducto("PROD001");
        productoEjemplo.setPrecioUnitario(BigDecimal.valueOf(1000));
        productoEjemplo.setStock(10);
        productoRepository.save(productoEjemplo);
    }

    @Test
    void testFindByCodigoProducto_Encontrado() {
        Producto producto = productoRepository.findByCodigoProducto("PROD001");
        assertThat(producto).isNotNull();
        assertThat(producto.getCodigoProducto()).isEqualTo("PROD001");
    }

    @Test
    void testFindByCodigoProducto_NoEncontrado() {
        Producto producto = productoRepository.findByCodigoProducto("PROD002");
        assertThat(producto).isNull();
    }

    @Test
    void testGuardarProducto() {
        Producto nuevoProducto = new Producto();
        nuevoProducto.setCodigoProducto("PROD002");
        nuevoProducto.setPrecioUnitario(BigDecimal.valueOf(2000));
        nuevoProducto.setStock(5);

        Producto guardado = productoRepository.save(nuevoProducto);

        assertThat(guardado).isNotNull();
        assertThat(guardado.getCodigoProducto()).isEqualTo("PROD002");
    }

    @Test
    void testEliminarProducto() {
        productoRepository.delete(productoEjemplo);
        Producto eliminado = productoRepository.findByCodigoProducto("PROD001");
        assertThat(eliminado).isNull();
    }
}
