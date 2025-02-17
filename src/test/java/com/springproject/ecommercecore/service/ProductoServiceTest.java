package com.springproject.ecommercecore.service;

import com.springproject.ecommercecore.dataaccess.ProductoDataAccess;
import com.springproject.ecommercecore.exception.RecursoExistenteException;
import com.springproject.ecommercecore.exception.RecursoNoEncontradoException;
import com.springproject.ecommercecore.model.postgresql.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoDataAccess productoDataAccess;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto("P001", 1000.0, 10);
    }

    @Test
    void listarProductos_DeberiaRetornarListaDeProductosSiExisten() {
        when(productoDataAccess.listarTodos()).thenReturn(List.of(producto));

        List<Producto> productos = productoService.listarProductos();

        assertFalse(productos.isEmpty());
        assertEquals(1, productos.size());
    }

    @Test
    void listarProductos_DeberiaRetornarListaVaciaSiNoExisten() {
        when(productoDataAccess.listarTodos()).thenReturn(List.of());

        List<Producto> productos = productoService.listarProductos();

        assertTrue(productos.isEmpty());
    }

    @Test
    void buscarPorCodigo_DeberiaRetornarProductoSiExiste() {
        when(productoDataAccess.buscarPorCodigo("P001")).thenReturn(Optional.of(producto));

        Optional<Producto> resultado = Optional.ofNullable(productoService.buscarPorCodigo("P001"));

        assertNotNull(resultado);
        assertEquals("P001", resultado.get().getCodigoProducto());
    }

    @Test
    void buscarPorCodigo_DeberiaLanzarExcepcionSiNoExiste() {
        when(productoDataAccess.buscarPorCodigo("P002")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RecursoNoEncontradoException.class, () ->
                productoService.buscarPorCodigo("P002"));

        assertEquals("Producto con código P002 no encontrado", exception.getMessage());
    }


    @Test
    void guardarProducto_DeberiaGuardarProductoSiEsNuevo() {
        when(productoDataAccess.buscarPorCodigo("P001")).thenReturn(Optional.empty());

        productoService.guardarProducto(producto);

        verify(productoDataAccess).guardarProducto(producto);
    }

    @Test
    void guardarProducto_DeberiaLanzarExcepcionSiCodigoYaExiste() {
        when(productoDataAccess.buscarPorCodigo("P001")).thenReturn(Optional.of(producto));

        Exception exception = assertThrows(RecursoExistenteException.class, () ->
                productoService.guardarProducto(producto));

        assertEquals("El código del producto ya existe", exception.getMessage());
    }


    @Test
    void actualizarStock_DeberiaActualizarSiProductoExiste() {
        when(productoDataAccess.buscarPorCodigo("P001")).thenReturn(Optional.of(producto));
        when(productoDataAccess.descontarStock("P001", 5)).thenReturn(new Producto("P001", 1000.0, 5));

        Producto productoActualizado = productoService.actualizarStock("P001", 5);

        assertNotNull(productoActualizado);
        assertEquals(5, productoActualizado.getStock()); // Verifica que el stock se haya actualizado
        verify(productoDataAccess).descontarStock("P001", 5);
    }


    @Test
    void actualizarStock_DeberiaLanzarExcepcionSiProductoNoExiste() {
        when(productoDataAccess.buscarPorCodigo("P002")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RecursoNoEncontradoException.class, () ->
                productoService.actualizarStock("P002", 5));

        assertEquals("Producto con código P002 no encontrado", exception.getMessage());
    }


    @Test
    void eliminarProducto_DeberiaEliminarSiProductoExiste() {
        when(productoDataAccess.buscarPorCodigo("P001")).thenReturn(Optional.of(producto));
        doNothing().when(productoDataAccess).eliminarProducto(producto);

        productoService.eliminarProducto("P001");

        verify(productoDataAccess).eliminarProducto(producto);
    }

    @Test
    void eliminarProducto_DeberiaLanzarExcepcionSiProductoNoExiste() {
        when(productoDataAccess.buscarPorCodigo("P002")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RecursoNoEncontradoException.class, () ->
                productoService.eliminarProducto("P002"));

        assertEquals("Producto con código P002 no encontrado", exception.getMessage());
    }
}
