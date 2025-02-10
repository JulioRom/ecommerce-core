package com.springproject.ecommercecore.service;

import com.springproject.ecommercecore.exception.RecursoNoEncontradoException;
import com.springproject.ecommercecore.model.postgresql.Producto;
import com.springproject.ecommercecore.repository.postgresql.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto productoEjemplo;

    @BeforeEach
    void setUp() {
        productoEjemplo = new Producto(1, "PROD001", 1000, 10);
    }

    @Test
    void testListarProductos() {
        when(productoRepository.findAll()).thenReturn(List.of(productoEjemplo));

        List<Producto> productos = productoService.listarProductos();

        assertThat(productos).isNotEmpty();
        assertThat(productos.size()).isEqualTo(1);
        assertThat(productos.get(0).getCodigoProducto()).isEqualTo("PROD001");

        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorCodigoProducto_Encontrado() {
        when(productoRepository.findByCodigoProducto("PROD001")).thenReturn(productoEjemplo);

        Producto producto = productoService.buscarPorCodigo("PROD001");

        assertThat(producto).isNotNull();
        assertThat(producto.getCodigoProducto()).isEqualTo("PROD001");

        verify(productoRepository, times(1)).findByCodigoProducto("PROD001");
    }

    @Test
    void testBuscarPorCodigoProducto_NoEncontrado() {
        when(productoRepository.findByCodigoProducto("PROD002")).thenReturn(null);

        assertThatThrownBy(() -> productoService.buscarPorCodigo("PROD002"))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("Producto no encontrado");

        verify(productoRepository, times(1)).findByCodigoProducto("PROD002");
    }

    @Test
    void testActualizarStock() {
        when(productoRepository.findByCodigoProducto("PROD001")).thenReturn(productoEjemplo);
        when(productoRepository.save(any(Producto.class))).thenReturn(productoEjemplo);

        boolean resultado = productoService.actualizarStock("PROD001", 2);

        assertThat(resultado).isTrue();
        assertThat(productoEjemplo.getStock()).isEqualTo(8); // 10 - 2 = 8

        verify(productoRepository, times(1)).findByCodigoProducto("PROD001");
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void testActualizarStock_ProductoNoEncontrado() {
        when(productoRepository.findByCodigoProducto("PROD002")).thenReturn(null);

        boolean resultado = productoService.actualizarStock("PROD002", 2);

        assertThat(resultado).isFalse();

        verify(productoRepository, times(1)).findByCodigoProducto("PROD002");
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void testGuardarProducto() {
        when(productoRepository.save(any(Producto.class))).thenReturn(productoEjemplo);

        Producto productoGuardado = productoService.guardarProducto(productoEjemplo);

        assertThat(productoGuardado).isNotNull();
        assertThat(productoGuardado.getCodigoProducto()).isEqualTo("PROD001");

        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void testActualizarProducto_Encontrado() {
        Producto productoActualizado = new Producto(1, "PROD001", 1200, 15);

        when(productoRepository.findByCodigoProducto("PROD001")).thenReturn(productoEjemplo);
        when(productoRepository.save(any(Producto.class))).thenReturn(productoActualizado);

        Producto resultado = productoService.actualizarProducto("PROD001", productoActualizado);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getPrecioUnitario()).isEqualTo(1200);
        assertThat(resultado.getStock()).isEqualTo(15);

        verify(productoRepository, times(1)).findByCodigoProducto("PROD001");
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void testActualizarProducto_NoEncontrado() {
        when(productoRepository.findByCodigoProducto("PROD002")).thenReturn(null);

        assertThatThrownBy(() -> productoService.actualizarProducto("PROD002", new Producto()))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("Producto no encontrado");

        verify(productoRepository, times(1)).findByCodigoProducto("PROD002");
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void testEliminarProducto_Encontrado() {
        when(productoRepository.findByCodigoProducto("PROD001")).thenReturn(productoEjemplo);
        doNothing().when(productoRepository).delete(any(Producto.class));

        productoService.eliminarProducto("PROD001");

        verify(productoRepository, times(1)).findByCodigoProducto("PROD001");
        verify(productoRepository, times(1)).delete(any(Producto.class));
    }

    @Test
    void testEliminarProducto_NoEncontrado() {
        when(productoRepository.findByCodigoProducto("PROD002")).thenReturn(null);

        assertThatThrownBy(() -> productoService.eliminarProducto("PROD002"))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("Producto no encontrado");

        verify(productoRepository, times(1)).findByCodigoProducto("PROD002");
        verify(productoRepository, never()).delete(any(Producto.class));
    }
}
