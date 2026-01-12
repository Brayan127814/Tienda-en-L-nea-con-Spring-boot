package com.TiendaEnLinea.TiendaEnLinea.Repository;

import com.TiendaEnLinea.TiendaEnLinea.Entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Categoria findById(long id);
}
