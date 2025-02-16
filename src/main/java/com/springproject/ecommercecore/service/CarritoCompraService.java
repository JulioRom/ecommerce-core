package com.springproject.ecommercecore.service;

import com.springproject.ecommercecore.dataaccess.CarritoCompraDataAccess;
import com.springproject.ecommercecore.model.mongodb.CarritoCompra;
import com.springproject.ecommercecore.model.mongodb.ProductoCarrito;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarritoCompraService {

    private final CarritoCompraDataAccess carritoCompraDataAccess;

    /**
     * Agregar un producto al carrito de compras.
     */
    @Transactional
    public void agregarProducto(String idUsuario, ProductoCarrito nuevoProducto) {
        CarritoCompra carrito = carritoCompraDataAccess.buscarPorIdOUsuario(idUsuario)
                .orElseGet(() -> crearCarrito(idUsuario));

        Optional<ProductoCarrito> productoExistente = carrito.getProductos().stream()
                .filter(p -> p.getCodigoProducto().equals(nuevoProducto.getCodigoProducto()))
                .findFirst();

        if (productoExistente.isPresent()) {
            productoExistente.get().setCantidad(productoExistente.get().getCantidad() + nuevoProducto.getCantidad());
        } else {
            carrito.getProductos().add(nuevoProducto);
        }

        carrito.setUpdatedAt(LocalDateTime.now());
        carritoCompraDataAccess.guardarCarrito(carrito);
    }

    /**
     * Obtener un carrito por ID o por ID de usuario.
     */
    public Optional<CarritoCompra> obtenerCarritoPorIdOUsuario(String identificador) {
        return carritoCompraDataAccess.buscarPorIdOUsuario(identificador);
    }

    /**
     * Eliminar un producto específico del carrito.
     */
    @Transactional
    public boolean removerProductoDelCarrito(String idUsuario, String codigoProducto) {
        Optional<CarritoCompra> carritoOptional = carritoCompraDataAccess.buscarPorIdOUsuario(idUsuario);

        if (carritoOptional.isPresent()) {
            CarritoCompra carrito = carritoOptional.get();
            boolean removed = carrito.getProductos().removeIf(p -> p.getCodigoProducto().equals(codigoProducto));

            if (removed) {
                carrito.setUpdatedAt(LocalDateTime.now());
                carritoCompraDataAccess.guardarCarrito(carrito);
            }
            return removed;
        }
        return false;
    }

    /**
     * Vaciar el carrito de un usuario.
     */
    @Transactional
    public boolean vaciarCarrito(String idUsuario) {
        carritoCompraDataAccess.eliminarCarritoPorUsuarioId(idUsuario);
        return true;
    }

    /**
     * Crear un nuevo carrito para el usuario.
     */
    private CarritoCompra crearCarrito(String idUsuario) {
        CarritoCompra nuevoCarrito = new CarritoCompra(
                null, idUsuario, new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()
        );
        return carritoCompraDataAccess.guardarCarrito(nuevoCarrito);
    }
}
