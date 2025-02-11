package com.springproject.ecommercecore.service;

import com.springproject.ecommercecore.exception.RecursoNoEncontradoException;
import com.springproject.ecommercecore.model.mongodb.CarritoCompra;
import com.springproject.ecommercecore.model.mongodb.ProductoCarrito;
import com.springproject.ecommercecore.model.postgresql.DetalleCompra;
import com.springproject.ecommercecore.model.postgresql.OrdenCompra;
import com.springproject.ecommercecore.model.postgresql.Producto;
import com.springproject.ecommercecore.repository.postgresql.DetalleCompraRepository;
import com.springproject.ecommercecore.repository.postgresql.OrdenCompraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Date;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompraServiceTest {

    @Mock
    private CarritoCompraService carritoCompraService;

    @Mock
    private ProductoService productoService;

    @Mock
    private OrdenCompraRepository ordenCompraRepository;

    @Mock
    private DetalleCompraRepository detalleCompraRepository;

    @InjectMocks
    private CompraService compraService;
    private CarritoCompra carritoEjemplo;
    private Producto productoEjemplo;
    private OrdenCompra ordenCompraEjemplo;

    @BeforeEach
    void setUp() {
        // Crear un producto en el carrito
        ProductoCarrito productoCarritoEjemplo = new ProductoCarrito("PROD001", 2);

        // Crear un carrito con productos
        carritoEjemplo = new CarritoCompra("usuario123", new ArrayList<>());
        carritoEjemplo.getProductos().add(productoCarritoEjemplo);

        // Crear un producto en la base de datos
        productoEjemplo = new Producto(1, "PROD001", 1000, 10);

        // Crear una orden de compra
        ordenCompraEjemplo = new OrdenCompra();
        ordenCompraEjemplo.setId(1);
        ordenCompraEjemplo.setFechaEmision(new Date());
        ordenCompraEjemplo.setFechaEntrega(new Date());
        ordenCompraEjemplo.setFechaSolicitada(new Date());
    }

    @Test
    void testGenerarCompra_Exitoso() {
        when(carritoCompraService.obtenerCarrito("usuario123")).thenReturn(carritoEjemplo);
        when(productoService.buscarPorCodigo("PROD001")).thenReturn(productoEjemplo);
        when(ordenCompraRepository.save(any(OrdenCompra.class))).thenReturn(ordenCompraEjemplo);
        when(detalleCompraRepository.save(any(DetalleCompra.class))).thenReturn(new DetalleCompra());

        String resultado = compraService.generarCompra("usuario123");

        assertThat(resultado).contains("Compra realizada con éxito");

        verify(productoService, times(1)).actualizarStock("PROD001", 2);
        verify(ordenCompraRepository, times(1)).save(any(OrdenCompra.class));
        verify(detalleCompraRepository, times(1)).save(any(DetalleCompra.class));
        verify(carritoCompraService, times(1)).vaciarCarrito("usuario123");
    }

    @Test
    void testGenerarCompra_CarritoNoExiste() {
        when(carritoCompraService.obtenerCarrito("usuario123"))
                .thenThrow(new RecursoNoEncontradoException("El carrito de compras no existe."));

        assertThatThrownBy(() -> compraService.generarCompra("usuario123"))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("El carrito de compras no existe.");

        verify(carritoCompraService, times(1)).obtenerCarrito("usuario123");
        verifyNoInteractions(productoService, ordenCompraRepository, detalleCompraRepository);
    }

    @Test
    void testGenerarCompra_CarritoVacio() {
        // Simular un carrito vacío
        CarritoCompra carritoVacio = new CarritoCompra("usuario123", new ArrayList<>());

        when(carritoCompraService.obtenerCarrito("usuario123")).thenReturn(carritoVacio);

        assertThatThrownBy(() -> compraService.generarCompra("usuario123"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("El carrito está vacío. No se puede generar la compra.");

        verify(carritoCompraService, times(1)).obtenerCarrito("usuario123");
        verifyNoInteractions(productoService, ordenCompraRepository, detalleCompraRepository);
    }

    @Test
    void testGenerarCompra_ProductoNoExiste() {
        when(carritoCompraService.obtenerCarrito("usuario123")).thenReturn(carritoEjemplo);
        when(productoService.buscarPorCodigo("PROD001"))
                .thenThrow(new RecursoNoEncontradoException("Producto no encontrado: PROD001"));

        assertThatThrownBy(() -> compraService.generarCompra("usuario123"))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("Producto no encontrado: PROD001");

        verify(carritoCompraService, times(1)).obtenerCarrito("usuario123");
        verify(productoService, times(1)).buscarPorCodigo("PROD001");
        verifyNoInteractions(ordenCompraRepository, detalleCompraRepository);
    }

    @Test
    void testGenerarCompra_StockInsuficiente() {
        productoEjemplo.setStock(1); // Stock menor a la cantidad requerida
        when(carritoCompraService.obtenerCarrito("usuario123")).thenReturn(carritoEjemplo);
        when(productoService.buscarPorCodigo("PROD001")).thenReturn(productoEjemplo);

        assertThatThrownBy(() -> compraService.generarCompra("usuario123"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Stock insuficiente para el producto: PROD001");

        verify(carritoCompraService, times(1)).obtenerCarrito("usuario123");
        verify(productoService, times(1)).buscarPorCodigo("PROD001");
        verifyNoInteractions(ordenCompraRepository, detalleCompraRepository);
    }

    @Test
    void testGenerarCompra_VaciaCarritoDespuesDeCompra() {
        when(carritoCompraService.obtenerCarrito("usuario123")).thenReturn(carritoEjemplo);
        when(productoService.buscarPorCodigo("PROD001")).thenReturn(productoEjemplo);
        when(ordenCompraRepository.save(any(OrdenCompra.class))).thenReturn(ordenCompraEjemplo);
        when(detalleCompraRepository.save(any(DetalleCompra.class))).thenReturn(new DetalleCompra());

        compraService.generarCompra("usuario123");

        verify(carritoCompraService, times(1)).vaciarCarrito("usuario123");
    }
}
