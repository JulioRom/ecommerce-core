package com.springproject.ecommercecore.security.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthResponseTest {

    @Test
    void authResponse_DeberiaGuardarYRetornarTokenCorrectamente() {
        String token = "test-token";
        AuthResponse response = new AuthResponse(token);

        assertEquals(token, response.getToken());
    }
}
