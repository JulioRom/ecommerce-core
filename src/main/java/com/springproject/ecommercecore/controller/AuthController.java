package com.springproject.ecommercecore.controller;

import com.springproject.ecommercecore.security.AuthService;
import com.springproject.ecommercecore.security.dto.AuthRequest;
import com.springproject.ecommercecore.security.dto.AuthResponse;
import com.springproject.ecommercecore.security.dto.RegisterRequest;
import com.springproject.ecommercecore.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Autenticación", description = "Operaciones relacionadas con la autenticación de usuarios")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;

    /**
     *  Endpoint para iniciar sesión
     *  Retorna un JWT si las credenciales son correctas
     */
    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario y devuelve un token JWT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Credenciales del usuario",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\n  \"username\": \"usuario123\",\n  \"password\": \"contraseña\"\n}")))
            @Valid @RequestBody AuthRequest request) {
        try {
            AuthResponse response = authService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body(new AuthResponse("Credenciales inválidas"));
        }
    }

    /**
     *  Endpoint para registrar un nuevo usuario
     *  Válida que el usuario no exista antes de crearlo
     */
    @Operation(summary = "Registrar un nuevo usuario", description = "Crea un nuevo usuario en la base de datos.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "El usuario ya existe")
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del usuario para registro",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\n  \"username\": \"SwaggerUser\",\n  \"password\": \"contraseñaSegura\",\n  \"email\": \"swagger@email.com\",\n  \"role\": \"admin\"\n}")))
            @Valid @RequestBody RegisterRequest request)  {
        try {
            String response = usuarioService.registrarUsuario(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
