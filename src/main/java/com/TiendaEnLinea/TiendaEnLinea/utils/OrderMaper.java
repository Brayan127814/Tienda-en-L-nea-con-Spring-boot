package com.TiendaEnLinea.TiendaEnLinea.utils;

import com.TiendaEnLinea.TiendaEnLinea.Entity.Order;
import com.TiendaEnLinea.TiendaEnLinea.dtos.OrderItemsResponse;
import com.TiendaEnLinea.TiendaEnLinea.dtos.OrderResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMaper {

    public OrderResponse toResponse(Order order) {
        List<OrderItemsResponse> items = order.getItems().stream()
                .map(item -> OrderItemsResponse.builder()
                        .productoId(item.getProductoId())
                        .nameProduct(item.getProductName())
                        .cantidad(item.getCantidad())
                        .precioUnitario(item.getPrecioUnitario())
                        .subTota(item.getSubTotal())
                        .build()


                ).toList();

        return  OrderResponse.builder()

                .orderId(order.getId())
                .fecha(order.getCreateATt())
                .status(order.getStatus())
                .total(order.getTotal())
                .items(items)
                .build();
    }
}
