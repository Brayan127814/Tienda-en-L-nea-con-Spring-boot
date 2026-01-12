package com.TiendaEnLinea.TiendaEnLinea.controllers;


import com.TiendaEnLinea.TiendaEnLinea.Entity.UsuarioEntity;
import com.TiendaEnLinea.TiendaEnLinea.Exceptions.NotFoundExceptions;
import com.TiendaEnLinea.TiendaEnLinea.Repository.UsuarioRepository;
import com.TiendaEnLinea.TiendaEnLinea.dtos.LoginRequest;
import com.TiendaEnLinea.TiendaEnLinea.dtos.UsuarioRequest;
import com.TiendaEnLinea.TiendaEnLinea.dtos.UsuarioRequestUpdate;
import com.TiendaEnLinea.TiendaEnLinea.dtos.UsuarioResponse;
import com.TiendaEnLinea.TiendaEnLinea.services.UsuariosServices;
import com.TiendaEnLinea.TiendaEnLinea.utils.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.function.LongBinaryOperator;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuariosServices usuariosServices;
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuariosServices usuariosServices, UsuarioRepository usuarioRepository, AuthenticationManager authenticationManager, JwtUtils jwtUtils, PasswordEncoder passwordEncoder) {
        this.usuariosServices = usuariosServices;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> addUser(@RequestBody UsuarioRequest data) {
        UsuarioResponse usuario = usuariosServices.register(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest data) {
        try {

            Authentication authentication = authenticationManager.authenticate(

                    new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword())
            );
            System.out.println(data.getPassword());

            String email = authentication.getName();
            UsuarioEntity usuario = usuarioRepository.findByEmail(email).orElseThrow(()-> new NotFoundExceptions("Usuario no encontrado"));
            String token = jwtUtils.generarToken(usuario);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales invalidas");
        }
    }


    //Buscar usuarios por id

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> userById(@PathVariable Long id) {
        UsuarioResponse usuario = usuariosServices.findUserById(id);
        return ResponseEntity.ok(usuario);
    }

    //LISTAR TODOS LOS USUARIOS
    @GetMapping("/all")
    public ResponseEntity<List<UsuarioResponse>> listarall() {
        List<UsuarioResponse> allUsers = usuariosServices.findAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    // ACTUALIZAR DATOS
    @PutMapping("/updateData/{id}")
    public ResponseEntity<UsuarioResponse> updateDataUser(@PathVariable Long id, @RequestBody UsuarioRequestUpdate data) {
        UsuarioResponse usuario = usuariosServices.updateData(id, data);


        return ResponseEntity.ok(usuario);
    }

    //EDPOINT PARA ACTUALIZAR O ASIGNAR NUEVOS ROLES
    @PatchMapping("/updateRoles/{id}")
    public ResponseEntity<UsuarioResponse> actualizarRoles(@PathVariable long id, @RequestBody UsuarioRequestUpdate data) {
        UsuarioResponse usuario = usuariosServices.updateRoles(id, data);
        return  ResponseEntity.ok(usuario);
    }
}
