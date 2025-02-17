package com.springproject.ecommercecore.security.integration;

import com.springproject.ecommercecore.security.UserDetailsServiceImpl;
import com.springproject.ecommercecore.dataaccess.UsuarioDataAccess;
import com.springproject.ecommercecore.model.postgresql.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class UserDetailsServiceImplIntegrationTest {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UsuarioDataAccess usuarioDataAccess;

    @BeforeEach
    void setUp() {
        // Insertar un usuario de prueba antes de cada test
        Usuario usuario = new Usuario("usuario123", "hashedpassword", "correo@example.com", Set.of("ROLE_USER"),true);
        usuarioDataAccess.guardarUsuario(usuario);
    }

    /**
     * Prueba de integración para verificar que `UserDetailsServiceImpl` carga un usuario existente desde la BD.
     */
    @Test
    void loadUserByUsername_DeberiaRetornarUserDetailsSiUsuarioExiste() {
        UserDetails userDetails = userDetailsService.loadUserByUsername("usuario123");

        assertNotNull(userDetails);
        assertEquals("usuario123", userDetails.getUsername());
    }

    /**
     * Prueba de integración para verificar que `UserDetailsServiceImpl` lanza una excepción si el usuario no existe.
     */
    @Test
    void loadUserByUsername_DeberiaLanzarExceptionSiUsuarioNoExiste() {
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("usuarioNoExistente"));
    }
}
