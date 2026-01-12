package com.TiendaEnLinea.TiendaEnLinea.services;


import com.TiendaEnLinea.TiendaEnLinea.Entity.Cart;
import com.TiendaEnLinea.TiendaEnLinea.Entity.CartItem;
import com.TiendaEnLinea.TiendaEnLinea.Entity.Productos;
import com.TiendaEnLinea.TiendaEnLinea.Entity.UsuarioEntity;
import com.TiendaEnLinea.TiendaEnLinea.Exceptions.NotFoundExceptions;
import com.TiendaEnLinea.TiendaEnLinea.Repository.CartItemRepository;
import com.TiendaEnLinea.TiendaEnLinea.Repository.CartRepository;
import com.TiendaEnLinea.TiendaEnLinea.Repository.ProductosRepository;
import com.TiendaEnLinea.TiendaEnLinea.Repository.UsuarioRepository;
import com.TiendaEnLinea.TiendaEnLinea.dtos.AddToCartRequest;
import com.TiendaEnLinea.TiendaEnLinea.dtos.CartItemResponse;
import com.TiendaEnLinea.TiendaEnLinea.dtos.CartResponse;
import com.TiendaEnLinea.TiendaEnLinea.utils.CartMapper;
import jakarta.transaction.Transactional;
import org.hibernate.query.ReturnableType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UsuarioRepository usuarioRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductosRepository productosRepository;
    private final CartMapper cartMapper;
    private final CurrentService currentService;

    public CartService(CartRepository cartRepository, UsuarioRepository usuarioRepository, CartItemRepository cartItemRepository, ProductosRepository productosRepository, CartMapper cartMapper, CurrentService currentService) {
        this.cartRepository = cartRepository;
        this.usuarioRepository = usuarioRepository;
        this.cartItemRepository = cartItemRepository;
        this.productosRepository = productosRepository;
        this.cartMapper = cartMapper;
        this.currentService = currentService;
    }

    //buscar carrito perteneciente al usuario
    @Transactional()
    public Cart obtenerCarrtito(long usuarioId) {
        Optional<UsuarioEntity> usuario = usuarioRepository.findById(usuarioId);
        if (usuario.isEmpty()) {
            throw new NotFoundExceptions("Usuario no encontrado");
        }

        UsuarioEntity user = usuario.get();

        return cartRepository.findByUsuario(user).orElseGet(() -> {
            Cart nuevo = new Cart();
            nuevo.setUsuario(user);
            return cartRepository.save(nuevo);
        });


    }

    //METODO PARA AGREGAR PRODUCTOS AL CARRTIO
    @Transactional
    public CartResponse agregarProductos(AddToCartRequest data) {
        //VALIDAR QUE LA CANTIDAD SEA VALIDA
        if (data.getCantidad() == null || data.getCantidad() <= 0) {
            throw new IllegalArgumentException("Cantidad invalida");
        }

        //BUSCAR EL USUARIO AUTENTICADO

        UsuarioEntity usuario = currentService.getCurrenteUser();


        //OBTENER EL CARRRITO DE ESE USUARIO
        Cart cart = cartRepository.findByUsuario(usuario).orElseThrow(() -> new NotFoundExceptions("carrito no encontrado"));
        //OBTENER ESE PRODUCCTO
        Productos producto = productosRepository.findById(data.getProductoId()).orElseThrow(() -> new NotFoundExceptions("Producto no encontrado"));


        //Verificar que el producto ya este relacionado en el carrtito
        // si eso pasa aumentamos en uno la cantidad
        // si no creamos un item nuevo
        Optional<CartItem> itemsp = cartItemRepository.findByCartAndProductos(cart, producto);
        if (itemsp.isPresent()) {
            itemsp.get().setQuantity(itemsp.get().getQuantity() + data.getCantidad());
        } else {
            cart.getItems().add(
                    CartItem.builder()
                            .cart(cart)
                            .productos(producto)
                            .quantity(data.getCantidad())
                            .build()
            );
        }
        Cart guardado = cartRepository.save(cart);
        return cartMapper.toCartMapper(guardado);
    }

    //METODO PARA MAPEAR RESPUESTA
    public CartResponse mapToResponse(Cart cart) {
        List<CartItemResponse> itemp = cart.getItems().stream().map(
                item -> {
                    BigDecimal price = item.getProductos().getPrice();
                    BigDecimal subTotal = price.multiply(BigDecimal.valueOf(item.getQuantity()));

                    return CartItemResponse.builder()
                            .itemId(item.getId())
                            .productoId(item.getProductos().getId())
                            .name(item.getProductos().getProductName())
                            .quantity(item.getQuantity())
                            .precioUnitario(item.getProductos().getPrice())
                            .subtotal(subTotal)
                            .build();
                }
        ).toList();
        //TOTAL DE LA COMPRA
        BigDecimal total = itemp.stream().map(CartItemResponse::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);

        //TOTAL ARTICULOS COMPRADOS
        int totalItems = itemp.stream().mapToInt(CartItemResponse::getQuantity).sum();


        return CartResponse.builder()
                .items(itemp)
                .totalItems(totalItems)
                .totalPrice(total)
                .build();

    }

    //ELIMINAR PRODUCTO DEL CARRITO

    public CartResponse eliminarProducto(long productoId) {

        //Obtener usuaro loguado


        UsuarioEntity usuario = currentService.getCurrenteUser();

        //Obtener su carrito

        Cart cart = obtenerCarrtito(usuario.getId());
        //OBTENER PRODUCTO
        Productos producto = productosRepository.findById(productoId).orElseThrow(() -> new NotFoundExceptions("Producto no encontrado"));

        //Buscar item
        CartItem item = cartItemRepository.findByCartAndProductos(cart, producto).orElseThrow(() -> new NotFoundExceptions("Producto no encontrado"));

        if (item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
            cartItemRepository.save(item);
        } else {
            cartItemRepository.delete(item);
        }
        return mapToResponse(cart);
    }

    //vaciar cart
    public void vaciarCart() {
        UsuarioEntity usuario = currentService.getCurrenteUser();

        Cart cart = cartRepository.findByUsuario(usuario).orElseThrow(() -> new NotFoundExceptions("Carrito no encontrado"));

        cart.getItems().clear();
    }
}


/*


    public CartResponse mapToRresponse(Cart cart) {
        List<CartItemResponse> itemsp = cart.getItems().stream()
                .map(item -> {
                    BigDecimal price = item.getProductos().getPrice();
                    BigDecimal subTotal = price.multiply(BigDecimal.valueOf(item.getQuantity()));

                    return CartItemResponse.builder()
                            .itemId(item.getId())
                            .productoId(item.getProductos().getId())
                            .name(item.getProductos().getProductName())
                            .quantity(item.getQuantity())
                            .precioUnitario(item.getProductos().getPrice())
                            .subtotal(subTotal)
                            .build();
                }).toList();

        BigDecimal total = itemsp.stream().map(CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        int totalItems = itemsp.stream().mapToInt(CartItemResponse::getQuantity).sum();


        return  CartResponse.builder()
                .items(itemsp)
                .totalItems(totalItems)
                .totalPrice(total).build();
    }
 */