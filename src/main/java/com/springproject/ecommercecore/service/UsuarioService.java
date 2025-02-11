package com.springproject.ecommercecore.service;

import com.springproject.ecommercecore.model.postgresql.Usuario;
import com.springproject.ecommercecore.repository.postgresql.UsuarioRepository;
import com.springproject.ecommercecore.security.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 🔹 Listar todos los usuarios
     */
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     * 🔹 Obtener usuario por ID
     */
    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    /**
     * 🔹 Obtener usuario por username
     */
    public Optional<Usuario> obtenerPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    /**
     * 🔹 Registrar un nuevo usuario
     */
    public String registrarUsuario(RegisterRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("El usuario ya existe");
        }
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }

        Usuario newUser = new Usuario();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword())); // 🔹 Cifra la contraseña
        newUser.setEmail(request.getEmail());
        newUser.setRoles(Set.of("ROLE_" + request.getRole().toUpperCase()));

        usuarioRepository.save(newUser);
        return "Usuario registrado exitosamente";
    }

    /**
     * 🔹 Actualizar usuario existente
     */
    public String actualizarUsuario(String username, RegisterRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword())); // 🔹 Actualiza la contraseña cifrada

        usuarioRepository.save(usuario);
        return "Usuario actualizado correctamente";
    }

    /**
     * 🔹 Eliminar usuario por ID
     */
    public boolean eliminarUsuario(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
