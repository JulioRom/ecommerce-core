package com.springproject.ecommercecore.controller;

import com.springproject.ecommercecore.exception.RecursoNoEncontradoException;
import com.springproject.ecommercecore.model.postgresql.Producto;
import com.springproject.ecommercecore.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Operaciones relacionadas con los productos")
public class ProductoController {
    private final ProductoService productoService;

    @Operation(summary = "Listar todos los productos")
    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos() {
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @Operation(summary = "Obtener un producto por código")
    @GetMapping("/{codigoProducto}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable String codigoProducto) {
        Producto producto = productoService.buscarPorCodigo(codigoProducto);
        if (producto == null) {
            throw new RecursoNoEncontradoException("Producto con código " + codigoProducto + " no encontrado.");
        }
        return ResponseEntity.ok(producto);
    }

    @Operation(summary = "Agregar un nuevo producto")
    @PostMapping
    public ResponseEntity<Producto> agregarProducto(@Valid @RequestBody Producto producto) {
        if (producto.getCodigoProducto() == null || producto.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0 || producto.getStock() < 0) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(productoService.guardarProducto(producto));
    }

    @Operation(summary = "Actualizar un producto existente")
    @PutMapping("/{codigoProducto}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable String codigoProducto, @Valid @RequestBody Producto productoDetalles) {
        Producto productoExistente = productoService.buscarPorCodigo(codigoProducto);
        if (productoExistente == null) {
            throw new RecursoNoEncontradoException("No se puede actualizar. Producto con código " + codigoProducto + " no encontrado.");
        }
        return ResponseEntity.ok(productoService.actualizarProducto(codigoProducto, productoDetalles));
    }

    @Operation(summary = "Eliminar un producto")
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
