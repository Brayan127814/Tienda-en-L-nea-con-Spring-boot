package com.TiendaEnLinea.TiendaEnLinea.Repository;

import com.TiendaEnLinea.TiendaEnLinea.Entity.Cart;
import com.TiendaEnLinea.TiendaEnLinea.Entity.CartItem;
import com.TiendaEnLinea.TiendaEnLinea.Entity.Productos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

     Optional<CartItem> findByCartAndProductos(Cart cart, Productos productos);
}
