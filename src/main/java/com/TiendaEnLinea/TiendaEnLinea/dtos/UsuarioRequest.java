package com.TiendaEnLinea.TiendaEnLinea.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequest {

    private  String name;
    private  String email;
    private  String password;

}
