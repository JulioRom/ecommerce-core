package com.springproject.ecommercecore.security;

import com.springproject.ecommercecore.model.postgresql.Usuario;
import com.springproject.ecommercecore.repository.postgresql.UsuarioRepository;
import com.springproject.ecommercecore.security.dto.AuthRequest;
import com.springproject.ecommercecore.security.dto.AuthResponse;
import com.springproject.ecommercecore.security.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 🔹 Método para autenticar usuarios
     * 🔹 Válida las credenciales y genera un JWT si son correctas
     */
    public AuthResponse authenticate(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtUtil.generateToken(usuario.getUsername());
        return new AuthResponse(token);
    }
}

