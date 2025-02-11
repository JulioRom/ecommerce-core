package com.springproject.ecommercecore.model.mongodb;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoCarrito {
    private String codigoProducto;
    private int cantidad;
    private BigDecimal precioUnitario;
}
