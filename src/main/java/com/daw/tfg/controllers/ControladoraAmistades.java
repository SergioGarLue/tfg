package com.daw.tfg.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daw.tfg.models.Amistad;
import com.daw.tfg.models.Usuario;
import com.daw.tfg.service.AmigoService;
import com.daw.tfg.service.UsuarioService;

/**
 * Controladora REST para gestión de amistades y solicitudes.
 * Basada en la estructura de ControladoraCarrito.
 */
@RestController
@RequestMapping("/api/amistades")
public class ControladoraAmistades {

    private final AmigoService amigoService;
    private final UsuarioService usuarioService;

    public ControladoraAmistades(AmigoService amigoService, UsuarioService usuarioService) {
        this.amigoService = amigoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Amistad> getAll() {
        return amigoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Amistad> getById(@PathVariable Long id) {
        Amistad amistad = amigoService.findById(id);
        if (amistad == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(amistad);
    }

    @GetMapping("/solicitante/{usuarioId}")
    public ResponseEntity<List<Amistad>> getBySolicitante(@PathVariable Long usuarioId) {
        try {
            Usuario usuario = usuarioService.findById(usuarioId);
            List<Amistad> lista = amigoService.findByUsuarioSolicitante(usuario);
            if (lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/destinatario/{usuarioId}")
    public ResponseEntity<List<Amistad>> getByDestinatario(@PathVariable Long usuarioId) {
        try {
            Usuario usuario = usuarioService.findById(usuarioId);
            List<Amistad> lista = amigoService.findByUsuarioDestino(usuario);
            if (lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/enviar")
    public ResponseEntity<String> sendFriendRequest(@RequestParam Long solicitanteId,
                                                    @RequestParam Long destinatarioId) {
        try {
            amigoService.sendFriendRequest(solicitanteId, destinatarioId);
            return ResponseEntity.ok("Solicitud de amistad enviada correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al enviar la solicitud de amistad");
        }
    }

    @PostMapping("/aceptar/{amistadId}")
    public ResponseEntity<String> acceptFriendRequest(@PathVariable Long amistadId,
                                                      @RequestParam Long destinatarioId) {
        try {
            amigoService.acceptFriendRequest(amistadId, destinatarioId);
            return ResponseEntity.ok("Solicitud de amistad aceptada");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al aceptar la solicitud");
        }
    }

    @PostMapping("/rechazar/{amistadId}")
    public ResponseEntity<String> rejectFriendRequest(@PathVariable Long amistadId,
                                                      @RequestParam Long destinatarioId) {
        try {
            amigoService.rejectFriendRequest(amistadId, destinatarioId);
            return ResponseEntity.ok("Solicitud de amistad rechazada");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al rechazar la solicitud");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        try {
            amigoService.deleteById(id);
            return ResponseEntity.ok("Amistad eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al eliminar la amistad");
        }
    }

    @GetMapping("/usuario/{usuarioId}/aceptadas")
    public ResponseEntity<List<Usuario>> obtenerAmigosAceptados(@PathVariable Long usuarioId) {
        try {
            List<Usuario> amigos = amigoService.obtenerAmigosAceptados(usuarioId);
            if (amigos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(amigos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/usuario/{usuarioId}/pendientes")
    public ResponseEntity<List<Amistad>> obtenerSolicitudesPendientes(@PathVariable Long usuarioId) {
        try {
            List<Amistad> solicitudes = amigoService.obtenerSolicitudesPendientes(usuarioId);
            if (solicitudes.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(solicitudes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

