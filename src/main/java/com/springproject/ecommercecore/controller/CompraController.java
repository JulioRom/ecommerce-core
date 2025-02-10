package com.springproject.ecommercecore.controller;

import com.springproject.ecommercecore.service.CompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compra")
@RequiredArgsConstructor
public class CompraController {
    private final CompraService compraService;

    // Generar una compra a partir del carrito de un usuario
    @PostMapping("/{idUsuario}")
    public ResponseEntity<String> generarCompra(@PathVariable String idUsuario) {
        return ResponseEntity.ok(compraService.generarCompra(idUsuario));
    }
}
