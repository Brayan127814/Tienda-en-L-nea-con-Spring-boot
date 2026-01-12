package com.TiendaEnLinea.TiendaEnLinea.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(nullable = false)
    private String name;

    @Column
    private String email;

    @Column
    private String password;


    @OneToOne(mappedBy = "usuario")
    private  Cart cart;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuaro_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id")
    )

    private Set<RolesEntity> roles = new HashSet<>();
}
