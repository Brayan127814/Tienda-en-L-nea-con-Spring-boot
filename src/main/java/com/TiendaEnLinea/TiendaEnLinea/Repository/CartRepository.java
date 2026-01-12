package com.TiendaEnLinea.TiendaEnLinea.Repository;

import com.TiendaEnLinea.TiendaEnLinea.Entity.Cart;
import com.TiendaEnLinea.TiendaEnLinea.Entity.Productos;
import com.TiendaEnLinea.TiendaEnLinea.Entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository  extends JpaRepository<Cart , Long> {

     Optional<Cart> findByUsuario(UsuarioEntity usuario);

}
