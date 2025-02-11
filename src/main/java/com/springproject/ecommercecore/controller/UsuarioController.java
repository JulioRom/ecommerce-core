package com.springproject.ecommercecore.controller;

import com.springproject.ecommercecore.model.postgresql.Usuario;
import com.springproject.ecommercecore.security.dto.RegisterRequest;
import com.springproject.ecommercecore.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * 🔹 Listar todos los usuarios (Solo accesible por ADMIN)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    /**
     * 🔹 Obtener usuario por ID
     */
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.obtenerPorId(id);
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 🔹 Obtener usuario por username
     */
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/buscar")
    public ResponseEntity<Usuario> obtenerUsuarioPorUsername(@RequestParam String username) {
        Optional<Usuario> usuario = usuarioService.obtenerPorUsername(username);
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 🔹 Actualizar usuario (Solo el usuario o un ADMIN pueden modificar)
     */
    @PreAuthorize("#username == authentication.principal.username or hasRole('ADMIN')")
    @PutMapping("/{username}")
    public ResponseEntity<String> actualizarUsuario(@PathVariable String username, @Valid @RequestBody RegisterRequest request) {
        try {
            String response = usuarioService.actualizarUsuario(username, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 🔹 Eliminar usuario por ID (Solo ADMIN puede eliminar)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        if (usuarioService.eliminarUsuario(id)) {
            return ResponseEntity.ok("Usuario eliminado correctamente.");
        }
        return ResponseEntity.notFound().build();
    }
}
