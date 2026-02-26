package com.TiendaEnLinea.TiendaEnLinea.controllers;


import com.TiendaEnLinea.TiendaEnLinea.dtos.CategoriaResponseDto;
import com.TiendaEnLinea.TiendaEnLinea.services.CategoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
    private final CategoriaService categoriaService;


    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping

    public ResponseEntity<List<CategoriaResponseDto>> listar(){

         List<CategoriaResponseDto> categorias = categoriaService.listarCategorias();

         return  ResponseEntity.ok(categorias);
    }

}
