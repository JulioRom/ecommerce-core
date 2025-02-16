package com.springproject.ecommercecore.security.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AuthRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void authRequest_DeberiaFallarSiCamposSonNulos() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            AuthRequest request = new AuthRequest(null, null);
            Set<?> violaciones = validator.validate(request);
            assertFalse(violaciones.isEmpty());
        }
    }

    @Test
    void authRequest_DeberiaSerValido() {
        AuthRequest request = new AuthRequest("usuario123", "password123");
        Set<?> violaciones = validator.validate(request);
        assertTrue(violaciones.isEmpty());
    }
}
