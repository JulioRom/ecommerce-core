package com.springproject.ecommercecore.security.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void registerRequest_DeberiaFallarSiCamposSonInvalidos() {
        RegisterRequest request = new RegisterRequest("", "", "correo-invalido", "INVALID_ROLE");
        Set<?> violaciones = validator.validate(request);
        assertFalse(violaciones.isEmpty());
    }

    @Test
    void registerRequest_DeberiaSerValido() {
        RegisterRequest request = new RegisterRequest("usuario123", "password123", "correo@example.com", "ADMIN");
        Set<?> violaciones = validator.validate(request);
        assertTrue(violaciones.isEmpty());
    }
}
