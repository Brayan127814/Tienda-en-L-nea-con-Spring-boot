package com.TiendaEnLinea.TiendaEnLinea.controllers;

import com.TiendaEnLinea.TiendaEnLinea.Entity.Cart;
import com.TiendaEnLinea.TiendaEnLinea.dtos.AddToCartRequest;
import com.TiendaEnLinea.TiendaEnLinea.dtos.CartResponse;
import com.TiendaEnLinea.TiendaEnLinea.services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    //EDPOINT PARA AGREGAR PRODUCTOS AL CARRITO

    @PostMapping("/addproductotoCart")

    public ResponseEntity<CartResponse> addproducttoCart(@RequestBody AddToCartRequest data) {
        CartResponse cart = cartService.agregarProductos(data);

        return ResponseEntity.ok(cart);
    }
}
