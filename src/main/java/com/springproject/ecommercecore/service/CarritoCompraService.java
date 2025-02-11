package com.springproject.ecommercecore.service;

import com.springproject.ecommercecore.model.mongodb.CarritoCompra;
import com.springproject.ecommercecore.model.mongodb.ProductoCarrito;
import com.springproject.ecommercecore.repository.mongodb.CarritoCompraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarritoCompraService {
    private final CarritoCompraRepository carritoCompraRepository;

    // ✅ Agregar producto al carrito con cantidad específica
    public void agregarProducto(String idUsuario, ProductoCarrito nuevoProducto) {
        CarritoCompra carrito = carritoCompraRepository.findById(idUsuario).orElse(new CarritoCompra(idUsuario, idUsuario, new java.util.ArrayList<>(), LocalDateTime.now(),LocalDateTime.now()));

        // Buscar si el producto ya está en el carrito
        Optional<ProductoCarrito> productoExistente = carrito.getProductos().stream()
                .filter(p -> p.getCodigoProducto().equals(nuevoProducto.getCodigoProducto()))
                .findFirst();

        if (productoExistente.isPresent()) {
            // Si ya existe, actualizar cantidad
            productoExistente.get().setCantidad(productoExistente.get().getCantidad() + nuevoProducto.getCantidad());
        } else {
            // Si no existe, agregar nuevo producto
            carrito.getProductos().add(nuevoProducto);
        }

        carritoCompraRepository.save(carrito);
    }

    // ✅ Obtener el carrito de un usuario
    public Optional<CarritoCompra> obtenerCarrito(String idUsuario) {
        return carritoCompraRepository.findById(idUsuario);
    }

    // ✅ Eliminar un producto del carrito
    public boolean eliminarProducto(String idUsuario, String codigoProducto) {
        Optional<CarritoCompra> carritoOpt = carritoCompraRepository.findById(idUsuario);
        if (carritoOpt.isEmpty()) {
            return false;
        }

        CarritoCompra carrito = carritoOpt.get();
        boolean removed = carrito.getProductos().removeIf(p -> p.getCodigoProducto().equals(codigoProducto));

        if (removed) {
            carritoCompraRepository.save(carrito);
        }

        return removed;
    }

    // ✅ Vaciar el carrito completamente
    public boolean vaciarCarrito(String idUsuario) {
        if (carritoCompraRepository.existsById(idUsuario)) {
            carritoCompraRepository.deleteById(idUsuario);
            return true;
        }
        return false;
    }
}
