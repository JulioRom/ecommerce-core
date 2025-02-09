package com.springproject.ecommercecore.model.mongodb;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoCarrito {
    private String codigoProducto;
    private int cantidad;
}

