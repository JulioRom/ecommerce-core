package com.springproject.ecommercecore.service;

import com.springproject.ecommercecore.exception.RecursoNoEncontradoException;
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
    public Producto buscarPorCodigo(String codigoProducto) {
        return Optional.ofNullable(productoRepository.findByCodigoProducto(codigoProducto))
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado: " + codigoProducto));
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
    public Producto actualizarProducto(String codigoProducto, Producto productoDetalles) {
        Producto productoExistente = buscarPorCodigo(codigoProducto);

        productoExistente.setPrecioUnitario(productoDetalles.getPrecioUnitario());
        productoExistente.setStock(productoDetalles.getStock());

        return productoRepository.save(productoExistente);
    }

    // ✅ Eliminar un producto
    public void eliminarProducto(String codigoProducto) {
        Producto producto = buscarPorCodigo(codigoProducto);
        productoRepository.delete(producto);
    }
}
