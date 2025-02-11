package com.springproject.ecommercecore.controller;

import com.springproject.ecommercecore.exception.RecursoNoEncontradoException;
import com.springproject.ecommercecore.model.mongodb.CarritoCompra;
import com.springproject.ecommercecore.model.mongodb.ProductoCarrito;
import com.springproject.ecommercecore.service.CarritoCompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoCompraController {
    private final CarritoCompraService carritoCompraService;

    // ✅ Agregar producto con cantidad específica
    @PostMapping("/{idUsuario}")
    public ResponseEntity<Void> agregarProducto(@PathVariable String idUsuario, @RequestBody ProductoCarrito producto) {
        carritoCompraService.agregarProducto(idUsuario, producto);
        return ResponseEntity.ok().build();
    }

    // ✅ Obtener carrito del usuario
    @GetMapping("/{idUsuario}")
    public ResponseEntity<CarritoCompra> obtenerCarrito(@PathVariable String idUsuario) {
        CarritoCompra carrito = carritoCompraService.obtenerCarrito(idUsuario);
        return ResponseEntity.ok(carrito);
    }

    // ✅ Eliminar un producto del carrito
    @DeleteMapping("/{idUsuario}/{codigoProducto}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable String idUsuario, @PathVariable String codigoProducto) {
        carritoCompraService.eliminarProducto(idUsuario, codigoProducto);
        return ResponseEntity.noContent().build();
    }

    // ✅ Vaciar el carrito completamente
    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable String idUsuario) {
        carritoCompraService.vaciarCarrito(idUsuario);
        return ResponseEntity.noContent().build();
    }
}
