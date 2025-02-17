package com.springproject.ecommercecore.service;

import com.springproject.ecommercecore.dataaccess.CarritoCompraDataAccess;
import com.springproject.ecommercecore.model.mongodb.CarritoCompra;
import com.springproject.ecommercecore.model.mongodb.ProductoCarrito;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarritoCompraService {

    private final CarritoCompraDataAccess carritoCompraDataAccess;

    /**
     * Agregar un producto al carrito de compras.
     */
    @Transactional
    public void agregarProducto(String idUsuario, ProductoCarrito productoCarrito) {
        Optional<CarritoCompra> carritoOptional = carritoCompraDataAccess.buscarPorIdOUsuario(idUsuario);

        CarritoCompra carrito = carritoOptional.orElseGet(() -> {
            CarritoCompra nuevoCarrito = new CarritoCompra(null, idUsuario, new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());
            return carritoCompraDataAccess.guardarCarrito(nuevoCarrito);
        });

        if (carrito.getProductos() == null) {
            carrito.setProductos(new ArrayList<>());
        } else if (!(carrito.getProductos() instanceof ArrayList)) {
            carrito.setProductos(new ArrayList<>(carrito.getProductos()));
        }

        carrito.getProductos().add(productoCarrito);
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
            List<ProductoCarrito> productosModificables = new ArrayList<>(carrito.getProductos()); // 🔹 Convertir a `ArrayList`

            boolean removed = productosModificables.removeIf(p -> p.getCodigoProducto().equals(codigoProducto));

            if (removed) {
                carrito.setProductos(productosModificables);
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
        if (carritoCompraDataAccess.buscarPorIdOUsuario(idUsuario).isEmpty()) {
            return false; // Retorna false si el carrito no existe
        }
        carritoCompraDataAccess.eliminarCarritoPorUsuarioId(idUsuario);
        return true;
    }


    /**
     * Crear un nuevo carrito para el usuario.
     */
    private CarritoCompra crearCarrito(String idUsuario) {
        return carritoCompraDataAccess.guardarCarrito(new CarritoCompra(
                null, idUsuario, new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()
        ));
    }

}
