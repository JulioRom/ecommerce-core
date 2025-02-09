package com.springproject.ecommercecore.model.postgresql;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String codigoProducto;

    @Column(nullable = false)
    private Integer precioUnitario;

    @Column(nullable = false)
    private Integer stock;
}

