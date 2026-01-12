package com.TiendaEnLinea.TiendaEnLinea.services;


import com.TiendaEnLinea.TiendaEnLinea.Entity.Categoria;
import com.TiendaEnLinea.TiendaEnLinea.Entity.Productos;
import com.TiendaEnLinea.TiendaEnLinea.Exceptions.NotFoundExceptions;
import com.TiendaEnLinea.TiendaEnLinea.Repository.CategoriaRepository;
import com.TiendaEnLinea.TiendaEnLinea.Repository.ProductosRepository;
import com.TiendaEnLinea.TiendaEnLinea.dtos.ProductoRequestDto;
import com.TiendaEnLinea.TiendaEnLinea.dtos.ProductoResponse;
import com.TiendaEnLinea.TiendaEnLinea.dtos.UsuarioResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductosServices {

    private final ProductosRepository productosRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductosServices(ProductosRepository productosRepository, CategoriaRepository categoriaRepository) {
        this.productosRepository = productosRepository;
        this.categoriaRepository = categoriaRepository;
    }

    //Registrar productos

    public ProductoResponse crearProucto(ProductoRequestDto data) {
        //Buscar productos antes de registrar


        //Buscar categoria

        Categoria categoria = categoriaRepository.findById(data.getCategoriaId()).orElseThrow(() -> new NotFoundExceptions("Categoria no encontrada"));


        Productos producto = Productos.builder()
                .productName(data.getProductName())
                .descriptions(data.getDescriptions())
                .stock(data.getStock())
                .price(data.getPrice())
                .categoria(categoria)
                .build();


        //GUARDAR
        Productos guardado = productosRepository.save(producto);

        //Retornar DTO de repuesta
        ProductoResponse response = new ProductoResponse();
        response.setId(guardado.getId());
        response.setProductName(guardado.getProductName());
        response.setDescriptions(guardado.getDescriptions());
        response.setStock(guardado.getStock());
        response.setPrice(guardado.getPrice());
        response.setCategoryId(categoria.getId());
        response.setCategoryName(categoria.getCategoryName());

        return response;

    }

    //LISTAR PRODUCTOS

    public List<ProductoResponse> allProducts() {
        List<Productos> productos = productosRepository.findAll();

        return productos.stream().map(p -> ProductoResponse.builder()

                .id(p.getId())
                .productName(p.getProductName())
                .descriptions(p.getDescriptions())
                .stock(p.getStock())
                .price(p.getPrice())
                .categoryId(p.getCategoria().getId())
                .categoryName(p.getCategoria().getCategoryName())
                .build()
        ).toList();
    }

    //BUSCAR UN PRODUCTO

    public ProductoResponse productById(Long id) {

        Productos producto = productosRepository.findById(id).orElseThrow(() -> new NotFoundExceptions("Producto no encontrado"));

        return ProductoResponse.builder()
                .id(producto.getId())
                .productName(producto.getProductName())
                .descriptions(producto.getDescriptions())
                .stock(producto.getStock())
                .price(producto.getPrice())
                .categoryId(producto.getCategoria().getId())
                .categoryName(producto.getCategoria().getCategoryName())
                .build();
    }

}
