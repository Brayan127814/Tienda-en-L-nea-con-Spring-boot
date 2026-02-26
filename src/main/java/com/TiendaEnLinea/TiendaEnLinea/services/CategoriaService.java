package com.TiendaEnLinea.TiendaEnLinea.services;


import com.TiendaEnLinea.TiendaEnLinea.Entity.Categoria;
import com.TiendaEnLinea.TiendaEnLinea.Repository.CategoriaRepository;
import com.TiendaEnLinea.TiendaEnLinea.dtos.CategoriaResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;


    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    //Servicio para consultar todas las categorias
    @Transactional
    public List<CategoriaResponseDto> listarCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();
        return categorias.stream().map(c -> CategoriaResponseDto.builder()
                .id(c.getId())
                .categoryName(c.getCategoryName())
                .descripcion(c.getDescription())

                .build()).toList();
    }

}
