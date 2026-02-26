package com.TiendaEnLinea.TiendaEnLinea.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoriaResponseDto {
    private  long id;
    private  String categoryName;
    private  String descripcion;
}
