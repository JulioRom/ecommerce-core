package com.springproject.ecommercecore.business;

import com.springproject.ecommercecore.model.postgresql.Usuario;
import com.springproject.ecommercecore.dataaccess.UsuarioDataAccess;
import com.springproject.ecommercecore.security.dto.RegisterRequest;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UsuarioManager {

    private final UsuarioDataAccess usuarioDataAccess;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     *  Crear un nuevo usuario con datos validados
     */
    public Usuario crearUsuario(RegisterRequest request) {
        return new Usuario(
        request.getUsername(),
        passwordEncoder.encode(request.getPassword()),
        request.getEmail(),
        Set.of("ROLE_" + request.getRole().toUpperCase()),
        true);
    }

    /**
     *  Actualizar usuario existente con nuevos datos
     */
    public void actualizarUsuario(Usuario usuario, RegisterRequest request) {
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
    }
}

