package com.springproject.ecommercecore.controller;

import com.springproject.ecommercecore.service.CompraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compra")
@RequiredArgsConstructor
@Tag(name = "Compras", description = "Operaciones de compra")
public class CompraController {
    private final CompraService compraService;

    @Operation(summary = "Generar una compra desde el carrito de un usuario")
    @PostMapping("/{idUsuario}")
    public ResponseEntity<String> generarCompra(@PathVariable String idUsuario) {
        return ResponseEntity.ok(compraService.generarCompra(idUsuario));
    }
}
