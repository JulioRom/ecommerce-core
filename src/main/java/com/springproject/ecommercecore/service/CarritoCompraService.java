package com.springproject.ecommercecore.service;

import com.springproject.ecommercecore.business.CarritoCompraManager;
import com.springproject.ecommercecore.model.mongodb.CarritoCompra;
import com.springproject.ecommercecore.model.mongodb.ProductoCarrito;
import com.springproject.ecommercecore.repository.mongodb.CarritoCompraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarritoCompraService {

    private final CarritoCompraRepository carritoCompraRepository;
    private final CarritoCompraManager carritoCompraManager;

    /**
     * 🔹 Agregar producto al carrito con cantidad específica.
     */
    public void agregarProducto(String idUsuario, ProductoCarrito nuevoProducto) {
        CarritoCompra carrito = carritoCompraRepository.findById(idUsuario)
                .orElseGet(() -> carritoCompraManager.crearCarrito(idUsuario));

        carritoCompraManager.agregarProducto(carrito, nuevoProducto);
        carritoCompraRepository.save(carrito);
    }

    /**
     * 🔹 Obtener el carrito de un usuario.
     */
    public Optional<CarritoCompra> obtenerCarrito(String idUsuario) {
        return carritoCompraRepository.findById(idUsuario);
    }

    /**
     * 🔹 Eliminar un producto del carrito.
     */
    public boolean eliminarProducto(String idUsuario, String codigoProducto) {
        Optional<CarritoCompra> carritoOpt = carritoCompraRepository.findById(idUsuario);
        if (carritoOpt.isEmpty()) {
            return false;
        }

        CarritoCompra carrito = carritoOpt.get();
        boolean removed = carritoCompraManager.eliminarProducto(carrito, codigoProducto);

        if (removed) {
            carritoCompraRepository.save(carrito);
        }

        return removed;
    }

    /**
     * 🔹 Vaciar el carrito completamente.
     */
    public boolean vaciarCarrito(String idUsuario) {
        if (carritoCompraRepository.existsById(idUsuario)) {
            carritoCompraRepository.deleteById(idUsuario);
            return true;
        }
        return false;
    }
}
