package com.daw.tfg.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.daw.tfg.service.UsuarioService;
import com.daw.tfg.models.PerfilUsuario;
import com.daw.tfg.models.Usuario;
import com.daw.tfg.dtos.Perfil_UsuarioDTO;

@RestController
@RequestMapping("/api/perfil")
public class ControladoraPerfil {

    private final UsuarioService usuarioService;

    public ControladoraPerfil(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Obtiene el perfil de un usuario por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PerfilUsuario> getPerfil(@PathVariable Long id) {
        try {
            PerfilUsuario perfil = usuarioService.getPerfilByUsuarioId(id);
            return ResponseEntity.ok(perfil);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene el perfil del usuario actual (autenticado)
     */
    @GetMapping
    public ResponseEntity<PerfilUsuario> getMiPerfil(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        String username = auth.getName();
        Usuario user = usuarioService.findByNombreUsuario(username);
        PerfilUsuario perfil = usuarioService.getPerfilByUsuarioId(user.getIdUsuario());
        return ResponseEntity.ok(perfil);
    }

    /**
     * Actualiza el perfil del usuario actual 
     */
    @PutMapping
    public ResponseEntity<String> updateMiPerfil(@Valid @RequestBody Perfil_UsuarioDTO dto, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        String username = auth.getName();
        Usuario user = usuarioService.findByNombreUsuario(username);
        usuarioService.updatePerfilUsuario(user.getIdUsuario(), dto);
        return ResponseEntity.ok("Perfil actualizado correctamente");
    }

    /**
     * Actualiza solo la imagen de avatar (base64)
     */
    @PutMapping("/avatar")
    public ResponseEntity<String> updateAvatar(@RequestBody String imagenBase64, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        String username = auth.getName();
        Usuario user = usuarioService.findByNombreUsuario(username);
        Perfil_UsuarioDTO dto = new Perfil_UsuarioDTO();
        dto.setImagenUsuario(imagenBase64);
        usuarioService.updatePerfilUsuario(user.getIdUsuario(), dto);
        return ResponseEntity.ok("Avatar actualizado correctamente");
    }
}
