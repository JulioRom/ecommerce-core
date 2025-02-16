package com.springproject.ecommercecore.controller;

import com.springproject.ecommercecore.model.postgresql.Producto;
import com.springproject.ecommercecore.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Productos", description = "Operaciones relacionadas con los productos del ecommerce")
@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProductoController {
    private final ProductoService productoService;

    // Obtener todos los productos
    @Operation(summary = "Listar todos los productos", description = "Devuelve una lista de todos los productos disponibles.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos() {
        return ResponseEntity.ok(productoService.listarProductos());
    }

    // Obtener producto por código
    @Operation(summary = "Obtener un producto por código", description = "Devuelve un producto específico según su código.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{codigoProducto}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable String codigoProducto) {
        return productoService.buscarPorCodigo(codigoProducto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Agregar un producto (POST)
    @Operation(summary = "Agregar un nuevo producto", description = "Crea un nuevo producto en la base de datos.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Producto> agregarProducto(@Valid @RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.guardarProducto(producto));
    }

    //Actualizar un producto (PUT)
    @Operation(summary = "Actualizar un producto", description = "Modifica los detalles de un producto existente.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud")
    })
    @PutMapping("/{codigoProducto}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable String codigoProducto, @Valid @RequestBody Producto productoDetalles) {
        Producto productoActualizado = productoService.actualizarProducto(codigoProducto, productoDetalles);
        return ResponseEntity.ok(productoActualizado);
    }

    // Eliminar un producto (DELETE)
    @Operation(summary = "Eliminar un producto", description = "Elimina un producto de la base de datos según su código.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{codigoProducto}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable String codigoProducto) {
        productoService.eliminarProducto(codigoProducto);
        return ResponseEntity.noContent().build();
    }


}

