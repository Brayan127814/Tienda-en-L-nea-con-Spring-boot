package com.TiendaEnLinea.TiendaEnLinea.services;


import com.TiendaEnLinea.TiendaEnLinea.Entity.Categoria;
import com.TiendaEnLinea.TiendaEnLinea.Entity.Productos;
import com.TiendaEnLinea.TiendaEnLinea.Exceptions.NotFoundExceptions;
import com.TiendaEnLinea.TiendaEnLinea.Repository.CategoriaRepository;
import com.TiendaEnLinea.TiendaEnLinea.Repository.ProductosRepository;
import com.TiendaEnLinea.TiendaEnLinea.dtos.ProductoRequestDto;
import com.TiendaEnLinea.TiendaEnLinea.dtos.ProductoResponse;
import com.TiendaEnLinea.TiendaEnLinea.dtos.ProductoUpdateDto;
import com.TiendaEnLinea.TiendaEnLinea.dtos.UsuarioResponse;
import com.TiendaEnLinea.TiendaEnLinea.utils.ProductsMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductosServices {

    private final ProductosRepository productosRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductsMapper productsMapper;

    public ProductosServices(ProductosRepository productosRepository, CategoriaRepository categoriaRepository, ProductsMapper productsMapper) {
        this.productosRepository = productosRepository;
        this.categoriaRepository = categoriaRepository;
        this.productsMapper = productsMapper;
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
                .imageUrl(data.getImageUrl())
                .categoria(categoria)
                .build();


        //GUARDAR
        Productos guardado = productosRepository.save(producto);

        //Retornar DTO de repuesta
        ProductoResponse response = new ProductoResponse();
        response.setImageUrl(guardado.getImageUrl());
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
                .imageUrl(p.getImageUrl())
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
                .imageUrl(producto.getImageUrl())
                .id(producto.getId())
                .productName(producto.getProductName())
                .descriptions(producto.getDescriptions())
                .stock(producto.getStock())
                .price(producto.getPrice())
                .categoryId(producto.getCategoria().getId())
                .categoryName(producto.getCategoria().getCategoryName())
                .build();
    }


    //Actualizar los campos de un producto


    public ProductoResponse updateProducto(long productoId, ProductoUpdateDto data) {
        //buscar el producto
        Productos producto = productosRepository.findById(productoId).orElseThrow(() -> new NotFoundExceptions("Producto no encontrado"));

        //Actualizar los campos
        producto.setStock(data.getStock());
        producto.setPrice(data.getPrice());
        producto.setImageUrl(data.getImageUrl());

        Productos actualizado = productosRepository.save(producto);


        return ProductoResponse.builder()
                .id(actualizado.getId())
                .productName(actualizado.getProductName())
                .descriptions(actualizado.getDescriptions())
                .stock(actualizado.getStock())
                .price(actualizado.getPrice())
                .imageUrl(actualizado.getImageUrl())
                .categoryName(actualizado.getCategoria().getCategoryName())
                .categoryId(actualizado.getCategoria().getId())


                .build();
    }

    //Eliminar un producto

    public void deleteProducto(long productoId) {
        productosRepository.findById(productoId).ifPresentOrElse(
                productosRepository::delete, () -> {
                    throw new NotFoundExceptions("Producto no encontrado");
                }

        );
    }

    //Obtener los productos paginados
    public Page<ProductoResponse> productosPaginados(Pageable pageable) {
        return productosRepository.findAll(pageable).map(productsMapper::convertirADto);
    }

    //Obtener productos por categoria
    @Transactional
    public List<ProductoResponse> getByCategoria(long categoriaId) {
        List<Productos> productos = productosRepository.findByCategoriaId(categoriaId);

        return productos.stream().map(p -> ProductoResponse.builder()
                .imageUrl(p.getImageUrl())
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
}
