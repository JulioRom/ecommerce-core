package com.springproject.ecommercecore.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtUtil jwtUtil;

    public String generateToken(UserDetails userDetails) {
        return jwtUtil.generateToken(userDetails);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return jwtUtil.validateToken(token, userDetails);
    }

    public String extractUsername(String token) {
        return jwtUtil.extractUsername(token);
    }
}

