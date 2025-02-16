package com.springproject.ecommercecore.security.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 5, message = "El nombre de usuario debe tener al menos 5 caracteres")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "^(?i)(USER|ADMIN)$", message = "El rol debe ser USER o ADMIN (mayúsculas o minúsculas)")
    private String role;
}

