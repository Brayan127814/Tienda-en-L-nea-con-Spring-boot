package com.TiendaEnLinea.TiendaEnLinea.controllers;

import com.TiendaEnLinea.TiendaEnLinea.dtos.ProductoRequestDto;
import com.TiendaEnLinea.TiendaEnLinea.dtos.ProductoResponse;
import com.TiendaEnLinea.TiendaEnLinea.services.ProductosServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductosControllers {

    private final ProductosServices productosServices;

    public ProductosControllers(ProductosServices productosServices) {
        this.productosServices = productosServices;
    }

    //ENDPOINT PARA REGISTRAR PRODUCTOS
    @PostMapping("/addProducts")
    public ResponseEntity<ProductoResponse> addProducto(@RequestBody ProductoRequestDto data) {
        ProductoResponse producto = productosServices.crearProucto(data);

        return ResponseEntity.status(HttpStatus.CREATED).body(producto);
    }

    //ENDPOINT PARA LISTAR TODOS LOS PRODUCTOS
    @GetMapping("/allproducts")
    public ResponseEntity<List<ProductoResponse>> listar() {

        List<ProductoResponse> productos = productosServices.allProducts();

        return ResponseEntity.ok(productos);

    }

    //ENDPOINT PARA OBTENER UN PRODUCTO
    @GetMapping("/product/{id}")
    public ResponseEntity<ProductoResponse> getProductById(@PathVariable long id) {
        ProductoResponse producto = productosServices.productById(id);
        return  ResponseEntity.ok(producto);
    }
}
