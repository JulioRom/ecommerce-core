package com.springproject.ecommercecore.model.mongodb;

import lombok.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
@NoArgsConstructor
public class ProductoCarrito {
    private String codigoProducto;
    private int cantidad;
    private BigDecimal precioUnitario;

    // 🔹 Constructor adicional para permitir valores enteros como 3000
    public ProductoCarrito(String codigoProducto, int cantidad, double precio) {
        this.codigoProducto = codigoProducto;
        this.cantidad = cantidad;
        this.precioUnitario = BigDecimal.valueOf(precio).setScale(2, RoundingMode.HALF_UP);
    }
}
