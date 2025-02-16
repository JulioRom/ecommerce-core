package com.springproject.ecommercecore.controller;

import com.springproject.ecommercecore.model.postgresql.Usuario;
import com.springproject.ecommercecore.security.dto.RegisterRequest;
import com.springproject.ecommercecore.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Usuarios", description = "Operaciones relacionadas con la gestión de usuarios")
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Validated
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * 🔹 Listar todos los usuarios (Solo accesible por ADMIN)
     */
    @Operation(summary = "Listar todos los usuarios", description = "Obtiene una lista de todos los usuarios registrados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida correctamente")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    /**
     * 🔹 Obtener usuario por ID
     */
    @Operation(summary = "Obtener usuario por ID", description = "Devuelve un usuario basado en su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Integer id) {
        Optional<Usuario> usuario = usuarioService.obtenerPorId(id);
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 🔹 Obtener usuario por username
     */
    @Operation(summary = "Obtener usuario por username", description = "Devuelve un usuario basado en su username.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
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
    @Operation(summary = "Actualizar usuario", description = "Permite actualizar la información de un usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PreAuthorize("#username == authentication.principal.username or hasRole('ADMIN')")
    @PutMapping("/{username}")
    public ResponseEntity<String> actualizarUsuario(
            @PathVariable String username,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del usuario a actualizar",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\n  \"username\": \"usuario123\",\n  \"email\": \"usuario@email.com\",\n  \"password\": \"nuevacontraseña\"\n}")))
            @Valid @RequestBody RegisterRequest request) {
        try {
            String response = usuarioService.actualizarUsuario(username, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Usuario no encontrado")) {
                return ResponseEntity.status(404).body("Error: " + e.getMessage()); // 🔹 Retorna 404 si el usuario no existe
            }
            return ResponseEntity.badRequest().body("Error: " + e.getMessage()); // 🔹 Retorna 400 si hay otro error
        }
    }

    /**
     * 🔹 Eliminar usuario por ID (Solo ADMIN puede eliminar)
     */
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario por su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Integer id) {
        try {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.ok("Usuario eliminado correctamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Error: " + e.getMessage());
        }
    }

}
