package com.springproject.ecommercecore.service;

import com.springproject.ecommercecore.dataaccess.OrdenCompraDataAccess;
import com.springproject.ecommercecore.dataaccess.UsuarioDataAccess;
import com.springproject.ecommercecore.exception.RecursoNoEncontradoException;
import com.springproject.ecommercecore.model.postgresql.OrdenCompra;
import com.springproject.ecommercecore.model.postgresql.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompraServiceTest {

    @Mock
    private OrdenCompraDataAccess ordenCompraDataAccess;

    @Mock
    private UsuarioDataAccess usuarioDataAccess;

    @InjectMocks
    private CompraService compraService;

    private Usuario usuario;
    private OrdenCompra ordenCompra;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("usuario123", "hashedpassword", "correo@example.com", Set.of("ROLE_USER"),true);
        usuario.setId(1);

        ordenCompra = new OrdenCompra(usuario, LocalDateTime.now());
        ordenCompra.setId(1);
        ordenCompra.setEstado(OrdenCompra.EstadoOrden.PENDIENTE);
    }

    @Test
    void generarOrden_DeberiaCrearOrdenSiUsuarioExiste() {
        when(usuarioDataAccess.buscarPorId(1)).thenReturn(Optional.of(usuario));
        when(ordenCompraDataAccess.guardarOrden(any(OrdenCompra.class))).thenReturn(ordenCompra);

        OrdenCompra ordenGenerada = compraService.generarOrden(1, LocalDateTime.now());

        assertNotNull(ordenGenerada);
        assertEquals(OrdenCompra.EstadoOrden.PENDIENTE, ordenGenerada.getEstado());
        verify(ordenCompraDataAccess).guardarOrden(any(OrdenCompra.class));
    }

    @Test
    void generarOrden_DeberiaLanzarExcepcionSiUsuarioNoExiste() {
        when(usuarioDataAccess.buscarPorId(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RecursoNoEncontradoException.class, () ->
                compraService.generarOrden(1, LocalDateTime.now()));

        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    @Test
    void actualizarEstadoOrden_DeberiaActualizarSiOrdenExiste() {
        when(ordenCompraDataAccess.buscarPorId(1)).thenReturn(Optional.of(ordenCompra));
        when(ordenCompraDataAccess.guardarOrden(any(OrdenCompra.class))).thenReturn(ordenCompra);

        OrdenCompra ordenActualizada = compraService.actualizarEstadoOrden(1, OrdenCompra.EstadoOrden.ENTREGADO);

        assertNotNull(ordenActualizada);
        assertEquals(OrdenCompra.EstadoOrden.ENTREGADO, ordenActualizada.getEstado());
        verify(ordenCompraDataAccess).guardarOrden(any(OrdenCompra.class));
    }

    @Test
    void actualizarEstadoOrden_DeberiaLanzarExcepcionSiOrdenNoExiste() {
        when(ordenCompraDataAccess.buscarPorId(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RecursoNoEncontradoException.class, () ->
                compraService.actualizarEstadoOrden(1, OrdenCompra.EstadoOrden.ENTREGADO));

        assertEquals("Orden de compra no encontrada", exception.getMessage());
    }

    @Test
    void cancelarOrden_DeberiaCancelarSiOrdenExiste() {
        doNothing().when(ordenCompraDataAccess).cancelarOrden(1);

        compraService.cancelarOrden(1);

        verify(ordenCompraDataAccess).cancelarOrden(1);
    }

    @Test
    void cancelarOrden_DeberiaLanzarExcepcionSiOrdenNoExiste() {
        doThrow(new RecursoNoEncontradoException("Orden de compra no encontrada")).when(ordenCompraDataAccess).cancelarOrden(1);

        Exception exception = assertThrows(RecursoNoEncontradoException.class, () ->
                compraService.cancelarOrden(1));

        assertEquals("Orden de compra no encontrada", exception.getMessage());
    }

    @Test
    void obtenerOrdenPorId_DeberiaRetornarOrdenSiExiste() {
        when(ordenCompraDataAccess.buscarPorId(1)).thenReturn(Optional.of(ordenCompra));

        Optional<OrdenCompra> resultado = compraService.obtenerOrdenPorId(1);

        assertTrue(resultado.isPresent());
        assertEquals(1, resultado.get().getId());
    }

    @Test
    void obtenerOrdenPorId_DeberiaRetornarOptionalVacioSiNoExiste() {
        when(ordenCompraDataAccess.buscarPorId(1)).thenReturn(Optional.empty());

        Optional<OrdenCompra> resultado = compraService.obtenerOrdenPorId(1);

        assertFalse(resultado.isPresent());
    }
}
