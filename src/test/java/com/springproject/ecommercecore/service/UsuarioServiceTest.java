package com.springproject.ecommercecore.service;

import com.springproject.ecommercecore.business.UsuarioManager;
import com.springproject.ecommercecore.model.postgresql.Usuario;
import com.springproject.ecommercecore.repository.postgresql.UsuarioRepository;
import com.springproject.ecommercecore.security.dto.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioManager usuarioManager;

    @InjectMocks
    private UsuarioService usuarioService;

    private RegisterRequest request;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        request = new RegisterRequest("testUser", "password123", "test@example.com", "USER");

        usuario = new Usuario();
        usuario.setUsername("testUser");
        usuario.setPassword("password123");
        usuario.setEmail("test@example.com");
        usuario.setRoles(Set.of("ROLE_USER"));
        usuario.setEnabled(true);
        usuario.setAccountNonExpired(true);
        usuario.setAccountNonLocked(true);
        usuario.setCredentialsNonExpired(true);

        // Forzar que `usuarioService` use el mock de `usuarioManager`
        usuarioService = new UsuarioService(usuarioRepository, usuarioManager);
    }


    @Test
    void testRegistrarUsuario() {
        // Asegurar que `validarRegistro()` no lanza excepción y se detecta
        doNothing().when(usuarioManager).validarRegistro(any(), any());
        when(usuarioManager.crearUsuario(any())).thenReturn(usuario);
        when(usuarioRepository.save(any())).thenReturn(usuario);

        // Ejecutar la prueba
        String resultado = usuarioService.registrarUsuario(request);

        // Validaciones
        assertEquals("Usuario registrado exitosamente", resultado);
        verify(usuarioManager, times(1)).validarRegistro(any(), any());  // ✅ Ahora sí se detectará
        verify(usuarioManager, times(1)).crearUsuario(any());
        verify(usuarioRepository, times(1)).save(any());
    }



    @Test
    void testActualizarUsuario_Existente() {
        // Simular que `findByUsername()` devuelve un usuario
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(usuario));

        // Simular actualización sin errores
        doNothing().when(usuarioManager).actualizarUsuario(any(), any());
        when(usuarioRepository.save(any())).thenReturn(usuario);

        // Ejecutar la prueba
        String resultado = usuarioService.actualizarUsuario("testUser", request);

        // Validaciones
        assertEquals("Usuario actualizado correctamente", resultado);
        verify(usuarioRepository, times(1)).findByUsername(anyString());
        verify(usuarioManager, times(1)).actualizarUsuario(any(), any());
        verify(usuarioRepository, times(1)).save(any());
    }

}
