package com.springproject.ecommercecore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Pruebas", description = "Endpoints de prueba para verificar CORS y conectividad")
public class CorsTestController {

    @Operation(summary = "Probar conexión con el backend", description = "Devuelve un mensaje si el backend está accesible.")
    @GetMapping("/test")
    public String test() {
        return "CORS funciona correctamente 🚀";
    }

    @Operation(summary = "Verificar CORS (OPTIONS)", description = "Devuelve un mensaje si el método OPTIONS es permitido por CORS.")
    @RequestMapping(value = "/test", method = RequestMethod.OPTIONS)
    public String handleOptions() {
        return "OPTIONS permitido";
    }
}

