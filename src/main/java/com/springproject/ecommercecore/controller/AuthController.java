package com.springproject.ecommercecore.controller;

import com.springproject.ecommercecore.security.AuthService;
import com.springproject.ecommercecore.security.dto.AuthRequest;
import com.springproject.ecommercecore.security.dto.AuthResponse;
import com.springproject.ecommercecore.security.dto.RegisterRequest;
import com.springproject.ecommercecore.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;

    /**
     * 🔹 Endpoint para iniciar sesión
     * 🔹 Retorna un JWT si las credenciales son correctas
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        try {
            AuthResponse response = authService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body(new AuthResponse("Credenciales inválidas"));
        }
    }

    /**
     * 🔹 Endpoint para registrar un nuevo usuario
     * 🔹 Valida que el usuario no exista antes de crearlo
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        try {
            String response = usuarioService.registrarUsuario(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
