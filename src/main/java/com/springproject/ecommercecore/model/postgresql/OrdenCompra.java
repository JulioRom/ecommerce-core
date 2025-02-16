package com.springproject.ecommercecore.model.postgresql;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "ordenes_compra")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdenCompra {

    public OrdenCompra(Usuario usuario, LocalDateTime fechaSolicitada) {
        this.usuario = usuario;
        this.fechaEmision = LocalDateTime.now();
        this.fechaSolicitada = fechaSolicitada;
        this.estado = EstadoOrden.PENDIENTE;
    }

    public enum EstadoOrden {
        PENDIENTE, PROCESANDO, ENVIADO, ENTREGADO, CANCELADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime fechaEmision;

    @Column(nullable = false)
    private LocalDateTime fechaSolicitada;

    private LocalDateTime fechaEntrega;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoOrden estado;
}
