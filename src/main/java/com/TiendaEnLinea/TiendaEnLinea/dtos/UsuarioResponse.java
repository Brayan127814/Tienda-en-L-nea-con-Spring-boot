package com.TiendaEnLinea.TiendaEnLinea.dtos;


import com.TiendaEnLinea.TiendaEnLinea.Entity.RolesEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class UsuarioResponse {

    private long id;
    private String name;
    private String email;
    private List<String> roles;
}
