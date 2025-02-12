package com.springproject.ecommercecore.business;

import com.springproject.ecommercecore.model.mongodb.CarritoCompra;
import com.springproject.ecommercecore.model.mongodb.ProductoCarrito;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class CarritoCompraManager {

    /**
     * 🔹 Agregar un producto al carrito con cantidad específica.
     */
    public void agregarProducto(CarritoCompra carrito, ProductoCarrito nuevoProducto) {
        Optional<ProductoCarrito> productoExistente = carrito.getProductos().stream()
                .filter(p -> p.getCodigoProducto().equals(nuevoProducto.getCodigoProducto()))
                .findFirst();

        if (productoExistente.isPresent()) {
            productoExistente.get().setCantidad(productoExistente.get().getCantidad() + nuevoProducto.getCantidad());
        } else {
            carrito.getProductos().add(nuevoProducto);
        }
    }

    /**
     * 🔹 Eliminar un producto del carrito.
     */
    public boolean eliminarProducto(CarritoCompra carrito, String codigoProducto) {
        return carrito.getProductos().removeIf(p -> p.getCodigoProducto().equals(codigoProducto));
    }

    /**
     * 🔹 Crear un nuevo carrito si no existe.
     */
    public CarritoCompra crearCarrito(String idUsuario) {
        return new CarritoCompra(idUsuario, idUsuario, new java.util.ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());
    }
}
