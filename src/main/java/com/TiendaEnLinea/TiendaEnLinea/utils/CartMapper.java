package com.TiendaEnLinea.TiendaEnLinea.utils;

import com.TiendaEnLinea.TiendaEnLinea.Entity.Cart;
import com.TiendaEnLinea.TiendaEnLinea.dtos.CartItemResponse;
import com.TiendaEnLinea.TiendaEnLinea.dtos.CartResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CartMapper {

    public CartResponse toCartMapper(Cart cart) {
        List<CartItemResponse> itemsm = cart.getItems().stream().map(item -> {
            BigDecimal price = item.getProductos().getPrice();
            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(item.getQuantity()));


            return CartItemResponse.builder()
                    .itemId(item.getId())
                    .productoId(item.getProductos().getId())
                    .name(item.getProductos().getProductName())
                    .quantity(item.getQuantity())
                    .precioUnitario(price)
                    .subtotal(subtotal)

                    .build();
        }).toList();

        BigDecimal total = itemsm.stream().map(CartItemResponse::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        int totalItems = itemsm.stream().mapToInt(CartItemResponse::getQuantity).sum();

        return CartResponse.builder()
                .items(itemsm)
                .totalItems(totalItems)
                .totalPrice(total)
                .build();

    }
}
