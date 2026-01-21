package com.TiendaEnLinea.TiendaEnLinea.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "productos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Productos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String descriptions;

    @Column(nullable = false)
    private Integer stock;


    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private  String imageUrl;


    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}
