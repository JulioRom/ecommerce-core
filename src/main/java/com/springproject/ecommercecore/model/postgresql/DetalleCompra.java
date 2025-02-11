package com.springproject.ecommercecore.model.postgresql;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "codigo_producto", referencedColumnName = "codigoProducto")
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false)
    private Integer totalDetalle;

    @ManyToOne
    @JoinColumn(name = "id_orden")
    private OrdenCompra ordenCompra;
}
