package com.springproject.ecommercecore.controller;

import com.springproject.ecommercecore.model.mongodb.CarritoCompra;
import com.springproject.ecommercecore.model.mongodb.ProductoCarrito;
import com.springproject.ecommercecore.service.CarritoCompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoCompraController {
    private final CarritoCompraService carritoCompraService;

    // ✅ Agregar producto con cantidad específica
    @PostMapping("/{idUsuario}")
    public ResponseEntity<String> agregarProducto(@PathVariable String idUsuario, @RequestBody ProductoCarrito producto) {
        carritoCompraService.agregarProducto(idUsuario, producto);
        return ResponseEntity.ok("Producto agregado o actualizado en el carrito.");
    }

    // ✅ Obtener carrito del usuario
    @GetMapping("/{idUsuario}")
    public ResponseEntity<?> obtenerCarrito(@PathVariable String idUsuario) {
        Optional<CarritoCompra> carrito = carritoCompraService.obtenerCarrito(idUsuario);
        return carrito.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Eliminar un producto del carrito
    @DeleteMapping("/{idUsuario}/{codigoProducto}")
    public ResponseEntity<String> eliminarProducto(@PathVariable String idUsuario, @PathVariable String codigoProducto) {
        boolean eliminado = carritoCompraService.eliminarProducto(idUsuario, codigoProducto);
        return eliminado ? ResponseEntity.ok("Producto eliminado del carrito.") : ResponseEntity.notFound().build();
    }

    // ✅ Vaciar el carrito completamente
    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<String> vaciarCarrito(@PathVariable String idUsuario) {
        boolean vaciado = carritoCompraService.vaciarCarrito(idUsuario);
        return vaciado ? ResponseEntity.ok("Carrito vaciado.") : ResponseEntity.notFound().build();
    }
}
