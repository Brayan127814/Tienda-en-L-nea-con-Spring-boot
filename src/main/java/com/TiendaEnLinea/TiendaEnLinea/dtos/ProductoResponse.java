package com.TiendaEnLinea.TiendaEnLinea.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductoResponse {
    private  Long id;
    private String productName;
    private String descriptions;
    private Integer stock;
    private BigDecimal price;

    private  Long categoryId;
    private  String categoryName;
}
