package com.springproject.ecommercecore.repository.postgresql;

import com.springproject.ecommercecore.model.postgresql.OrdenCompra;
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
class OrdenCompraRepositoryTest {

    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

    private OrdenCompra ordenEjemplo;

    @BeforeEach
    void setUp() {
        ordenEjemplo = new OrdenCompra();
        ordenEjemplo.setFechaEmision(new Date());
        ordenEjemplo.setFechaEntrega(new Date());
        ordenEjemplo.setFechaSolicitada(new Date());

        ordenEjemplo = ordenCompraRepository.save(ordenEjemplo);
    }

    @Test
    void testGuardarOrdenCompra() {
        assertThat(ordenEjemplo).isNotNull();
        assertThat(ordenEjemplo.getId()).isNotNull();
    }

    @Test
    void testBuscarOrdenCompra_PorId() {
        OrdenCompra ordenEncontrada = ordenCompraRepository.findById(ordenEjemplo.getId()).orElse(null);
        assertThat(ordenEncontrada).isNotNull();
        assertThat(ordenEncontrada.getId()).isEqualTo(ordenEjemplo.getId());
    }

    @Test
    void testEliminarOrdenCompra() {
        ordenCompraRepository.delete(ordenEjemplo);
        OrdenCompra ordenEliminada = ordenCompraRepository.findById(ordenEjemplo.getId()).orElse(null);
        assertThat(ordenEliminada).isNull();
    }
}
