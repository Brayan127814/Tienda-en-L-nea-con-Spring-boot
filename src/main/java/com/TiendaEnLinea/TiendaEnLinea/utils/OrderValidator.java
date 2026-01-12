package com.TiendaEnLinea.TiendaEnLinea.utils;


import com.TiendaEnLinea.TiendaEnLinea.Entity.Order;
import com.TiendaEnLinea.TiendaEnLinea.Entity.OrderItem;
import com.TiendaEnLinea.TiendaEnLinea.Entity.Productos;
import com.TiendaEnLinea.TiendaEnLinea.Enum.OrderStates;
import com.TiendaEnLinea.TiendaEnLinea.Repository.ProductosRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Transactional
public class OrderValidator {

    private final ProductosRepository productosRepository;

    public OrderValidator(ProductosRepository productosRepository) {
        this.productosRepository = productosRepository;
    }

    public void validateForPayment(Order order) {
        validateOrderState(order);
        validateOrderData(order);
    }

    public void validateOrderState(Order order) {

        if (order.getStatus() == OrderStates.CANCELLED) {
            throw new IllegalStateException("La orden ya ha sido cancelada");
        }
        if (order.getStatus() == OrderStates.PAID) {
            throw new IllegalStateException("lA orden ya fue pagada");
        }
        if (order.getStatus() != OrderStates.CREATE) {
            throw new IllegalStateException(String.format("La orden no puede pagarse en estado: %s", order.getStatus()));
        }
    }

    public void validateOrderData(Order order) {

        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new IllegalStateException("La orden no tiene items");
        }
        if (order.getTotal() == null || order.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Monto invalido");
        }
    }


    public void validateBisinessRules(Order order) {
        order.getItems().forEach(item -> {
            if (item.getCantidad() <= 0) {
                throw new IllegalStateException(String.format("El Item no está disponible", item.getProductoId(), item.getProductName()));
            }
        });
    }


    //Validar disponibilidad
    public void validarDisponibilidadItems(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalStateException("la orden no tiene Items");
        }
        List<Long> productIds = items.stream().map(OrderItem::getProductoId).collect(Collectors.toList());


        Map<Long, Productos> productosMap = productosRepository.findAllById(productIds).stream().collect(Collectors.toMap(Productos::getId, p -> p));

        for (OrderItem item : items) {
            Productos producto = productosMap.get(item.getProductoId());


        }
    }
}
