package com.springproject.ecommercecore.business;

import com.springproject.ecommercecore.model.postgresql.Usuario;
import com.springproject.ecommercecore.repository.postgresql.UsuarioRepository;
import com.springproject.ecommercecore.security.dto.RegisterRequest;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class UsuarioManager {

    private final BCryptPasswordEncoder passwordEncoder;

    /**
     *  Validar si un usuario puede ser registrado
     */
    public void validarRegistro(UsuarioRepository usuarioRepository, RegisterRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("El usuario ya existe");
        }
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }
    }

    /**
     *  Crear un nuevo usuario con datos validados
     */
    public Usuario crearUsuario(RegisterRequest request) {
        Usuario newUser = new Usuario();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setEmail(request.getEmail());
        newUser.setRoles(Set.of("ROLE_" + request.getRole().toUpperCase()));
        return newUser;
    }

    /**
     *  Actualizar usuario existente con nuevos datos
     */
    public void actualizarUsuario(Usuario usuario, RegisterRequest request) {
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
    }
}
