package com.TiendaEnLinea.TiendaEnLinea.Entity;


import jakarta.persistence.*;
import lombok.*;


import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categorias")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(nullable = false)
    private String categoryName;

    @Column(nullable = false)
    private String description;


    @OneToMany(mappedBy = "categoria")
    private  Set<Productos> productos = new HashSet<>();


}
