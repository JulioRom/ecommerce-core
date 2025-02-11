package com.springproject.ecommercecore.controller;

import com.springproject.ecommercecore.exception.RecursoNoEncontradoException;
import com.springproject.ecommercecore.model.postgresql.Producto;
import com.springproject.ecommercecore.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductoControllerTest {

    @InjectMocks
    private ProductoController productoController;

    @Mock
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarProductos_CuandoExistenProductos_DeberiaRetornarLista() {
        // Arrange
        List<Producto> productos = Arrays.asList(
                new Producto(1, "Laptop", 1500, 10),
                new Producto(2, "Mouse", 25, 50)
        );

        when(productoService.listarProductos()).thenReturn(productos);

        // Act
        ResponseEntity<List<Producto>> response = productoController.listarProductos();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());

        verify(productoService, times(1)).listarProductos();
    }

    @Test
    void obtenerProducto_CuandoExiste_DeberiaRetornarProducto() {
        // Arrange
        String codigo = "Laptop";
        Producto producto = new Producto(1, codigo, 1500, 10);

        when(productoService.buscarPorCodigo(codigo)).thenReturn(producto);

        // Act
        ResponseEntity<Producto> response = productoController.obtenerProducto(codigo);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(codigo, Objects.requireNonNull(response.getBody()).getCodigoProducto());

        verify(productoService, times(1)).buscarPorCodigo(codigo);
    }

    @Test
    void agregarProducto_DeberiaGuardarYRetornarProducto() {
        // Arrange
        Producto producto = new Producto(2, "Teclado", 50, 30);
        when(productoService.guardarProducto(producto)).thenReturn(producto);

        // Act
        ResponseEntity<Producto> response = productoController.agregarProducto(producto);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Teclado", Objects.requireNonNull(response.getBody()).getCodigoProducto());

        verify(productoService, times(1)).guardarProducto(producto);
    }

    @Test
    void actualizarProducto_CuandoExiste_DeberiaActualizarProducto() {
        // Arrange
        String codigo = "Laptop Pro";
        Producto productoActualizado = new Producto(5, codigo, 1700, 8);

        // Simular que el producto existe antes de actualizarlo
        when(productoService.buscarPorCodigo(codigo)).thenReturn(productoActualizado);
        when(productoService.actualizarProducto(eq(codigo), any(Producto.class))).thenReturn(productoActualizado);

        // Act
        ResponseEntity<Producto> response = productoController.actualizarProducto(codigo, productoActualizado);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Laptop Pro", Objects.requireNonNull(response.getBody()).getCodigoProducto());

        verify(productoService, times(1)).buscarPorCodigo(codigo);
        verify(productoService, times(1)).actualizarProducto(eq(codigo), any(Producto.class));
    }


    @Test
    void eliminarProducto_CuandoExiste_DeberiaEliminarProducto() {
        // Arrange
        String codigo = "Laptop";
        Producto productoExistente = new Producto(1, codigo, 1500, 10);

        // Simular que el producto existe antes de eliminarlo
        when(productoService.buscarPorCodigo(codigo)).thenReturn(productoExistente);
        doNothing().when(productoService).eliminarProducto(codigo);

        // Act
        ResponseEntity<Void> response = productoController.eliminarProducto(codigo);

        // Assert
        assertNotNull(response);
        assertEquals(204, response.getStatusCode().value()); // No Content

        // Verificar que primero se llamó a buscar el producto antes de eliminarlo
        verify(productoService, times(1)).buscarPorCodigo(codigo);

        //  Verificar que luego se llamó a eliminar el producto
        verify(productoService, times(1)).eliminarProducto(codigo);
    }


    @Test
    void obtenerProducto_CuandoNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        String codigo = "ProductoInexistente";
        String mensajeEsperado = "Producto con código " + codigo + " no encontrado.";

        // Simular que el servicio lanza la excepción cuando el producto no existe
        when(productoService.buscarPorCodigo(codigo)).thenThrow(new RecursoNoEncontradoException(mensajeEsperado));

        // Act & Assert
        RecursoNoEncontradoException exception = assertThrows(RecursoNoEncontradoException.class, () -> {
            productoController.obtenerProducto(codigo);
        });

        // Verificar que el mensaje de la excepción es el esperado
        assertNotNull(exception);
        assertEquals(mensajeEsperado, exception.getMessage());

        // Verificar que el servicio fue llamado exactamente una vez con el código correcto
        verify(productoService, times(1)).buscarPorCodigo(codigo);
    }



    @Test
    void agregarProducto_CuandoDatosInvalidos_DeberiaRetornarBadRequest() {
        // Arrange
        Producto productoInvalido = new Producto(5, null, -10, -5); // Nombre nulo, precio negativo, stock negativo

        // Act
        ResponseEntity<Producto> response = productoController.agregarProducto(productoInvalido);

        // Assert
        assertEquals(400, response.getStatusCode().value()); // Bad Request
        verify(productoService, never()).guardarProducto(any());
    }

    @Test
    void actualizarProducto_CuandoNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        String codigo = "ProductoInexistente";
        Producto productoDetalles = new Producto(1, "Producto Actualizado", 1200, 5);

        when(productoService.buscarPorCodigo(codigo)).thenReturn(null);

        // Act & Assert
        RecursoNoEncontradoException exception = assertThrows(RecursoNoEncontradoException.class, () -> {
            productoController.actualizarProducto(codigo, productoDetalles);
        });

        assertEquals("No se puede actualizar. Producto con código ProductoInexistente no encontrado.", exception.getMessage());

        verify(productoService, times(1)).buscarPorCodigo(codigo);
        verify(productoService, never()).actualizarProducto(anyString(), any());
    }

    @Test
    void eliminarProducto_CuandoNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        String codigo = "ProductoInexistente";
        when(productoService.buscarPorCodigo(codigo)).thenReturn(null);

        // Act & Assert
        RecursoNoEncontradoException exception = assertThrows(RecursoNoEncontradoException.class, () -> {
            productoController.eliminarProducto(codigo);
        });

        assertEquals("No se puede eliminar. Producto con código ProductoInexistente no encontrado.", exception.getMessage());

        verify(productoService, times(1)).buscarPorCodigo(codigo);
        verify(productoService, never()).eliminarProducto(anyString());
    }
}
