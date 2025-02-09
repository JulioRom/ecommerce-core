package com.springproject.ecommercecore.service;

import com.springproject.ecommercecore.repository.postgresql.DetalleCompraRepository;
import com.springproject.ecommercecore.repository.postgresql.OrdenCompraRepository;
import com.springproject.ecommercecore.model.mongodb.*;
import com.springproject.ecommercecore.model.postgresql.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompraService {
    private final CarritoCompraService carritoCompraService;
    private final ProductoService productoService;
    private final OrdenCompraRepository ordenCompraRepository;
    private final DetalleCompraRepository detalleCompraRepository;

    // ✅ Generar compra a partir del carrito con cantidades
    public String generarCompra(String idUsuario) {
        Optional<CarritoCompra> carritoOpt = carritoCompraService.obtenerCarrito(idUsuario);
        if (carritoOpt.isEmpty()) {
            return "El carrito de compras no existe.";
        }

        CarritoCompra carrito = carritoOpt.get();
        if (carrito.getProductos().isEmpty()) {
            return "El carrito está vacío.";
        }

        // Validar stock de los productos y preparar los detalles de compra
        List<DetalleCompra> detalles = carrito.getProductos().stream().map(productoCarrito -> {
            Optional<Producto> productoOpt = productoService.buscarPorCodigo(productoCarrito.getCodigoProducto());

            if (productoOpt.isEmpty()) {
                throw new RuntimeException("Producto no encontrado: " + productoCarrito.getCodigoProducto());
            }

            Producto producto = productoOpt.get();
            int cantidadSolicitada = productoCarrito.getCantidad();

            if (producto.getStock() < cantidadSolicitada) {
                throw new RuntimeException("Stock insuficiente para el producto: " + productoCarrito.getCodigoProducto());
            }

            int totalDetalle = cantidadSolicitada * producto.getPrecioUnitario();

            // Actualizar stock
            productoService.actualizarStock(producto.getCodigoProducto(), cantidadSolicitada);

            return new DetalleCompra(null, producto, cantidadSolicitada, totalDetalle, null);
        }).toList();

        // Crear la orden de compra
        OrdenCompra orden = new OrdenCompra();
        orden.setFechaEmision(new Date());
        orden.setFechaEntrega(new Date());
        orden.setFechaSolicitada(new Date());
        orden = ordenCompraRepository.save(orden);

        // Asociar detalles a la orden y guardarlos en la base de datos
        for (DetalleCompra detalle : detalles) {
            detalle.setOrdenCompra(orden);
            detalleCompraRepository.save(detalle);
        }

        // ✅ Vaciar el carrito después de procesar la compra
        carritoCompraService.vaciarCarrito(idUsuario);

        return "Compra realizada con éxito. ID de Orden: " + orden.getId();
    }
}

