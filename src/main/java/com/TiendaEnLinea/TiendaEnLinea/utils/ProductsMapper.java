package com.TiendaEnLinea.TiendaEnLinea.utils;


import com.TiendaEnLinea.TiendaEnLinea.Entity.Productos;
import com.TiendaEnLinea.TiendaEnLinea.dtos.ProductoResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductsMapper {

    public ProductoResponse convertirADto(Productos p) {
        return ProductoResponse.builder()
                .id(p.getId())
                .productName(p.getProductName())
                .descriptions(p.getDescriptions())
                .stock(p.getStock())
                .price(p.getPrice())
                .imageUrl(p.getImageUrl())
                .categoryId(p.getCategoria().getId())
                .categoryName(p.getCategoria().getCategoryName())

                .build();
    }
}
