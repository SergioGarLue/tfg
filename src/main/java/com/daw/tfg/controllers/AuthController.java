    package com.daw.tfg.controllers;

import com.daw.tfg.dtos.UsuarioDTO;
import com.daw.tfg.dtos.LoginDTO;
import com.daw.tfg.dtos.LoginResponseDTO;
import com.daw.tfg.dtos.UsuarioInfoDTO;
import com.daw.tfg.models.Usuario;
import com.daw.tfg.service.UsuarioService;
import com.daw.tfg.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioService usuarioService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDto) {
        try {
            Usuario usuario = usuarioService.findByNombreUsuario(loginDto.getUsername());
            
            if (!passwordEncoder.matches(loginDto.getPassword(), usuario.getContraseñaCifrada())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrectos");
            }

            // Crear un CustomUserDetails para la generación de JWT
            var userDetails = new org.springframework.security.core.userdetails.User(
                usuario.getNombreUsuario(),
                usuario.getContraseñaCifrada(),
                org.springframework.security.core.authority.AuthorityUtils.createAuthorityList("ROLE_" + usuario.getRol().name())
            );

            String accessToken = jwtUtil.generateAccessToken(userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);

            UsuarioInfoDTO usuarioInfo = new UsuarioInfoDTO(
                usuario.getIdUsuario(),
                usuario.getNombreUsuario(),
                usuario.getCorreoElectronico(),
                usuario.getRol().name(),
                usuario.getPerfilUsuario() != null ? usuario.getPerfilUsuario().getImagenUsuario() : null
            );

            LoginResponseDTO response = new LoginResponseDTO(accessToken, refreshToken, usuarioInfo);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrectos");
        }
    }

    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> register(@Valid @RequestBody UsuarioDTO userDto) {
        try {
            usuarioService.registrar(userDto);
            return ResponseEntity.ok("Usuario registrado correctamente. Usa /login para acceder.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/refresh")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no proporcionado");
            }

            String refreshToken = authHeader.substring(7);
            String username = jwtUtil.extractUsername(refreshToken);
            Usuario usuario = usuarioService.findByNombreUsuario(username);

            var userDetails = new org.springframework.security.core.userdetails.User(
                usuario.getNombreUsuario(),
                usuario.getContraseñaCifrada(),
                org.springframework.security.core.authority.AuthorityUtils.createAuthorityList("ROLE_" + usuario.getRol().name())
            );

            String newAccessToken = jwtUtil.generateAccessToken(userDetails);
            return ResponseEntity.ok(new java.util.HashMap<String, String>() {{
                put("accessToken", newAccessToken);
            }});
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token inválido");
        }
    }
}

