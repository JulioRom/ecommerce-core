package com.springproject.ecommercecore.service;

import com.springproject.ecommercecore.dataaccess.ProductoDataAccess;
import com.springproject.ecommercecore.exception.RecursoExistenteException;
import com.springproject.ecommercecore.exception.RecursoNoEncontradoException;
import com.springproject.ecommercecore.model.postgresql.Producto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoDataAccess productoDataAccess;

    /**
     * Listar todos los productos.
     */
    public List<Producto> listarProductos() {
        return productoDataAccess.listarTodos();
    }

    /**
     * Buscar un producto por su código.
     */
    public Producto buscarPorCodigo(String codigoProducto) {
        return productoDataAccess.buscarPorCodigo(codigoProducto)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto con código " + codigoProducto + " no encontrado"));
    }


    /**
     * Descontar stock de un producto.
     */
    @Transactional
    public Producto actualizarStock(String codigoProducto, int cantidad) {
        Producto productoExistente = productoDataAccess.buscarPorCodigo(codigoProducto)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto con código " + codigoProducto + " no encontrado"));

        return productoDataAccess.descontarStock(codigoProducto, cantidad);
    }


    /**
     * Guardar un nuevo producto.
     */
    public Producto guardarProducto(Producto producto) {
        if (productoDataAccess.buscarPorCodigo(producto.getCodigoProducto()).isPresent()) {
            throw new RecursoExistenteException("El código del producto ya existe");
        }
        return productoDataAccess.guardarProducto(producto);
    }


    /**
     * Actualizar un producto existente.
     */
    @Transactional
    public Producto actualizarProducto(String codigoProducto, Producto productoDetalles) {
        Producto productoExistente = productoDataAccess.buscarPorCodigo(codigoProducto)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto con código " + codigoProducto + " no encontrado"));

        productoExistente.setPrecioUnitario(productoDetalles.getPrecioUnitario());
        productoExistente.setStock(productoDetalles.getStock());

        return productoDataAccess.actualizarProducto(productoExistente);
    }


    /**
     * Eliminar un producto por su código.
     */
    @Transactional
    public void eliminarProducto(String codigoProducto) {
        Producto producto = productoDataAccess.buscarPorCodigo(codigoProducto)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto con código " + codigoProducto + " no encontrado"));

        productoDataAccess.eliminarProducto(producto);
    }
}
