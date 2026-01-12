package com.TiendaEnLinea.TiendaEnLinea.Repository;

import com.TiendaEnLinea.TiendaEnLinea.Entity.Productos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProductosRepository extends JpaRepository<Productos, Long> {

    Optional<Productos> findByProductName(String productName);

    Optional<Productos> findById(long id);
}
