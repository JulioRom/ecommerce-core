package com.springproject.ecommercecore.model.mongodb;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "carritos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarritoCompra {
    @Id
    private String idUsuario;
    private List<ProductoCarrito> productos;
}
