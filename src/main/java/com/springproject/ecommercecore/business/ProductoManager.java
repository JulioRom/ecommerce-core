package com.springproject.ecommercecore.business;

import com.springproject.ecommercecore.model.postgresql.Producto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductoManager {

    /**
     *  Validar stock antes de descontar
     */
    public void validarStock(Producto producto, int cantidad) {
        if (producto.getStock() < cantidad) {
            throw new IllegalArgumentException("Stock insuficiente para el producto: " + producto.getCodigoProducto());
        }
    }

    /**
     *  Actualizar los datos de un producto existente
     */
    public void actualizarDatosProducto(Producto producto, Producto productoDetalles) {
        producto.setPrecioUnitario(productoDetalles.getPrecioUnitario());
        producto.setStock(productoDetalles.getStock());
    }

    /**
     *  Aplicar reducción de stock a un producto
     */
    public void reducirStock(Producto producto, int cantidad) {
        validarStock(producto, cantidad);
        producto.setStock(producto.getStock() - cantidad);
    }
}
