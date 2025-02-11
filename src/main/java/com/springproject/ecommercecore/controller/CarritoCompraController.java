package com.springproject.ecommercecore.controller;

import com.springproject.ecommercecore.model.mongodb.CarritoCompra;
import com.springproject.ecommercecore.model.mongodb.ProductoCarrito;
import com.springproject.ecommercecore.service.CarritoCompraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
@Tag(name = "Carrito de Compra", description = "Operaciones sobre el carrito de compras")
public class CarritoCompraController {
    private final CarritoCompraService carritoCompraService;

    @Operation(summary = "Agregar un producto al carrito")
    @PostMapping("/{idUsuario}")
    public ResponseEntity<Void> agregarProducto(@PathVariable String idUsuario, @RequestBody ProductoCarrito producto) {
        carritoCompraService.agregarProducto(idUsuario, producto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Obtener el carrito de un usuario")
    @GetMapping("/{idUsuario}")
    public ResponseEntity<CarritoCompra> obtenerCarrito(@PathVariable String idUsuario) {
        CarritoCompra carrito = carritoCompraService.obtenerCarrito(idUsuario);
        return ResponseEntity.ok(carrito);
    }

    @Operation(summary = "Eliminar un producto del carrito")
    @DeleteMapping("/{idUsuario}/{codigoProducto}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable String idUsuario, @PathVariable String codigoProducto) {
        carritoCompraService.eliminarProducto(idUsuario, codigoProducto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Vaciar el carrito de un usuario")
    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable String idUsuario) {
        carritoCompraService.vaciarCarrito(idUsuario);
        return ResponseEntity.noContent().build();
    }
}
