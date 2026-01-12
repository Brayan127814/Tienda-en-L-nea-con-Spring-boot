package com.TiendaEnLinea.TiendaEnLinea.dtos;


import com.TiendaEnLinea.TiendaEnLinea.Enum.OrderStates;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private  Long orderId;
    private LocalDateTime fecha;
    private OrderStates status;
    private BigDecimal total;
    private List<OrderItemsResponse> items;
}
