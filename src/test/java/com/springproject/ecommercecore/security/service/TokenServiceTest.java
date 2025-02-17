package com.springproject.ecommercecore.security.service;

import com.springproject.ecommercecore.security.JwtUtil;
import com.springproject.ecommercecore.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private TokenService tokenService;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        userDetails = new User("usuario123", "password123", List.of());
    }

    @Test
    void generateToken_DeberiaRetornarUnTokenValido() {
        when(jwtUtil.generateToken(userDetails)).thenReturn("mocked-token");

        String token = tokenService.generateToken(userDetails);

        assertEquals("mocked-token", token);
    }

    @Test
    void validateToken_DeberiaRetornarTrueSiElTokenEsValido() {
        when(jwtUtil.validateToken("mocked-token", userDetails)).thenReturn(true);

        boolean esValido = tokenService.validateToken("mocked-token", userDetails);

        assertTrue(esValido);
    }

    @Test
    void extractUsername_DeberiaRetornarElUsernameCorrecto() {
        when(jwtUtil.extractUsername("mocked-token")).thenReturn("usuario123");

        String username = tokenService.extractUsername("mocked-token");

        assertEquals("usuario123", username);
    }
}
