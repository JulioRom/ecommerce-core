package com.springproject.ecommercecore.controller;

import com.springproject.ecommercecore.model.postgresql.Usuario;
import com.springproject.ecommercecore.repository.postgresql.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestParam String username,
                                               @RequestParam String password,
                                               @RequestParam(defaultValue = "USER") String role) {
        if (usuarioRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body("El usuario ya existe.");
        }

        Usuario nuevoUsuario = Usuario.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(Collections.singleton("ROLE_" + role.toUpperCase()).toString())
                .build();

        usuarioRepository.save(nuevoUsuario);
        return ResponseEntity.ok("Usuario registrado exitosamente con rol: " + role);
    }
}
