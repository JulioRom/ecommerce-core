package com.springproject.ecommercecore.controller;

import com.springproject.ecommercecore.model.postgresql.Producto;
import com.springproject.ecommercecore.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {
    private final ProductoService productoService;

    // Obtener todos los productos
    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos() {
        return ResponseEntity.ok(productoService.listarProductos());
    }

    // Obtener producto por código
    @GetMapping("/{codigoProducto}")
    public ResponseEntity<Optional<Producto>> obtenerProducto(@PathVariable String codigoProducto) {
        return ResponseEntity.ok(productoService.buscarPorCodigo(codigoProducto));
    }

    // ✅ Agregar un producto (POST)
    @PostMapping
    public ResponseEntity<Producto> agregarProducto(@RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.guardarProducto(producto));
    }

    // ✅ Actualizar un producto (PUT)
    @PutMapping("/{codigoProducto}")
    public ResponseEntity<Optional<Producto>> actualizarProducto(@PathVariable String codigoProducto, @RequestBody Producto productoDetalles) {
        return ResponseEntity.ok(productoService.actualizarProducto(codigoProducto, productoDetalles));
    }

    // ✅ Eliminar un producto (DELETE)
    @DeleteMapping("/{codigoProducto}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable String codigoProducto) {
        productoService.eliminarProducto(codigoProducto);
        return ResponseEntity.noContent().build();
    }

}

