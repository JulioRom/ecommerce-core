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
    public ResponseEntity<?> obtenerProducto(@PathVariable String codigoProducto) {
        Optional<Producto> producto = productoService.buscarPorCodigo(codigoProducto);
        return producto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Agregar un producto (POST)
    @PostMapping
    public ResponseEntity<Producto> agregarProducto(@RequestBody Producto producto) {
        Producto nuevoProducto = productoService.guardarProducto(producto);
        return ResponseEntity.ok(nuevoProducto);
    }

    // ✅ Actualizar un producto (PUT)
    @PutMapping("/{codigoProducto}")
    public ResponseEntity<?> actualizarProducto(@PathVariable String codigoProducto, @RequestBody Producto producto) {
        Optional<Producto> productoActualizado = productoService.actualizarProducto(codigoProducto, producto);
        return productoActualizado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Eliminar un producto (DELETE)
    @DeleteMapping("/{codigoProducto}")
    public ResponseEntity<String> eliminarProducto(@PathVariable String codigoProducto) {
        boolean eliminado = productoService.eliminarProducto(codigoProducto);
        return eliminado ? ResponseEntity.ok("Producto eliminado.") : ResponseEntity.notFound().build();
    }

}

