package com.springproject.ecommercecore.service;

import com.springproject.ecommercecore.exception.RecursoNoEncontradoException;
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

    // Agregar producto al carrito con cantidad específica
    public void agregarProducto(String idUsuario, ProductoCarrito nuevoProducto) {
        CarritoCompra carrito = carritoCompraRepository.findById(idUsuario).orElse(new CarritoCompra(null, idUsuario, new java.util.ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()));


        Optional<ProductoCarrito> productoExistente = carrito.getProductos().stream()
                .filter(p -> p.getCodigoProducto().equals(nuevoProducto.getCodigoProducto()))
                .findFirst();

        if (productoExistente.isPresent()) {
            productoExistente.get().setCantidad(productoExistente.get().getCantidad() + nuevoProducto.getCantidad());
        } else {
            carrito.getProductos().add(nuevoProducto);
        }

        carritoCompraRepository.save(carrito);
    }

    // Obtener el carrito de un usuario
    public CarritoCompra obtenerCarrito(String idUsuario) {
        return carritoCompraRepository.findById(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException("Carrito de compras no encontrado para usuario: " + idUsuario));
    }

    // Eliminar un producto del carrito
    public void eliminarProducto(String idUsuario, String codigoProducto) {
        CarritoCompra carrito = obtenerCarrito(idUsuario);
        boolean removed = carrito.getProductos().removeIf(p -> p.getCodigoProducto().equals(codigoProducto));

        if (!removed) {
            throw new RecursoNoEncontradoException("Producto no encontrado en el carrito del usuario: " + idUsuario);
        }

        carritoCompraRepository.save(carrito);
    }

    // Vaciar el carrito completamente
    public void vaciarCarrito(String idUsuario) {
        if (!carritoCompraRepository.existsById(idUsuario)) {
            throw new RecursoNoEncontradoException("Carrito de compras no encontrado para usuario: " + idUsuario);
        }

        carritoCompraRepository.deleteById(idUsuario);
    }
}
