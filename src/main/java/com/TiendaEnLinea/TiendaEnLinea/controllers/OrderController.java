package com.TiendaEnLinea.TiendaEnLinea.controllers;

import com.TiendaEnLinea.TiendaEnLinea.Entity.Order;
import com.TiendaEnLinea.TiendaEnLinea.dtos.OrderResponse;
import com.TiendaEnLinea.TiendaEnLinea.services.OrderService;
import com.TiendaEnLinea.TiendaEnLinea.utils.OrderMaper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final OrderMaper orderMaper;

    public OrderController(OrderService orderService, OrderMaper orderMaper) {
        this.orderService = orderService;
        this.orderMaper = orderMaper;
    }

    //Endpoint para crear una orden

    @PostMapping("/crearOrden")
    public OrderResponse crearOrden() {
        Order order = orderService.crearOrdenDesdeCarrito();
        return orderMaper.toResponse(order);

    }

    //listar todas las ordenes
    @GetMapping
    public ResponseEntity<List<OrderResponse>> listar() {
        List<OrderResponse> orders = orderService.listarOrdenes();

        return ResponseEntity.ok(orders);
    }

    //ENPOINT PARA CANCELAR UNA ORDEN

    @DeleteMapping("{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable long orderId) {
        orderService.CancelarOrden(orderId);
        return ResponseEntity.noContent().build();
    }

    //ENDPOINT  PARA PAGAR ORDEN
    @PostMapping("/{orderId}/paid")
    public ResponseEntity<?> pagar(@PathVariable long orderId) {
        orderService.pagarOrden(orderId);
        return ResponseEntity.ok(Map.of("message", "Order pagada correctamente"));
    }
}
