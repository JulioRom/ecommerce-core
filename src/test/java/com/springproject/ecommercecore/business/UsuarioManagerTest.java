package com.springproject.ecommercecore.business;

import com.springproject.ecommercecore.model.postgresql.Usuario;
import com.springproject.ecommercecore.security.dto.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioManagerTest {

    private UsuarioManager usuarioManager;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        usuarioManager = new UsuarioManager(passwordEncoder);
    }

    @Test
    void testCrearUsuario() {
        RegisterRequest request = new RegisterRequest("usuarioTest", "password123", "test@example.com", "USER");
        Usuario usuario = usuarioManager.crearUsuario(request);

        assertNotNull(usuario);
        assertEquals("usuarioTest", usuario.getUsername());
        assertTrue(passwordEncoder.matches("password123", usuario.getPassword())); // Verifica cifrado de contraseña
        assertEquals("test@example.com", usuario.getEmail());
    }

    @Test
    void testActualizarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setUsername("usuarioTest");
        usuario.setEmail("old@example.com");
        usuario.setPassword(passwordEncoder.encode("oldPassword"));

        RegisterRequest request = new RegisterRequest("usuarioTest", "newPassword123", "new@example.com", "USER");
        usuarioManager.actualizarUsuario(usuario, request);

        assertEquals("new@example.com", usuario.getEmail());
        assertTrue(passwordEncoder.matches("newPassword123", usuario.getPassword()));
    }
}
