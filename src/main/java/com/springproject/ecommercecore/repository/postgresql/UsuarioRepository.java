package com.springproject.ecommercecore.repository.postgresql;

import com.springproject.ecommercecore.model.postgresql.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsername(String username);
}
