package com.springproject.ecommercecore.service;

import com.springproject.ecommercecore.exception.RecursoNoEncontradoException;
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
        // Obtener el carrito del usuario
        Optional<CarritoCompra> carritoOpt = Optional.ofNullable(carritoCompraService.obtenerCarrito(idUsuario));

        if (carritoOpt.isEmpty() || carritoOpt.get().getProductos().isEmpty()) {
            throw new RecursoNoEncontradoException("El carrito está vacío. No se puede generar la compra.");
        }

        CarritoCompra carrito = carritoOpt.get();

        // Validar stock y crear detalles de compra
        List<DetalleCompra> detalles = carrito.getProductos().stream().map(productoCarrito -> {
            Producto producto = productoService.buscarPorCodigo(productoCarrito.getCodigoProducto());

            if (producto.getStock() < productoCarrito.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + productoCarrito.getCodigoProducto());
            }

            int totalDetalle = productoCarrito.getCantidad() * producto.getPrecioUnitario();
            productoService.actualizarStock(producto.getCodigoProducto(), productoCarrito.getCantidad());

            return new DetalleCompra(null, producto, productoCarrito.getCantidad(), totalDetalle, null);
        }).toList();

        // Crear la orden de compra
        OrdenCompra orden = new OrdenCompra();
        orden.setFechaEmision(new Date());
        orden.setFechaEntrega(new Date());  // 🚨 Ajustar si es necesario
        orden.setFechaSolicitada(new Date());  // 🚨 Ajustar si es necesario
        orden = ordenCompraRepository.save(orden);

        // Asociar detalles a la orden y guardarlos en la base de datos
        for (DetalleCompra detalle : detalles) {
            detalle.setOrdenCompra(orden);
            detalleCompraRepository.save(detalle);
        }

        // Vaciar el carrito después de procesar la compra
        carritoCompraService.vaciarCarrito(idUsuario);

        return "Compra realizada con éxito. ID de Orden: " + orden.getId();
    }
}

