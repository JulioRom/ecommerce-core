package com.springproject.ecommercecore.controller;

import com.springproject.ecommercecore.model.postgresql.Usuario;
import com.springproject.ecommercecore.security.AuthService;
import com.springproject.ecommercecore.security.dto.AuthRequest;
import com.springproject.ecommercecore.security.dto.AuthResponse;
import com.springproject.ecommercecore.security.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        authService.registerUser(request);
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }
}
