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
    private final JwtUtils jwtUtils;
    private final UsuarioRepository usuarioRepository;
    private static String[] PUBLIC_URL = {
            "/usuarios/register",
            "/usuarios/login",
            "/usuarios/refresh",
            "/productos/addProducts",
            "/productos/allproducts",
            "/product/*",
            "/productos/paginados",
            "/productos/actualizar/*",
            "/productos/eliminar/*",
            "/categorias/**",
            "/productos/**"
    };

    public JwtFilter(JwtUtils jwtUtils, UsuarioRepository usuarioRepository) {
        this.jwtUtils = jwtUtils;
        this.usuarioRepository = usuarioRepository;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();

        //validar las url publicas
        boolean isValid = Arrays.stream(PUBLIC_URL).anyMatch(requestUri::startsWith);

        if (isValid) {
            filterChain.doFilter(request, response);
            return;
        }

        //Obtener el ahutheader
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        //Obtener el token
        String token = authHeader.substring(7);
        /*
        Validar el token
         */
        if (!jwtUtils.validarToken(token)) {
            response.getWriter().write("Token invalido");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        /*
        SACAR EL SUBJECT
         */

        try {
            String subject = jwtUtils.getSubjectFromToken(token);
            if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsuarioEntity usuario = usuarioRepository.findByEmail(subject).orElseThrow(() -> new NotFoundExceptions("Usuario no encontrado"));

                var authorities = usuario.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getRoleName())).toList();


                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(usuario.getEmail(), null, authorities);


                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token Invalido ok " + e);

        }

        filterChain.doFilter(request, response);
    }
}

/*
public class JwtFilter extends OncePerRequestFilter {

    private final UsuarioRepository usuarioRepository;
    private final CurrentService currentService;
    private final JwtUtils jwtUtils;
    private static String[] PUBLIC_URL = {
            "/usuarios/register",
            "/usuarios/login",
            "/productos/addProducts",
            "/productos/allproducts",
            "/product/*",
            "/productos/paginados",
            "/productos/actualizar/*"
    };

    public JwtFilter(UsuarioRepository usuarioRepository, CurrentService currentService, JwtUtils jwtUtils) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtils = jwtUtils;
        this.currentService = currentService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();// Url donde se aplicará el filtro
        boolean isPublic = Arrays.stream(PUBLIC_URL).anyMatch(requestUri::startsWith);// URLS publicas
        //si la url es publica no aplicar filtro
        if (isPublic) {
            filterChain.doFilter(request, response);
            return;
        }

        //Extraer encavezado
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        //Extraer token
        String token = authHeader.substring(7);
        if (!jwtUtils.validarToken(token)) {
            logger.error("Token invalido");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token invalido ok");
            return;
        }

        try {
            //extraer subject
            String subject = jwtUtils.getSubjectFromToken(token);

            if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsuarioEntity usuario = usuarioRepository.findByEmail(subject).orElseThrow(() -> new NotFoundExceptions("Usuario no encontrado"));


                //EXTRAER ROLES
                var authorities = usuario.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getRoleName())).toList();

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(usuario.getEmail(), null, authorities);
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            }

        } catch (Exception error) {
            logger.error("Token invalido");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token invalido ok" + error);
            return;
        }
        filterChain.doFilter(request, response);
    }
}

 */