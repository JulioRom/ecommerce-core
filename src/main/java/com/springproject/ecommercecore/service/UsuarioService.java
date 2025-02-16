package com.springproject.ecommercecore.service;

import com.springproject.ecommercecore.business.UsuarioManager;
import com.springproject.ecommercecore.dataaccess.UsuarioDataAccess;
import com.springproject.ecommercecore.model.postgresql.Usuario;
import com.springproject.ecommercecore.security.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioDataAccess usuarioDataAccess;
    private final UsuarioManager usuarioManager;

    /**
     * Listar todos los usuarios activos.
     */
    public List<Usuario> listarUsuarios() {
        return usuarioDataAccess.buscarTodosUsuarios(); // Ahora devuelve usuarios activos e inactivos
    }

    /**
     * Obtener usuario por ID.
     */
    public Optional<Usuario> obtenerPorId(Integer id) {
        return usuarioDataAccess.buscarPorId(id);
    }

    /**
     * Obtener usuario por username.
     */
    public Optional<Usuario> obtenerPorUsername(String username) {
        return usuarioDataAccess.buscarPorUsername(username);
    }

    /**
     * Registrar un nuevo usuario.
     */
    @Transactional
    public String registrarUsuario(RegisterRequest request) {
        if (usuarioDataAccess.existePorUsername(request.getUsername())) {
            throw new IllegalArgumentException("El usuario ya existe");
        }
        if (usuarioDataAccess.existePorEmail(request.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }

        Usuario newUser = usuarioManager.crearUsuario(request);
        usuarioDataAccess.guardarUsuario(newUser);
        return "Usuario registrado exitosamente";
    }

    /**
     * Actualizar usuario existente.
     */
    @Transactional
    public String actualizarUsuario(String username, RegisterRequest request) {
        Usuario usuario = usuarioDataAccess.buscarPorUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        usuarioManager.actualizarUsuario(usuario, request);
        usuarioDataAccess.guardarUsuario(usuario);
        return "Usuario actualizado correctamente";
    }

    /**
     * Eliminar usuario por ID.
     */
    @Transactional
    public boolean eliminarUsuario(Integer id) {
        if (!usuarioDataAccess.existePorId(id)) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        usuarioDataAccess.eliminarPorId(id);
        return true;
    }

}
