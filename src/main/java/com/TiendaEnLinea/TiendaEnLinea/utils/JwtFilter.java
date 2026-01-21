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
            "/productos/allproducts",
            "/product/*"
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


/**
 * public JwtFilter(UsuarioRepository usuarioRepository, JwtUtils jwtUtils, CurrentService currentService) {
 * this.usuarioRepository = usuarioRepository;
 * this.jwtUtils = jwtUtils;
 * this.currentService = currentService;
 * }
 *
 * @Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
 * // Sacar la url donde se aplica el filtro
 * <p>
 * String requestUri = request.getRequestURI();
 * //OBTENER LAS URL PUBLICAS
 * boolean isPublic = Arrays.stream(PUBLIC_URL).anyMatch(requestUri::startsWith);
 * <p>
 * if (isPublic) {
 * filterChain.doFilter(request, response);
 * return;
 * }
 * <p>
 * //OBTENER EL HEADER DE AUTENTICACIÓN
 * String authHeader = request.getHeader("Authorization");
 * if (authHeader == null || !authHeader.startsWith("Bearer ")) {
 * filterChain.doFilter(request, response);
 * return;
 * }
 * <p>
 * //Extraer el token de autorización
 * <p>
 * String token = authHeader.substring(7);
 * //Validar token
 * if (!jwtUtils.validarToken(token)) {
 * logger.error("Token invalido");
 * response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
 * response.getWriter().write("Token invalido");
 * return;
 * }
 * <p>
 * //Extraer el subject
 * <p>
 * try {
 * <p>
 * String email = jwtUtils.getSubjectFromToken(token);
 * if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
 * <p>
 * UsuarioEntity usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new NotFoundExceptions("Usuario no enontrado"));
 * <p>
 * if (usuario != null) {
 * <p>
 * var authorities = usuario.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getRoleName())).toList();
 * UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usuario.getEmail(), null, authorities);
 * authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
 * SecurityContextHolder.getContext().setAuthentication(authenticationToken);
 * }
 * }
 * <p>
 * <p>
 * } catch (Exception error) {
 * <p>
 * logger.error("Token invalido" + error);
 * response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
 * response.getWriter().write("Token invalido");
 * }
 * <p>
 * filterChain.doFilter(request, response);
 * }
 */