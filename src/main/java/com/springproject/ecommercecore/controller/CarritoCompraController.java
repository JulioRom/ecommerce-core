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

    @Operation(summary = "Agregar producto al carrito", description = "Añade un producto al carrito de un usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto agregado correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping("/{identificador}")
    public ResponseEntity<String> agregarProducto(@PathVariable String identificador, @RequestBody ProductoCarrito producto) {
        carritoCompraService.agregarProducto(identificador, producto);
        return ResponseEntity.ok("Producto agregado o actualizado en el carrito.");
    }

    @Operation(summary = "Obtener el carrito de un usuario", description = "Devuelve el carrito de compras de un usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrito obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado")
    })
    @GetMapping("/{identificador}")
    public ResponseEntity<CarritoCompra> obtenerCarrito(@PathVariable String identificador) {
        Optional<CarritoCompra> carrito = carritoCompraService.obtenerCarritoPorIdOUsuario(identificador);
        return carrito.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un producto del carrito", description = "Elimina un producto específico del carrito de un usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado en el carrito")
    })
    @DeleteMapping("/{identificador}/{codigoProducto}")
    public ResponseEntity<String> eliminarProducto(@PathVariable String identificador, @PathVariable String codigoProducto) {
        boolean eliminado = carritoCompraService.removerProductoDelCarrito(identificador, codigoProducto);
        return eliminado ? ResponseEntity.ok("Producto eliminado del carrito.") : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Vaciar el carrito de compras", description = "Elimina todos los productos del carrito de un usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrito vaciado correctamente"),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado")
    })
    @DeleteMapping("/{identificador}")
    public ResponseEntity<String> vaciarCarrito(@PathVariable String identificador) {
        boolean vaciado = carritoCompraService.vaciarCarrito(identificador);
        return vaciado ? ResponseEntity.ok("Carrito vaciado.") : ResponseEntity.notFound().build();
    }
}
