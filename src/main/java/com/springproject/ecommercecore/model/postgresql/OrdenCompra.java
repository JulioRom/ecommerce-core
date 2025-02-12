package com.springproject.ecommercecore.model.postgresql;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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
    private LocalDateTime fechaEmision;
    private LocalDateTime fechaEntrega;
    private LocalDateTime fechaSolicitada;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoOrden estado;

    public enum EstadoOrden {
        PENDIENTE, PAGADO, ENVIADO, ENTREGADO, CANCELADO
    }
}
