package com.springproject.ecommercecore.service;

import com.springproject.ecommercecore.business.UsuarioManager;
import com.springproject.ecommercecore.model.postgresql.Usuario;
import com.springproject.ecommercecore.repository.postgresql.UsuarioRepository;
import com.springproject.ecommercecore.security.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioManager usuarioManager;

    /**
     *  Listar todos los usuarios
     */
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     *  Obtener usuario por ID
     */
    public Optional<Usuario> obtenerPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    /**
     *  Obtener usuario por username
     */
    public Optional<Usuario> obtenerPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    /**
     *  Registrar un nuevo usuario
     */
    public String registrarUsuario(RegisterRequest request) {
        usuarioManager.validarRegistro(usuarioRepository, request);

        Usuario newUser = usuarioManager.crearUsuario(request);
        usuarioRepository.save(newUser);
        return "Usuario registrado exitosamente";
    }

    /**
     *  Actualizar usuario existente
     */
    public String actualizarUsuario(String username, RegisterRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        usuarioManager.actualizarUsuario(usuario, request);
        usuarioRepository.save(usuario);
        return "Usuario actualizado correctamente";
    }

    /**
     *  Eliminar usuario por ID
     */
    public boolean eliminarUsuario(Integer id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
