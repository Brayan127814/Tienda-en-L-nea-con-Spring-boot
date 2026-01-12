package com.TiendaEnLinea.TiendaEnLinea.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {

    private Long itemId;
    private Long productoId;
    private String name;
    private Integer quantity;
    private BigDecimal precioUnitario;
    private  BigDecimal subtotal;
}
