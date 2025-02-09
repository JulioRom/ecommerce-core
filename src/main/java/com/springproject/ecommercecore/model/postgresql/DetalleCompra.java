package com.springproject.ecommercecore.model.postgresql;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "detalles_compra")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "codigo_producto", referencedColumnName = "codigoProducto")
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Integer totalDetalle;

    @ManyToOne
    @JoinColumn(name = "id_orden")
    private OrdenCompra ordenCompra;
}
