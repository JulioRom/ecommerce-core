package com.springproject.ecommercecore.security;

import com.springproject.ecommercecore.repository.postgresql.UsuarioRepository;
import com.springproject.ecommercecore.security.dto.AuthRequest;
import com.springproject.ecommercecore.security.dto.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;  // 🔹 Agregamos UserDetailsService

    public AuthResponse authenticate(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        // 🔹 Cargar UserDetails para obtener los roles
        UserDetails usuario = userDetailsService.loadUserByUsername(request.getUsername());

        // 🔹 Generar token con UserDetails en lugar de solo username
        String token = jwtUtil.generateToken(usuario);

        return new AuthResponse(token);
    }
}

