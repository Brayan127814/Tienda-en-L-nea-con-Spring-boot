package com.TiendaEnLinea.TiendaEnLinea.utils;


import com.TiendaEnLinea.TiendaEnLinea.Entity.UsuarioEntity;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {
    private static final long EXPIRATION_TIME = 86400000;

    @Value("${jwt.secret:miClaveSecretaPorDefecto1234567891011121314151617}")
    private String secretString;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    }

    //GENERAR TOKEN
    public String generarToken(UsuarioEntity usuario) {
        List<String> roles = usuario.getRoles().stream().map(r -> r.getRoleName()).toList();

        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //EXTRAER EL SUBJECT DEL TOKEN

    public String getSubjectFromToken(String token) {
        try {
            if (token == null || token.equals("null") || token.isEmpty()) return null;

            return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token).getBody().getSubject();

        } catch (Exception e) {
            return null;
        }
    }

    //Validar token

    public boolean validarToken(String token) {
        try {
            if (token == null || token.isBlank()) {
                return false;
            }

            Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token);
            return true;

        } catch (Exception e) {
            return false;
        }
    }
}

/*
  @Value("${jwt.secret:miClaveSecretaPorDefecto12345678901234567890}")
    private String secretString;


    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    }

    //Generar token
    public String generarToken(UsuarioEntity usuario) {

        List<String> roles = usuario.getRoles().stream().map(r -> r.getRoleName()).toList();

        return Jwts.builder().setSubject(usuario.getEmail())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //EXTRAER SUBJECT
    public String getSubjectFromToken(String token) {
        try {
            if (token == null || token.equals("null") || token.isEmpty()) {
                return null;
            }

            return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token).getBody().getSubject();
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            return null;
        }
    }
    //Validar token
    public boolean validarToken(String token) {
        try {
            if (token == null || token.isEmpty()) return false;

            Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Error + " + e.getMessage());
            return false;
        } catch (Exception e) {
            return false;
        }
    }
 */
