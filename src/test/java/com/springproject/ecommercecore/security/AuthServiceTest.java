package com.springproject.ecommercecore.security;

import com.springproject.ecommercecore.security.dto.AuthRequest;
import com.springproject.ecommercecore.security.dto.AuthResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthService authService;

    private AuthRequest request;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        request = new AuthRequest("usuario123", "password123");
        userDetails = new User("usuario123", "password123", List.of());

        // Asegurar que userDetailsService no sea null
        assertNotNull(userDetailsService, "userDetailsService no está siendo inyectado correctamente");

        when(userDetailsService.loadUserByUsername(request.getUsername())).thenReturn(userDetails);
    }

    @Test
    void authenticate_DeberiaGenerarTokenSiCredencialesSonValidas() {
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(userDetailsService.loadUserByUsername(request.getUsername()))
                .thenReturn(userDetails);

        when(tokenService.generateToken(userDetails))
                .thenReturn("mocked-token");

        AuthResponse response = authService.authenticate(request);

        assertEquals("mocked-token", response.getToken());
    }

}
