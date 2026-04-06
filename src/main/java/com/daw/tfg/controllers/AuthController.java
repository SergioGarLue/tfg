    package com.daw.tfg.controllers;

import com.daw.tfg.dtos.UsuarioDTO;

import com.daw.tfg.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class AuthController {

    private final UsuarioService usuarioService;


    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> register(@Valid @RequestBody UsuarioDTO userDto) {
        usuarioService.registrar(userDto);
        return ResponseEntity.ok("Usuario registrado correctamente. Usa /login para acceder.");
    }




}
