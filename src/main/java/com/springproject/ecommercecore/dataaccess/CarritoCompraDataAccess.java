package com.springproject.ecommercecore.dataaccess;

import com.springproject.ecommercecore.model.mongodb.CarritoCompra;
import com.springproject.ecommercecore.repository.mongodb.CarritoCompraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CarritoCompraDataAccess {

    private final CarritoCompraRepository carritoCompraRepository;

    /**
     * Buscar un carrito por su ID o por ID de usuario.
     */
    public Optional<CarritoCompra> buscarPorIdOUsuario(String identificador) {
        // Sí es un ID válido de MongoDB, buscar por ID de carrito
        if (identificador.matches("^[a-fA-F0-9]{24}$")) { // Valida formato de ObjectId
            return carritoCompraRepository.findById(identificador);
        }
        // Si no es un ObjectId válido, buscar por idUsuario
        return carritoCompraRepository.findByIdUsuario(identificador);
    }

    /**
     * Guardar o actualizar un carrito.
     */
    public CarritoCompra guardarCarrito(CarritoCompra carrito) {
        return carritoCompraRepository.save(carrito);
    }

    /**
     * Eliminar un carrito por el ID del usuario.
     */
    @Transactional
    public void eliminarCarritoPorUsuarioId(String idUsuario) {
        carritoCompraRepository.deleteByIdUsuario(idUsuario);
    }
}
