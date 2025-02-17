package com.springproject.ecommercecore.security.service;

import com.springproject.ecommercecore.dataaccess.UsuarioDataAccess;
import com.springproject.ecommercecore.model.postgresql.Usuario;
import com.springproject.ecommercecore.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UsuarioDataAccess usuarioDataAccess;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("usuario123", "hashedpassword", "correo@example.com", Set.of("ROLE_USER"), true);
    }

    @Test
    void loadUserByUsername_DeberiaRetornarUserDetailsSiUsuarioExiste() {
        when(usuarioDataAccess.buscarPorUsername("usuario123")).thenReturn(Optional.of(usuario));

        UserDetails userDetails = userDetailsService.loadUserByUsername("usuario123");

        assertEquals("usuario123", userDetails.getUsername());
        assertEquals("hashedpassword", userDetails.getPassword());
        assertFalse(userDetails.getAuthorities().isEmpty());
    }

    @Test
    void loadUserByUsername_DeberiaLanzarExceptionSiUsuarioNoExiste() {
        when(usuarioDataAccess.buscarPorUsername("usuario123")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("usuario123"));
    }
}
