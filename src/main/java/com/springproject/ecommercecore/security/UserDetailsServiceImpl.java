package com.springproject.ecommercecore.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 🔹 Aquí podrías consultar una base de datos de usuarios en el futuro
        if (!username.equals("admin")) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        // 🔹 Devolver un usuario en memoria con la contraseña codificada
        return new User("admin", "{noop}admin", new ArrayList<>());
    }
}
