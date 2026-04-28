package com.daw.tfg.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daw.tfg.dtos.ActualizarJuegoDesarrolladorDTO;
import com.daw.tfg.models.Juego;
import com.daw.tfg.service.DesarrolladorB2BService;

@RestController
@RequestMapping("/api/desarrollador")
@PreAuthorize("hasAnyRole('DEVELOPER','DESARROLLADOR')")
public class DesarrolladorController {

    private final DesarrolladorB2BService desarrolladorB2BService;

    public DesarrolladorController(DesarrolladorB2BService desarrolladorB2BService) {
        this.desarrolladorB2BService = desarrolladorB2BService;
    }

    @GetMapping("/mis-juegos")
    public ResponseEntity<List<Juego>> obtenerMisJuegos(Principal principal) {
        try {
            String nombreUsuario = principal.getName();
            List<Juego> juegos = desarrolladorB2BService.obtenerMisJuegos(nombreUsuario);
            return ResponseEntity.ok(juegos);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/juego/{idJuego}")
    public ResponseEntity<?> actualizarJuego(
            Principal principal,
            @PathVariable Long idJuego,
            @RequestBody ActualizarJuegoDesarrolladorDTO dto) {
        try {
            String nombreUsuario = principal.getName();
            Juego juegoActualizado = desarrolladorB2BService.actualizarJuego(nombreUsuario, idJuego, dto);
            return ResponseEntity.ok(juegoActualizado);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno del servidor");
        }
    }

}
