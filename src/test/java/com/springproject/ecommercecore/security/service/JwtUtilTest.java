package com.springproject.ecommercecore.security.service;

import com.springproject.ecommercecore.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secretKey", "MiClaveSecretaParaJWTDe64CaracteresDeLargoSeguro1234567890!");
        ReflectionTestUtils.setField(jwtUtil, "expirationTime", 3600000L); // 1 hora en milisegundos
        userDetails = new User("usuario123", "password123", List.of());
    }



    @Test
    void generateToken_DeberiaCrearUnTokenValido() {
        String token = jwtUtil.generateToken(userDetails);
        assertNotNull(token);
    }

    @Test
    void extractUsername_DeberiaRetornarElUsernameCorrecto() {
        String token = jwtUtil.generateToken(userDetails);
        assertEquals(userDetails.getUsername(), jwtUtil.extractUsername(token));
    }

    @Test
    void validateToken_DeberiaRetornarTrueSiElTokenEsValido() {
        String token = jwtUtil.generateToken(userDetails);
        boolean esValido = jwtUtil.validateToken(token, userDetails);
        assertTrue(esValido, "El token debería ser válido");
    }


    @Test
    void validateToken_DeberiaRetornarFalseSiElTokenEsInvalido() {
        assertFalse(jwtUtil.validateToken("token-invalido", userDetails));
    }
}
