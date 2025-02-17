package com.springproject.ecommercecore.service;

import com.springproject.ecommercecore.business.UsuarioManager;
import com.springproject.ecommercecore.dataaccess.UsuarioDataAccess;
import com.springproject.ecommercecore.model.postgresql.Usuario;
import com.springproject.ecommercecore.security.dto.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioDataAccess usuarioDataAccess;

    @Mock
    private UsuarioManager usuarioManager;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("usuario123", "hashedpassword", "correo@example.com", Set.of("ROLE_USER"),true);
        usuario.setId(1); // Asegurar que tiene un ID válido
        registerRequest = new RegisterRequest("usuario123", "password123", "correo@example.com", "USER");
    }

    @Test
    void registrarUsuario_DeberiaRegistrarNuevoUsuarioSiNoExiste() {
        when(usuarioDataAccess.existePorUsername(registerRequest.getUsername())).thenReturn(false);
        when(usuarioDataAccess.existePorEmail(registerRequest.getEmail())).thenReturn(false);
        when(usuarioManager.crearUsuario(registerRequest)).thenReturn(usuario);

        String resultado = usuarioService.registrarUsuario(registerRequest);

        assertEquals("Usuario registrado exitosamente", resultado);
        verify(usuarioDataAccess).guardarUsuario(usuario);
    }

    @Test
    void registrarUsuario_DeberiaLanzarExcepcionSiUsuarioYaExiste() {
        when(usuarioDataAccess.existePorUsername(registerRequest.getUsername())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                usuarioService.registrarUsuario(registerRequest));

        assertEquals("El usuario ya existe", exception.getMessage());
        verify(usuarioDataAccess, never()).guardarUsuario(any(Usuario.class));
    }

    @Test
    void registrarUsuario_DeberiaLanzarExcepcionSiEmailYaExiste() {
        when(usuarioDataAccess.existePorUsername(registerRequest.getUsername())).thenReturn(false);
        when(usuarioDataAccess.existePorEmail(registerRequest.getEmail())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                usuarioService.registrarUsuario(registerRequest));

        assertEquals("El correo electrónico ya está registrado", exception.getMessage());
        verify(usuarioDataAccess, never()).guardarUsuario(any(Usuario.class));
    }

    @Test
    void actualizarUsuario_DeberiaActualizarUsuarioSiExiste() {
        when(usuarioDataAccess.buscarPorUsername(usuario.getUsername())).thenReturn(Optional.of(usuario));

        usuarioService.actualizarUsuario(usuario.getUsername(), registerRequest);

        verify(usuarioManager).actualizarUsuario(usuario, registerRequest);
        verify(usuarioDataAccess).guardarUsuario(usuario);
    }

    @Test
    void actualizarUsuario_DeberiaLanzarExcepcionSiUsuarioNoExiste() {
        when(usuarioDataAccess.buscarPorUsername(usuario.getUsername())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                usuarioService.actualizarUsuario(usuario.getUsername(), registerRequest));

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(usuarioDataAccess, never()).guardarUsuario(any(Usuario.class));
    }

    @Test
    void eliminarUsuario_DeberiaEliminarUsuarioSiExiste() {
        when(usuarioDataAccess.existePorId(usuario.getId())).thenReturn(true);

        boolean resultado = usuarioService.eliminarUsuario(usuario.getId());

        assertTrue(resultado);
        verify(usuarioDataAccess).eliminarPorId(usuario.getId());
    }

    @Test
    void eliminarUsuario_DeberiaLanzarExcepcionSiUsuarioNoExiste() {
        when(usuarioDataAccess.existePorId(usuario.getId())).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                usuarioService.eliminarUsuario(usuario.getId()));

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(usuarioDataAccess, never()).eliminarPorId(any(Integer.class));
    }

    @Test
    void obtenerPorUsername_DeberiaRetornarUsuarioSiExiste() {
        when(usuarioDataAccess.buscarPorUsername(usuario.getUsername())).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.obtenerPorUsername(usuario.getUsername());

        assertTrue(resultado.isPresent());
        assertEquals(usuario.getUsername(), resultado.get().getUsername());
    }

    @Test
    void obtenerPorUsername_DeberiaRetornarOptionalVacioSiUsuarioNoExiste() {
        when(usuarioDataAccess.buscarPorUsername(usuario.getUsername())).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.obtenerPorUsername(usuario.getUsername());

        assertFalse(resultado.isPresent());
    }
}
