package com.TiendaEnLinea.TiendaEnLinea.services;


import com.TiendaEnLinea.TiendaEnLinea.Entity.UsuarioEntity;
import com.TiendaEnLinea.TiendaEnLinea.Exceptions.NotFoundExceptions;
import com.TiendaEnLinea.TiendaEnLinea.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jmx.export.UnableToRegisterMBeanException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;


@Service
@RequiredArgsConstructor
public class CurrentService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioEntity getCurrenteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new RuntimeException("Usuario no autenticado");
        }

       return  usuarioRepository.findByEmail(authentication.getName()).orElseThrow(()->new NotFoundExceptions("Usuario no registrado"));
    }
}
