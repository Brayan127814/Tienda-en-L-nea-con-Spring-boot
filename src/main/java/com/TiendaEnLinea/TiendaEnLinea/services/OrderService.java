package com.TiendaEnLinea.TiendaEnLinea.services;

import com.TiendaEnLinea.TiendaEnLinea.Entity.*;
import com.TiendaEnLinea.TiendaEnLinea.Enum.OrderStates;
import com.TiendaEnLinea.TiendaEnLinea.Exceptions.BadRequestException;
import com.TiendaEnLinea.TiendaEnLinea.Exceptions.NotFoundExceptions;
import com.TiendaEnLinea.TiendaEnLinea.Repository.CartRepository;
import com.TiendaEnLinea.TiendaEnLinea.Repository.OrderRepository;
import com.TiendaEnLinea.TiendaEnLinea.Repository.ProductosRepository;
import com.TiendaEnLinea.TiendaEnLinea.Repository.UsuarioRepository;
import com.TiendaEnLinea.TiendaEnLinea.dtos.OrderResponse;
import com.TiendaEnLinea.TiendaEnLinea.utils.OrderMaper;
import com.TiendaEnLinea.TiendaEnLinea.utils.OrderValidator;
import jakarta.transaction.Transactional;
import jdk.dynalink.NamedOperation;
import org.aspectj.weaver.ast.Or;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final CartRepository cartRepository;
    private final UsuarioRepository usuarioRepository;
    private final OrderRepository orderRepository;
    private final OrderMaper orderMaper;
    private final CurrentService currentService;
    private final OrderValidator orderValidator;
    private final ProductosRepository productosRepository;
    private final CartService cartService;


    public OrderService(CartRepository cartRepository, UsuarioRepository usuarioRepository, OrderRepository orderRepository, OrderMaper orderMaper, CurrentService currentService,
                        OrderValidator orderValidator, ProductosRepository productosRepository, CartService cartService) {
        this.cartRepository = cartRepository;
        this.usuarioRepository = usuarioRepository;
        this.orderRepository = orderRepository;
        this.orderMaper = orderMaper;
        this.currentService = currentService;
        this.orderValidator = orderValidator;
        this.productosRepository = productosRepository;
        this.cartService = cartService;
    }

    //CREAR UNA ORDEN

    @Transactional
    public Order crearOrdenDesdeCarrito() {
        //Buscar el usuario autenticado
        UsuarioEntity usuario = currentService.getCurrenteUser();
        //Crear la orden
        Order order = Order.builder()
                .usuario(usuario)
                .status(OrderStates.CREATE)
                .createATt(LocalDateTime.now())
                .build();

        //Obtener su carrito de compras
        Cart cart = cartRepository.findByUsuario(usuario).orElseThrow(() -> new NotFoundExceptions("Carrito no encontrado"));

        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Carrito vacío");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;


        for (CartItem items : cart.getItems()) {
            BigDecimal price = items.getProductos().getPrice();
            BigDecimal subTotal = price.multiply(BigDecimal.valueOf(items.getQuantity()));


            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .productoId(items.getProductos().getId())
                    .productName(items.getProductos().getProductName())
                    .precioUnitario(price)
                    .cantidad(items.getQuantity())
                    .subTotal(subTotal)
                    .build();

            orderItems.add(orderItem);
            total = total.add(subTotal);

        }

        order.setItems(orderItems);
        order.setTotal(total);


        Order guardado = orderRepository.save(order);

        return guardado;
    }


    //LISTAR ORDENES

    public List<OrderResponse> listarOrdenes() {

        UsuarioEntity usuario = currentService.getCurrenteUser();
        List<Order> orders = orderRepository.findByUsuario(usuario);

        return orders.stream().map(orderMaper::toResponse).toList();
    }

    //CACELAR UNA ORDEN
    @Transactional
    public void CancelarOrden(long orderId) {
        UsuarioEntity usuario = currentService.getCurrenteUser();

        // BUSCAR LA ORDEN

        Order order = orderRepository.findByIdAndUsuario(orderId, usuario).orElseThrow(() -> new NotFoundExceptions("Orden no encontrada"));

        if (order.getStatus() == OrderStates.CANCELLED) {
            throw new BadRequestException("La orden ya fue cancelada");
        }

        if (order.getStatus() == OrderStates.SHIPPED) {
            throw new BadRequestException("La compra ya fue despachada");
        }

        order.setStatus(OrderStates.CANCELLED);
    }


    //PAGAR ORDEN
    @Transactional
    public void pagarOrden(long orderId) {
        UsuarioEntity usuario = currentService.getCurrenteUser();
        Order order = orderRepository.findByIdAndUsuario(orderId, usuario).orElseThrow(() -> new NotFoundExceptions("Orden no encontrada"));

        orderValidator.validateOrderState(order);
        orderValidator.validateOrderData(order);

        List<OrderItem> items = order.getItems();
        List<Long> productsId = items.stream().map(OrderItem::getProductoId).collect(Collectors.toList());
        Map<Long, Productos> productosMap = productosRepository.findAllById(productsId).stream().collect(Collectors.toMap(Productos::getId, p -> p));


        for (OrderItem item : items) {
            Productos producto = productosMap.get(item.getProductoId());

            if (producto == null) {
                throw new IllegalStateException("Producto no encontrado");
            }

            if (item.getCantidad() > producto.getStock()) {
                throw new BadRequestException("Stock insuficiente para el producto " + item.getProductName());
            }

            producto.setStock(producto.getStock() - item.getCantidad());
        }

        productosRepository.saveAll(productosMap.values());
        order.setStatus(OrderStates.PAID);
        order.setCreateATt(LocalDateTime.now());
        cartService.vaciarCart();

        orderRepository.save(order);
    }

}

/*


    @Transactional
    public void pagarOrden(long orderId) {
        UsuarioEntity usuario = currentService.getCurrenteUser();


        //Buscar la orden
        Order order = orderRepository.findByIdAndUsuario(orderId, usuario).orElseThrow(() -> new NotFoundExceptions("Ordern no encontrada"));
        //VALIDAR EL ESTADO DE LA ORDEN
        orderValidator.validateOrderState(order);
        orderValidator.validateOrderData(order);

        //obterner los id de los productos
        List<OrderItem> items = order.getItems();
        List<Long> productsIds = items.stream().map(OrderItem::getProductoId).collect(Collectors.toList());

        Map<Long, Productos> productosMap = productosRepository.findAllById(productsIds).stream().collect(Collectors.toMap(Productos::getId, p -> p));

        for (OrderItem item : items) {
            Productos producto = productosMap.get(item.getProductoId());
            if (producto == null) {
                throw new IllegalStateException("Producto no encontrado");
            }
            if (producto.getStock() < item.getCantidad()) {
                throw new BadRequestException("Stock insuficiente para el producto " + item.getProductName());
            }
            producto.setStock(producto.getStock() - item.getCantidad());
        }
        productosRepository.saveAll(productosMap.values());
        order.setStatus(OrderStates.PAID);
        order.setCreateATt(LocalDateTime.now());
        cartService.vaciarCart();

        orderRepository.save(order);
    }
 */


/*

@Transactional
    public Order crearOrdenDesdeCarrito() {
        //buscar el usuario autenticado
        UsuarioEntity usuario = currentService.getCurrenteUser();

        //crear orden
        Order order = Order.builder()
                .usuario(usuario)
                .status(OrderStates.CREATE)
                .createATt(LocalDateTime.now())
                .build();
//CARRTO DEL USUARIO
        Cart cart = cartRepository.findByUsuario(usuario).orElseThrow(() -> new NotFoundExceptions("Carrito no encontrado"));
        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Carrito vacío");
        }
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO; // empieza en 0
        //Pasar los productos que están en el carrito para la ordent items
        for (CartItem item : cart.getItems()) {
            //sacar precio del producto
            BigDecimal price = item.getProductos().getPrice();
            //calcular total
            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(item.getQuantity()));


            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .productoId(item.getProductos().getId())
                    .productName(item.getProductos().getProductName())
                    .precioUnitario(price)
                    .cantidad(item.getQuantity())
                    .subTotal(subtotal)
                    .build();

            orderItems.add(orderItem);
            total = total.add(subtotal);
        }

        order.setItems(orderItems);
        order.setTotal(total);

        Order guardado = orderRepository.save(order);
        return guardado;

    }

 */