package com.springproject.ecommercecore.dataaccess;

import com.springproject.ecommercecore.model.postgresql.Usuario;
import com.springproject.ecommercecore.repository.postgresql.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UsuarioDataAccess {

    private final UsuarioRepository usuarioRepository;

    public Optional<Usuario> buscarPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public boolean existePorId(Integer id) {
        return usuarioRepository.existsById(id);
    }

    public boolean existePorUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    public boolean existePorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public List<Usuario> buscarTodosUsuarios() {
        return usuarioRepository.findAll(); // Ahora devuelve todos los usuarios, activos e inactivos
    }

    public List<Usuario> buscarUsuariosActivos() {
        return usuarioRepository.findByEnabledTrue();
    }

    public void guardarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void eliminarPorId(Integer id) {
        usuarioRepository.deleteById(id);
    }
}