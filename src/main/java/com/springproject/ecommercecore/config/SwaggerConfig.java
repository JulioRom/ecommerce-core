package com.springproject.ecommercecore.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ecommerce API")
                        .version("1.0")
                        .description("Documentación de la API | Proyecto de e-commerce"))
                .servers(List.of(new Server().url("https://ecommerce-core-production.up.railway.app").description("Servidor de Producción")));
    }
}