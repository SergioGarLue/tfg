package com.daw.tfg.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daw.tfg.dtos.AuthResponse;
import com.daw.tfg.dtos.LoginRequest;
import com.daw.tfg.dtos.UsuarioDTO;
import com.daw.tfg.models.Usuario;
import com.daw.tfg.service.UsuarioService;

import jakarta.validation.Valid;

/**
 * Controladora REST para la gestión de autenticación de usuarios.
 * Proporciona endpoints para login, registro y consulta de usuarios.
 */
@RestController
@RequestMapping("/api/autenticacion")
public class ControladoraAutenticacion {

    private final UsuarioService usuarioService;

    public ControladoraAutenticacion(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Obtiene todos los usuarios registrados en el sistema.
     * 
     * @return Lista de todos los usuarios
     */
    @GetMapping
    public List<Usuario> getAll() {
        return usuarioService.findAll();
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * 
     * @param usuarioDTO Datos del usuario a registrar
     * @return Respuesta 201 si se registra correctamente, 400 si hay error
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {
            usuarioService.registrar(usuarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado al registrar el usuario");
        }
    }

    /**
     * Autentica a un usuario con nombre de usuario y contraseña.
     * 
     * @param loginRequest Datos de login (username y password)
     * @return Usuario autenticado o error 401 si credenciales inválidas
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Optional<Usuario> usuarioOpt = usuarioService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                // Por ahora, devolver el usuario. En producción, generar JWT.
                return ResponseEntity.ok(new AuthResponse("token_placeholder", "refresh_placeholder", usuario.getNombreUsuario()));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado durante la autenticación");
        }
    }
}
