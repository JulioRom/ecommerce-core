package com.springproject.ecommercecore.business;

import com.springproject.ecommercecore.dataaccess.UsuarioDataAccess;
import com.springproject.ecommercecore.model.postgresql.Usuario;
import com.springproject.ecommercecore.security.dto.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioManagerTest {

    @Mock
    private UsuarioDataAccess usuarioDataAccess;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioManager usuarioManager;

    private RegisterRequest request;

    @BeforeEach
    void setUp() {
        request = new RegisterRequest("testuser", "password123", "test@example.com", "USER");
    }

    @Test
    void validarRegistro_UsuarioExistente_DeberiaLanzarExcepcion() {
        when(usuarioDataAccess.existePorUsername(request.getUsername())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                usuarioManager.validarRegistro(request));

        assertEquals("El usuario ya existe", exception.getMessage());
    }

    @Test
    void validarRegistro_EmailExistente_DeberiaLanzarExcepcion() {
        when(usuarioDataAccess.existePorEmail(request.getEmail())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                usuarioManager.validarRegistro(request));

        assertEquals("El correo electrónico ya está registrado", exception.getMessage());
    }

    @Test
    void validarRegistro_UsuarioYEmailNoExisten_NoLanzaExcepcion() {
        when(usuarioDataAccess.existePorUsername(request.getUsername())).thenReturn(false);
        when(usuarioDataAccess.existePorEmail(request.getEmail())).thenReturn(false);

        assertDoesNotThrow(() -> usuarioManager.validarRegistro(request));
    }

    @Test
    void crearUsuario_DeberiaHashearPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        UsuarioManager usuarioManager = new UsuarioManager(usuarioDataAccess, encoder);

        Usuario usuario = usuarioManager.crearUsuario(request);

        assertEquals("testuser", usuario.getUsername());
        assertTrue(encoder.matches(request.getPassword(), usuario.getPassword())); // Verifica el hash real
        assertEquals("test@example.com", usuario.getEmail());
        assertEquals(Set.of("ROLE_USER"), usuario.getRoles());
    }

    @Test
    void actualizarUsuario_DeberiaActualizarEmailYPassword() {
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setPassword("oldpassword");
        usuario.setEmail("old@example.com");

        when(passwordEncoder.encode(request.getPassword())).thenReturn("newhashedpassword");

        usuarioManager.actualizarUsuario(usuario, request);

        assertEquals("test@example.com", usuario.getEmail());
        assertEquals("newhashedpassword", usuario.getPassword());
    }
}
