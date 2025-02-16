package com.springproject.ecommercecore.dataaccess;

import com.springproject.ecommercecore.model.postgresql.Producto;
import com.springproject.ecommercecore.repository.postgresql.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductoDataAccess {

    private final ProductoRepository productoRepository;

    /**
     * Listar todos los productos.
     */
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    /**
     * Buscar un producto por código.
     */
    public Optional<Producto> buscarPorCodigo(String codigoProducto) {
        return productoRepository.findByCodigoProducto(codigoProducto);
    }

    /**
     * Guardar un nuevo producto.
     */
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    /**
     * Descontar stock de un producto.
     */
    @Transactional
    public boolean descontarStock(String codigoProducto, int cantidad) {
        return productoRepository.descontarStock(codigoProducto, cantidad) > 0;
    }

    /**
     * Actualizar un producto existente.
     */
    @Transactional
    public Producto actualizarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    /**
     * Eliminar un producto por su código.
     */
    @Transactional
    public void eliminarProducto(Producto producto) {
        productoRepository.delete(producto);
    }
}
