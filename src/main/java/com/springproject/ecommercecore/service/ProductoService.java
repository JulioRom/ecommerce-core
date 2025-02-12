package com.springproject.ecommercecore.service;

import com.springproject.ecommercecore.model.postgresql.Producto;
import com.springproject.ecommercecore.repository.postgresql.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoService {
    private final ProductoRepository productoRepository;

    // Obtener todos los productos
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    // Buscar producto por código
    public Optional<Producto> buscarPorCodigo(String codigoProducto) {
        return productoRepository.findByCodigoProducto(codigoProducto);
    }

    // Restar stock de un producto
    public void actualizarStock(String codigoProducto, int cantidad) {
        Producto producto = productoRepository.findByCodigoProducto(codigoProducto)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        if (producto.getStock() < cantidad) {
            throw new IllegalArgumentException("Stock insuficiente para el producto: " + codigoProducto);
        }

        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);
    }


    //  Guardar un nuevo producto
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    //  Actualizar un producto existente
    public Producto actualizarProducto(String codigoProducto, Producto productoDetalles) {
        Producto productoExistente = productoRepository.findByCodigoProducto(codigoProducto)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        productoExistente.setPrecioUnitario(productoDetalles.getPrecioUnitario());
        productoExistente.setStock(productoDetalles.getStock());

        return productoRepository.save(productoExistente);
    }

    //  Eliminar un producto
    public void eliminarProducto(String codigoProducto) {
        Producto producto = productoRepository.findByCodigoProducto(codigoProducto)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        productoRepository.delete(producto);
    }

}
