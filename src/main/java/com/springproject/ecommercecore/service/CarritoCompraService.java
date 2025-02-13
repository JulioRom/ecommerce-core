package com.springproject.ecommercecore.service;

import com.springproject.ecommercecore.model.mongodb.CarritoCompra;
import com.springproject.ecommercecore.model.mongodb.ProductoCarrito;
import com.springproject.ecommercecore.repository.mongodb.CarritoCompraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarritoCompraService {

    private final CarritoCompraRepository carritoCompraRepository;

    /**
     * Agregar un producto al carrito de compras
     */
    public void agregarProducto(String idUsuario, ProductoCarrito nuevoProducto) {
        CarritoCompra carrito = obtenerCarritoPorIdOUsuario(idUsuario)
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
        carritoCompraRepository.save(carrito);
    }

    /**
     * Obtener el carrito ya sea por Mongo ID o por idUsuario
     */
    public Optional<CarritoCompra> obtenerCarritoPorIdOUsuario(String identificador) {
        // Primero intentamos buscar por Mongo ID
        Optional<CarritoCompra> carrito = carritoCompraRepository.findById(identificador);

        // Si no existe por Mongo ID, intentamos buscar por el idUsuario
        if (carrito.isEmpty()) {
            carrito = carritoCompraRepository.findAll()
                    .stream()
                    .filter(c -> c.getIdUsuario().equals(identificador))
                    .findFirst();
        }

        return carrito;
    }

    /**
     * Eliminar un producto específico del carrito
     */
    public boolean removerProductoDelCarrito(String identificador, String codigoProducto) {
        Optional<CarritoCompra> carritoOptional = obtenerCarritoPorIdOUsuario(identificador);

        if (carritoOptional.isPresent()) {
            CarritoCompra carrito = carritoOptional.get();
            boolean removed = carrito.getProductos().removeIf(p -> p.getCodigoProducto().equals(codigoProducto));

            if (removed) {
                carrito.setUpdatedAt(LocalDateTime.now());
                carritoCompraRepository.save(carrito);
            }
            return removed;
        }
        return false;
    }

    /**
     * Crear un nuevo carrito para el usuario
     */
    private CarritoCompra crearCarrito(String idUsuario) {
        CarritoCompra nuevoCarrito = new CarritoCompra(
                null, idUsuario, new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()
        );
        return carritoCompraRepository.save(nuevoCarrito);
    }

    /**
     * Vaciar el carrito de un usuario
     */
    public boolean vaciarCarrito(String identificador) {
        Optional<CarritoCompra> carritoOptional = obtenerCarritoPorIdOUsuario(identificador);

        if (carritoOptional.isPresent()) {
            carritoCompraRepository.delete(carritoOptional.get());
            return true;
        }
        return false;
    }
}
