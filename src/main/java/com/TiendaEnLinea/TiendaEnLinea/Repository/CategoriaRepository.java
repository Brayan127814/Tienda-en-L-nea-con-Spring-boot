package com.TiendaEnLinea.TiendaEnLinea.Repository;

import com.TiendaEnLinea.TiendaEnLinea.Entity.Categoria;
import com.TiendaEnLinea.TiendaEnLinea.Entity.Productos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {


}
