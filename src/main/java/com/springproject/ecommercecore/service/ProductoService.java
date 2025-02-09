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
        return Optional.ofNullable(productoRepository.findByCodigoProducto(codigoProducto));
    }

    // Restar stock de un producto
    public boolean actualizarStock(String codigoProducto, int cantidad) {
        Producto producto = productoRepository.findByCodigoProducto(codigoProducto);
        if (producto != null && producto.getStock() >= cantidad) {
            producto.setStock(producto.getStock() - cantidad);
            productoRepository.save(producto);
            return true;
        }
        return false;
    }

    // ✅ Guardar un nuevo producto
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    // ✅ Actualizar un producto existente
    public Optional<Producto> actualizarProducto(String codigoProducto, Producto productoDetalles) {
        Producto productoExistente = productoRepository.findByCodigoProducto(codigoProducto);
        if (productoExistente == null) {
            return Optional.empty();
        }

        productoExistente.setPrecioUnitario(productoDetalles.getPrecioUnitario());
        productoExistente.setStock(productoDetalles.getStock());
        return Optional.of(productoRepository.save(productoExistente));
    }

    // ✅ Eliminar un producto
    public boolean eliminarProducto(String codigoProducto) {
        Producto producto = productoRepository.findByCodigoProducto(codigoProducto);
        if (producto == null) {
            return false;
        }

        productoRepository.delete(producto);
        return true;
    }
}
