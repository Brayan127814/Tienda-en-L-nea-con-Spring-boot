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
public class OrderItemsResponse {
    private long productoId;
    private String nameProduct;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subTota;
}

