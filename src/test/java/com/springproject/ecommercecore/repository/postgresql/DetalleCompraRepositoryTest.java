package com.springproject.ecommercecore.repository.postgresql;

import com.springproject.ecommercecore.model.postgresql.DetalleCompra;
import com.springproject.ecommercecore.model.postgresql.OrdenCompra;
import com.springproject.ecommercecore.model.postgresql.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Date;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class DetalleCompraRepositoryTest {

    @Autowired
    private DetalleCompraRepository detalleCompraRepository;

    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

    @Autowired
    private ProductoRepository productoRepository;

    private OrdenCompra ordenEjemplo;
    private Producto productoEjemplo;
    private DetalleCompra detalleEjemplo;

    @BeforeEach
    void setUp() {
        // Crear orden de compra
        ordenEjemplo = new OrdenCompra();
        ordenEjemplo.setFechaEmision(new Date());
        ordenEjemplo.setFechaEntrega(new Date());
        ordenEjemplo.setFechaSolicitada(new Date());
        ordenEjemplo = ordenCompraRepository.save(ordenEjemplo);

        // Crear producto
        productoEjemplo = new Producto();
        productoEjemplo.setCodigoProducto("PROD001");
        productoEjemplo.setPrecioUnitario(1000);
        productoEjemplo.setStock(10);
        productoEjemplo = productoRepository.save(productoEjemplo);

        // Crear detalle de compra
        detalleEjemplo = new DetalleCompra(null, productoEjemplo, 2, 2000, ordenEjemplo);
        detalleEjemplo = detalleCompraRepository.save(detalleEjemplo);
    }

    @Test
    void testGuardarDetalleCompra() {
        assertThat(detalleEjemplo).isNotNull();
        assertThat(detalleEjemplo.getId()).isNotNull();
        assertThat(detalleEjemplo.getOrdenCompra()).isEqualTo(ordenEjemplo);
        assertThat(detalleEjemplo.getProducto()).isEqualTo(productoEjemplo);
    }

    @Test
    void testBuscarDetalleCompra_PorId() {
        DetalleCompra encontrado = detalleCompraRepository.findById(detalleEjemplo.getId()).orElse(null);
        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getId()).isEqualTo(detalleEjemplo.getId());
    }

    @Test
    void testEliminarDetalleCompra() {
        detalleCompraRepository.delete(detalleEjemplo);
        DetalleCompra eliminado = detalleCompraRepository.findById(detalleEjemplo.getId()).orElse(null);
        assertThat(eliminado).isNull();
    }
}
