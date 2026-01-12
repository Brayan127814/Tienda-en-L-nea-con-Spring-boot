package com.TiendaEnLinea.TiendaEnLinea.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequest {

    private  long productoId;
    private  Integer cantidad;
}
