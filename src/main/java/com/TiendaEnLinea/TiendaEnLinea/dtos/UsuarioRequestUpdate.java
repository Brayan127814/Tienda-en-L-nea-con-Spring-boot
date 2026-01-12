package com.TiendaEnLinea.TiendaEnLinea.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioRequestUpdate extends  UsuarioRequest{
     private List<String> roles;

}
