package com.TiendaEnLinea.TiendaEnLinea.utils;


import com.TiendaEnLinea.TiendaEnLinea.Entity.UsuarioEntity;
import com.TiendaEnLinea.TiendaEnLinea.Exceptions.NotFoundExceptions;
import com.TiendaEnLinea.TiendaEnLinea.Repository.UsuarioRepository;
import com.TiendaEnLinea.TiendaEnLinea.services.CurrentService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final UsuarioRepository usuarioRepository;
    private final CurrentService currentService;
    private final JwtUtils jwtUtils;
    private static String[] PUBLIC_URL = {
            "/usuarios/register",
            "/usuarios/login",
            "/productos/addProducts",
            "/products/allproducts",
            "/product/*"
    };

    public JwtFilter(UsuarioRepository usuarioRepository, JwtUtils jwtUtils, CurrentService currentService) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtils = jwtUtils;
        this.currentService = currentService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Sacar la url donde se aplica el filtro

        String requestUri = request.getRequestURI();
        //OBTENER LAS URL PUBLICAS
        boolean isPublic = Arrays.stream(PUBLIC_URL).anyMatch(requestUri::startsWith);

        if (isPublic) {
            filterChain.doFilter(request, response);
            return;
        }

        //OBTENER EL HEADER DE AUTENTICACIÓN
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        //Extraer el token de autorización

        String token = authHeader.substring(7);
        //Validar token
        if (!jwtUtils.validarToken(token)) {
            logger.error("Token invalido");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token invalido");
            return;
        }

        //Extraer el subject

        try {

            String email = jwtUtils.getSubjectFromToken(token);
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UsuarioEntity usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new NotFoundExceptions("Usuario no enontrado"));

                if (usuario != null) {

                    var authorities = usuario.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getRoleName())).toList();
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usuario.getEmail(), null, authorities);
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }


        } catch (Exception error) {

            logger.error("Token invalido" + error);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token invalido");
        }

        filterChain.doFilter(request, response);
    }
}
