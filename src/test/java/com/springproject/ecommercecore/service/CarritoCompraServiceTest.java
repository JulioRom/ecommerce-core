package com.springproject.ecommercecore.service;

import com.springproject.ecommercecore.exception.RecursoNoEncontradoException;
import com.springproject.ecommercecore.model.mongodb.CarritoCompra;
import com.springproject.ecommercecore.model.mongodb.ProductoCarrito;
import com.springproject.ecommercecore.repository.mongodb.CarritoCompraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarritoCompraServiceTest {

    @Mock
    private CarritoCompraRepository carritoCompraRepository;

    @InjectMocks
    private CarritoCompraService carritoCompraService;

    private CarritoCompra carritoEjemplo;
    private ProductoCarrito productoEjemplo;

    @BeforeEach
    void setUp() {
        productoEjemplo = new ProductoCarrito("PROD001", 2,3000);
        carritoEjemplo = new CarritoCompra(null,"usuario123", new java.util.ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void testAgregarProducto_NuevoCarrito() {
        when(carritoCompraRepository.findById("usuario123")).thenReturn(Optional.empty());
        when(carritoCompraRepository.save(any(CarritoCompra.class))).thenReturn(carritoEjemplo);

        carritoCompraService.agregarProducto("usuario123", productoEjemplo);

        verify(carritoCompraRepository, times(1)).findById("usuario123");
        verify(carritoCompraRepository, times(1)).save(any(CarritoCompra.class));
    }

    @Test
    void testAgregarProducto_CarritoExistente_ProductoNuevo() {
        carritoEjemplo.getProductos().add(new ProductoCarrito("PROD002", 1, 3000));

        when(carritoCompraRepository.findById("usuario123")).thenReturn(Optional.of(carritoEjemplo));
        when(carritoCompraRepository.save(any(CarritoCompra.class))).thenReturn(carritoEjemplo);

        carritoCompraService.agregarProducto("usuario123", productoEjemplo);

        assertThat(carritoEjemplo.getProductos()).hasSize(2);
        assertThat(carritoEjemplo.getProductos().get(1).getCodigoProducto()).isEqualTo("PROD001");

        verify(carritoCompraRepository, times(1)).findById("usuario123");
        verify(carritoCompraRepository, times(1)).save(any(CarritoCompra.class));
    }

    @Test
    void testAgregarProducto_CarritoExistente_ProductoYaExiste() {
        carritoEjemplo.getProductos().add(new ProductoCarrito("PROD001", 3, 3000));

        when(carritoCompraRepository.findById("usuario123")).thenReturn(Optional.of(carritoEjemplo));
        when(carritoCompraRepository.save(any(CarritoCompra.class))).thenReturn(carritoEjemplo);

        carritoCompraService.agregarProducto("usuario123", productoEjemplo);

        assertThat(carritoEjemplo.getProductos().get(0).getCantidad()).isEqualTo(5); // 3 + 2

        verify(carritoCompraRepository, times(1)).findById("usuario123");
        verify(carritoCompraRepository, times(1)).save(any(CarritoCompra.class));
    }

    @Test
    void testObtenerCarrito_Existe() {
        when(carritoCompraRepository.findById("usuario123")).thenReturn(Optional.of(carritoEjemplo));

        Optional<CarritoCompra> resultado = Optional.ofNullable(carritoCompraService.obtenerCarrito("usuario123"));

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getIdUsuario()).isEqualTo("usuario123");

        verify(carritoCompraRepository, times(1)).findById("usuario123");
    }


    @Test
    void testObtenerCarrito_NoExiste() {
        when(carritoCompraRepository.findById("usuario123")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> carritoCompraService.obtenerCarrito("usuario123"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Carrito de compras no encontrado para usuario");

        verify(carritoCompraRepository, times(1)).findById("usuario123");
    }

    @Test
    void testEliminarProducto_ProductoExiste() {
        carritoEjemplo.getProductos().add(productoEjemplo);

        when(carritoCompraRepository.findById("usuario123")).thenReturn(Optional.of(carritoEjemplo));

        carritoCompraService.eliminarProducto("usuario123", "PROD001");

        assertThat(carritoEjemplo.getProductos()).isEmpty();

        verify(carritoCompraRepository, times(1)).findById("usuario123");
        verify(carritoCompraRepository, times(1)).save(any(CarritoCompra.class));
    }

    @Test
    void testEliminarProducto_ProductoNoExiste() {
        when(carritoCompraRepository.findById("usuario123"))
                .thenThrow(new RecursoNoEncontradoException("Producto no encontrado en el carrito del usuario: usuario123"));

        assertThatThrownBy(() -> carritoCompraService.eliminarProducto("usuario123", "PROD001"))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("Producto no encontrado en el carrito del usuario: usuario123");

        verify(carritoCompraRepository, times(1)).findById("usuario123");
    }

    @Test
    void testEliminarProducto_CarritoNoExiste() {
        when(carritoCompraRepository.findById("usuario123"))
                .thenThrow(new RecursoNoEncontradoException("Carrito de compras no encontrado para usuario: usuario123"));

        assertThatThrownBy(() -> carritoCompraService.eliminarProducto("usuario123", "PROD001"))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("Carrito de compras no encontrado para usuario: usuario123");

        verify(carritoCompraRepository, times(1)).findById("usuario123");
    }

    @Test
    void testVaciarCarrito_Existe() {
        when(carritoCompraRepository.existsById("usuario123")).thenReturn(true);
        doNothing().when(carritoCompraRepository).deleteById("usuario123");

        carritoCompraService.vaciarCarrito("usuario123");

        verify(carritoCompraRepository, times(1)).existsById("usuario123");
        verify(carritoCompraRepository, times(1)).deleteById("usuario123");
    }

    @Test
    void testVaciarCarrito_NoExiste() {
        when(carritoCompraRepository.existsById("usuario123")).thenReturn(false);

        assertThatThrownBy(() -> carritoCompraService.vaciarCarrito("usuario123"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Carrito de compras no encontrado para usuario");

        verify(carritoCompraRepository, times(1)).existsById("usuario123");
    }
}
