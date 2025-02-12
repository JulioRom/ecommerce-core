package com.springproject.ecommercecore.controller;

import com.springproject.ecommercecore.model.mongodb.CarritoCompra;
import com.springproject.ecommercecore.model.mongodb.ProductoCarrito;
import com.springproject.ecommercecore.service.CarritoCompraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "Carrito de Compras", description = "Operaciones relacionadas con el carrito de compras")
@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CarritoCompraController {
    private final CarritoCompraService carritoCompraService;

    // Agregar producto con cantidad específica
    @Operation(summary = "Agregar producto al carrito", description = "Añade un producto al carrito de un usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto agregado correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping("/{idUsuario}")
    public ResponseEntity<String> agregarProducto(@PathVariable String idUsuario, @RequestBody ProductoCarrito producto) {
        carritoCompraService.agregarProducto(idUsuario, producto);
        return ResponseEntity.ok("Producto agregado o actualizado en el carrito.");
    }

    // Obtener carrito del usuario
    @Operation(summary = "Obtener el carrito de un usuario", description = "Devuelve el carrito de compras de un usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrito obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado")
    })
    @GetMapping("/{idUsuario}")
    public ResponseEntity<?> obtenerCarrito(@PathVariable String idUsuario) {
        Optional<CarritoCompra> carrito = carritoCompraService.obtenerCarrito(idUsuario);
        return carrito.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Eliminar un producto del carrito
    @Operation(summary = "Eliminar un producto del carrito", description = "Elimina un producto específico del carrito de un usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado en el carrito")
    })
    @DeleteMapping("/{idUsuario}/{codigoProducto}")
    public ResponseEntity<String> eliminarProducto(@PathVariable String idUsuario, @PathVariable String codigoProducto) {
        boolean eliminado = carritoCompraService.eliminarProducto(idUsuario, codigoProducto);
        return eliminado ? ResponseEntity.ok("Producto eliminado del carrito.") : ResponseEntity.notFound().build();
    }

    // Vaciar el carrito completamente
    @Operation(summary = "Vaciar el carrito de compras", description = "Elimina todos los productos del carrito de un usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrito vaciado correctamente"),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado")
    })
    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<String> vaciarCarrito(@PathVariable String idUsuario) {
        boolean vaciado = carritoCompraService.vaciarCarrito(idUsuario);
        return vaciado ? ResponseEntity.ok("Carrito vaciado.") : ResponseEntity.notFound().build();
    }
}
