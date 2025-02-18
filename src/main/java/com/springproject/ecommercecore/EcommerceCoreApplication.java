package com.springproject.ecommercecore;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcommerceCoreApplication {
    public static void main(String[] args) {
        // Detectar si estamos en producción o desarrollo
        String activeProfile = System.getenv("SPRING_PROFILES_ACTIVE");

        if (activeProfile == null || activeProfile.equals("dev")) {
            // Solo cargar dotenv en desarrollo
            Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
            dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
            System.out.println("Cargando variables desde .env (Entorno de Desarrollo)");
        } else {
            System.out.println("Usando variables del sistema (Entorno de Producción)");
        }

        SpringApplication.run(EcommerceCoreApplication.class, args);
    }

}
