package com.springproject.ecommercecore.controller;

import com.springproject.ecommercecore.exception.RecursoNoEncontradoException;
import com.springproject.ecommercecore.model.postgresql.Producto;
import com.springproject.ecommercecore.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    // Obtener producto por código con manejo de error si no existe
    @GetMapping("/{codigoProducto}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable String codigoProducto) {
        Producto producto = productoService.buscarPorCodigo(codigoProducto);
        if (producto == null) {
            throw new RecursoNoEncontradoException("Producto con código " + codigoProducto + " no encontrado.");
        }
        return ResponseEntity.ok(producto);
    }

    // ✅ Agregar un producto con validación
    @PostMapping
    public ResponseEntity<Producto> agregarProducto(@Valid @RequestBody Producto producto) {
        if (producto.getCodigoProducto() == null || producto.getPrecioUnitario() <= 0 || producto.getStock() < 0) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(productoService.guardarProducto(producto));
    }

    // ✅ Actualizar un producto con validación
    @PutMapping("/{codigoProducto}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable String codigoProducto, @Valid @RequestBody Producto productoDetalles) {
        Producto productoExistente = productoService.buscarPorCodigo(codigoProducto);
        if (productoExistente == null) {
            throw new RecursoNoEncontradoException("No se puede actualizar. Producto con código " + codigoProducto + " no encontrado.");
        }
        return ResponseEntity.ok(productoService.actualizarProducto(codigoProducto, productoDetalles));
    }

    // ✅ Eliminar un producto con validación
    @DeleteMapping("/{codigoProducto}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable String codigoProducto) {
        Producto producto = productoService.buscarPorCodigo(codigoProducto);
        if (producto == null) {
            throw new RecursoNoEncontradoException("No se puede eliminar. Producto con código " + codigoProducto + " no encontrado.");
        }
        productoService.eliminarProducto(codigoProducto);
        return ResponseEntity.noContent().build();
    }
}
