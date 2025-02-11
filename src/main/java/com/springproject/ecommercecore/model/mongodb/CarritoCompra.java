package com.springproject.ecommercecore.model.mongodb;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.time.LocalDateTime;

@Document(collection = "carritos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarritoCompra {
    @Id
    private String id;
    private String idUsuario;
    private List<ProductoCarrito> productos;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
