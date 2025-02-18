package com.springproject.ecommercecore.controller;

import com.springproject.ecommercecore.model.postgresql.OrdenCompra;
import com.springproject.ecommercecore.service.CompraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/compras")
@Tag(name = "Compras", description = "Operaciones relacionadas con la gestión de compras")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CompraController {

    private final CompraService compraService;

    /**
     * 🔹 Crear una nueva orden de compra
     */
    @Operation(summary = "Generar una nueva compra", description = "Crea una orden de compra para un usuario específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Orden de compra creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping("/{idUsuario}")
    public ResponseEntity<OrdenCompra> generarCompra(
            @PathVariable Integer idUsuario,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaSolicitada) {
        OrdenCompra orden = compraService.generarOrden(idUsuario, fechaSolicitada);
        return ResponseEntity.ok(orden);
    }

    /**
     * 🔹 Obtener órdenes de un usuario por ID
     */
    @Operation(summary = "Obtener órdenes de un usuario con id", description = "Lista todas las órdenes de compra asociadas a un usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Órdenes obtenidas correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/usuario/id/{idUsuario}")
    public ResponseEntity<List<OrdenCompra>> obtenerOrdenesPorUsuarioId(@PathVariable Long idUsuario) {
        List<OrdenCompra> ordenes = compraService.obtenerOrdenesPorUsuarioId(idUsuario);
        return ordenes.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(ordenes);
    }

    /**
     * 🔹 Obtener todas las órdenes de un usuario
     */
    @Operation(summary = "Obtener órdenes de un usuario con username", description = "Lista todas las órdenes de compra asociadas a un usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Órdenes obtenidas correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/usuario/username/{username}")
    public ResponseEntity<List<OrdenCompra>> obtenerOrdenesPorUsername(@PathVariable String username) {
        List<OrdenCompra> ordenes = compraService.obtenerOrdenesPorUsername(username);
        return ordenes.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(ordenes);
    }

    /**
     * 🔹 Obtener una orden de compra por su ID
     */
    @Operation(summary = "Obtener una orden de compra por ID", description = "Recupera una orden de compra específica por su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orden de compra encontrada"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{idOrden}")
    public ResponseEntity<Optional<OrdenCompra>> obtenerOrdenPorId(@PathVariable Integer idOrden) {
        return ResponseEntity.ok(compraService.obtenerOrdenPorId(idOrden));
    }

    /**
     * 🔹 Actualizar el estado de una orden de compra
     */
    @Operation(summary = "Actualizar estado de una orden", description = "Modifica el estado de una orden de compra existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado de la orden actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{idOrden}/estado")
    public ResponseEntity<OrdenCompra> actualizarEstadoOrden(
            @PathVariable Integer idOrden,
            @RequestParam OrdenCompra.EstadoOrden nuevoEstado) {
        return ResponseEntity.ok(compraService.actualizarEstadoOrden(idOrden, nuevoEstado));
    }

    /**
     * 🔹 Cancelar una orden de compra
     */
    @Operation(summary = "Cancelar una orden de compra", description = "Permite a un usuario cancelar una orden de compra.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orden cancelada correctamente"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/{idOrden}/cancelar")
    public ResponseEntity<String> cancelarOrden(@PathVariable Integer idOrden) {
        try {
            compraService.cancelarOrden(idOrden);
            return ResponseEntity.ok("Orden cancelada correctamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Error: " + e.getMessage());
        }
    }
}
