package com.TiendaEnLinea.TiendaEnLinea.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoRequestDto {
    private  long id;
    private String productName;
    private String descriptions;
    private Integer stock;
    private BigDecimal price;

    private Long categoriaId;
}
