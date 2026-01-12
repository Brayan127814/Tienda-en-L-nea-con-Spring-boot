package com.TiendaEnLinea.TiendaEnLinea.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Relación uno a uno
    @OneToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity usuario;

    @OneToMany (mappedBy = "cart", cascade =CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items= new ArrayList<>();


}
