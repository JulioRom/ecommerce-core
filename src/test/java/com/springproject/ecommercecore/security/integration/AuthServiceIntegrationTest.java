package com.springproject.ecommercecore.security.integration;

import com.springproject.ecommercecore.dataaccess.UsuarioDataAccess;
import com.springproject.ecommercecore.model.postgresql.Usuario;
import com.springproject.ecommercecore.security.dto.AuthRequest;
import com.springproject.ecommercecore.security.dto.AuthResponse;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.env")
class AuthServiceIntegrationTest {

    @BeforeAll
    static void loadEnv() {
        Dotenv dotenv = Dotenv.configure().directory("src/test/resources").ignoreIfMissing().load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UsuarioDataAccess usuarioDataAccess;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        usuarioDataAccess.buscarPorUsername("usuarioTest").ifPresentOrElse(
                usuario -> System.out.println("Usuario de prueba ya existente, no es necesario crearlo."),
                () -> {
                    String hashedPassword = passwordEncoder.encode("password123"); // 🔹 Encriptar la contraseña antes de guardarla
                    Usuario usuario = new Usuario("usuarioTest", hashedPassword, "test@example.com", Set.of("ROLE_USER"),true);
                    usuarioDataAccess.guardarUsuario(usuario);
                    System.out.println("Usuario de prueba creado exitosamente con contraseña encriptada.");
                }
        );
    }

    @Test
    void testEnvLoaded() {
        System.out.println("DATABASE_URL: " + System.getenv("DATABASE_URL"));
    }

    /**
     * Prueba de integración para verificar si `AuthService` permite autenticar un usuario con credenciales correctas.
     * Se realiza una solicitud POST a `/api/auth/login` con credenciales válidas.
     */
    @Test
    void authenticate_DeberiaRetornarTokenSiCredencialesSonValidas() {
        AuthRequest request = new AuthRequest("usuarioTest", "password123");

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity("/api/auth/login", request, AuthResponse.class);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getToken());
    }

    /**
     * Prueba de integración para verificar que `AuthService` rechaza autenticación con credenciales incorrectas.
     * Se espera un error 401 Unauthorized.
     */
    @Test
    void authenticate_DeberiaRetornar401SiCredencialesSonInvalidas() {
        AuthRequest request = new AuthRequest("usuarioTest", "wrongpassword");

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity("/api/auth/login", request, AuthResponse.class);

        assertEquals(401, response.getStatusCode().value());
    }
}
