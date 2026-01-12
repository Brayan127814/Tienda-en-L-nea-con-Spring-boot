package com.TiendaEnLinea.TiendaEnLinea.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RolesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;


    @Column
    private String roleName;


    @ManyToMany(mappedBy = "roles")
    private Set<UsuarioEntity> usuario = new HashSet<>();

}
