package com.TiendaEnLinea.TiendaEnLinea.services;

import com.TiendaEnLinea.TiendaEnLinea.Entity.RolesEntity;
import com.TiendaEnLinea.TiendaEnLinea.Entity.UsuarioEntity;
import com.TiendaEnLinea.TiendaEnLinea.Exceptions.NotFoundExceptions;
import com.TiendaEnLinea.TiendaEnLinea.Repository.RolesRepository;
import com.TiendaEnLinea.TiendaEnLinea.Repository.UsuarioRepository;
import com.TiendaEnLinea.TiendaEnLinea.dtos.UsuarioRequest;
import com.TiendaEnLinea.TiendaEnLinea.dtos.UsuarioRequestUpdate;
import com.TiendaEnLinea.TiendaEnLinea.dtos.UsuarioResponse;
import org.apache.coyote.BadRequestException;
import org.hibernate.loader.NonUniqueDiscoveredSqlAliasException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsuariosServices {
    private final UsuarioRepository usuarioRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private  final CurrentService currentService;

    public UsuariosServices(UsuarioRepository usuarioRepository, RolesRepository rolesRepository, PasswordEncoder passwordEncoder , CurrentService currentService) {
        this.usuarioRepository = usuarioRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
        this.currentService = currentService;
    }

    //Registro de usuario

    public UsuarioResponse register(UsuarioRequest data) {
        //Verificar si el usuario se encuentra registrado

        if (usuarioRepository.findByEmail(data.getEmail()) != null) {
            throw new RuntimeException("Cuenta de correo ya se encuentra registrado");
        }

        //ASIGNAR ROLES
        Optional<RolesEntity> roles = rolesRepository.findByRoleName("ROLE_CLIENTE");
        if (roles.isEmpty()) {
            throw new RuntimeException("Rol no existe en la BD");
        }


        UsuarioEntity newUsuario = new UsuarioEntity();
        newUsuario.setName(data.getName());
        newUsuario.setEmail(data.getEmail());
        newUsuario.setPassword(passwordEncoder.encode(data.getPassword()));
        newUsuario.getRoles().add(roles.get());


        UsuarioEntity usuarioGuardado = usuarioRepository.save(newUsuario);
        List<String> rolesString = usuarioGuardado.getRoles().stream().map(RolesEntity::getRoleName).toList();


        return new UsuarioResponse(usuarioGuardado.getId(), usuarioGuardado.getName(), usuarioGuardado.getEmail(), rolesString);
    }

    //BUSCAR USUARIOS POR ID
    public UsuarioResponse findUserById(Long id) {
        UsuarioEntity usuario = usuarioRepository.findById(id).orElseThrow(() -> new NotFoundExceptions("Recurso no encontrado"));

        List<String> roles = usuario.getRoles().stream().map(RolesEntity::getRoleName).toList();

        return new UsuarioResponse(usuario.getId(), usuario.getName(), usuario.getEmail(), roles);
    }

    //LISTARO TODOS LOSO USUARIOS

    public List<UsuarioResponse> findAllUsers() {
        List<UsuarioEntity> usuarios = usuarioRepository.findAll();

        return usuarios.stream().map(
                usuario -> UsuarioResponse.builder()
                        .id(usuario.getId())
                        .name(usuario.getName())
                        .email(usuario.getEmail())
                        .roles(usuario.getRoles().stream().map(RolesEntity::getRoleName).toList())
                        .build()
        ).toList();

    }

    //ACTUALIZA CAMPOS
    public UsuarioResponse updateData(Long id, UsuarioRequestUpdate data) {
        //Buscar usuario
        UsuarioEntity usuario = usuarioRepository.findById(id).orElseThrow(() -> new NotFoundExceptions("Usuario no encontrado"));

        UsuarioEntity existingEmail = usuarioRepository.findByEmail(data.getEmail()).orElseThrow(()-> new NotFoundExceptions("Email no encontrado"));
        if (existingEmail != null && !Objects.equals(existingEmail.getId(), usuario.getId())) {
            throw new RuntimeException("Email ya registrado");
        }

        usuario.setName(data.getName());
        usuario.setEmail(data.getEmail());

        //Acutualizar password si viene en el cuerpo de la solicitud

        if (data.getPassword() != null && data.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(data.getPassword()));
        }


        //Actualizar roles si es el caso
        if (data.getRoles() != null && !data.getRoles().isEmpty()) {
            Set<RolesEntity> roles = data.getRoles().stream().map(
                    rol -> rolesRepository.findByRoleName(rol).orElseThrow(() -> new NotFoundExceptions("Rol no encontrado" + rol))

            ).collect(Collectors.toSet());
            usuario.setRoles(roles);
        }

        UsuarioEntity guardado = usuarioRepository.save(usuario);


        return UsuarioResponse.builder()
                .id(guardado.getId())
                .name(guardado.getName())
                .email(guardado.getEmail())
                .roles(guardado.getRoles().stream().map(RolesEntity::getRoleName).toList())
                .build();
    }

    //Servicio para actualiar solo roles
    public UsuarioResponse updateRoles(Long id, UsuarioRequestUpdate data) {
        UsuarioEntity usuario = usuarioRepository.findById(id).orElseThrow(() -> new NotFoundExceptions("Usuario no encontrado"));

        if (data.getRoles() != null && !data.getRoles().isEmpty()) {
            Set<RolesEntity> newRoles = data.getRoles().stream().map(
                    rol -> rolesRepository.findByRoleName(rol).orElseThrow(() -> new NotFoundExceptions("Nuveo Rol no encontrado" + rol))

            ).collect(Collectors.toSet());

            usuario.setRoles(newRoles);
        }

        UsuarioEntity rolGuardado = usuarioRepository.save(usuario);

        return  UsuarioResponse.builder()
                .id(rolGuardado.getId())
                .name(rolGuardado.getName())
                .email(rolGuardado.getEmail())
                .roles(rolGuardado.getRoles().stream().map(RolesEntity::getRoleName).toList())
                .build();
    }
}