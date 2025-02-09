package com.springproject.ecommercecore.model.postgresql;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "ordenes_compra")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdenCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrega;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSolicitada;
}
