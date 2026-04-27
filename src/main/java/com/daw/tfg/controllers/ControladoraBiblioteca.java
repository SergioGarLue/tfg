package com.daw.tfg.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daw.tfg.models.ColeccionFavoritos;
import com.daw.tfg.models.Juego;
import com.daw.tfg.service.ColeccionService;
import com.daw.tfg.service.UsuarioService;

/**
 * Controladora REST para la gestión de la biblioteca de juegos del usuario.
 * Proporciona endpoints para operaciones CRUD y lógica de negocio sobre la colección de juegos.
 */
@RestController
@RequestMapping("/api/biblioteca")
public class ControladoraBiblioteca {

    private final ColeccionService coleccionService;
    private final UsuarioService usuarioService;

    public ControladoraBiblioteca(ColeccionService coleccionService, UsuarioService usuarioService) {
        this.coleccionService = coleccionService;
        this.usuarioService = usuarioService;
    }

    /**
     * Obtiene todos los items de colección disponibles en el sistema.
     * 
     * @return Lista de todos los items de colección
     */
    @GetMapping
    public List<ColeccionFavoritos> getAll() {
        return coleccionService.findAll();
    }

    /**
     * Obtiene un item de colección específico por su ID.
     * 
     * @param id Identificador del item de colección
     * @return El item encontrado o error 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<ColeccionFavoritos> getById(@PathVariable Long id) {
        try {
            ColeccionFavoritos item = coleccionService.findById(id);
            return ResponseEntity.ok(item);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene la biblioteca (colección de juegos) completa de un usuario.
     * 
     * @param usuarioId Identificador del usuario
     * @return Lista de items en la biblioteca del usuario o 204 si está vacía
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ColeccionFavoritos>> getBibliotecaUsuario(@PathVariable Long usuarioId) {
        try {
            usuarioService.findById(usuarioId);
            List<ColeccionFavoritos> biblioteca = coleccionService.findByUsuario(usuarioId);
            if (biblioteca.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(biblioteca);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene todos los juegos en la biblioteca (colección) de un usuario.
     * 
     * @param usuarioId Identificador del usuario
     * @return Lista de juegos en la biblioteca o 204 si está vacía
     */
    @GetMapping("/usuario/{usuarioId}/juegos")
    public ResponseEntity<List<Juego>> getGamesInBiblioteca(@PathVariable Long usuarioId) {
        try {
            List<Juego> juegos = coleccionService.getAllGamesInCollection(usuarioId);
            if (juegos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(juegos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{usuarioId}/recientes")
    public ResponseEntity<List<ColeccionFavoritos>> getRecentPurchasedGames(@PathVariable Long usuarioId) {
        try {
            List<ColeccionFavoritos> recientes = coleccionService.findRecentByUsuario(usuarioId, 3);
            if (recientes.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(recientes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene los juegos marcados como favoritos de un usuario.
     * 
     * @param usuarioId Identificador del usuario
     * @return Lista de juegos favoritos o 204 si no tiene favoritos
     */
    @GetMapping("/usuario/{usuarioId}/favoritos")
    public ResponseEntity<List<Juego>> getFavoritosByUsuario(@PathVariable Long usuarioId) {
        try {
            List<Juego> favoritos = coleccionService.getFavoritesFromCollection(usuarioId);
            if (favoritos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(favoritos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Añade un juego a la biblioteca de un usuario.
     * 
     * @param usuarioId Identificador del usuario
     * @param juegoId   Identificador del juego a añadir
     * @return Respuesta 200 si se añade correctamente, 400 si hay error
     */
    @PostMapping("/usuario/{usuarioId}/agregar/{juegoId}")
    public ResponseEntity<String> addJuego(@PathVariable Long usuarioId, @PathVariable Long juegoId) {
        try {
            coleccionService.addJuegoToCollection(usuarioId, juegoId);
            return ResponseEntity.ok("Juego añadido a la biblioteca correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Marca un juego como favorito en la biblioteca del usuario.
     * 
     * @param usuarioId Identificador del usuario
     * @param juegoId   Identificador del juego a marcar como favorito
     * @return Respuesta 200 si se marca correctamente, 400 si hay error
     */
    @PostMapping("/usuario/{usuarioId}/favorito/{juegoId}")
    public ResponseEntity<String> markAsFavorite(@PathVariable Long usuarioId, @PathVariable Long juegoId) {
        try {
            coleccionService.markAsFavorite(usuarioId, juegoId);
            return ResponseEntity.ok("Juego marcado como favorito");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Desmarca un juego como favorito en la biblioteca del usuario.
     * 
     * @param usuarioId Identificador del usuario
     * @param juegoId   Identificador del juego a desmarcar como favorito
     * @return Respuesta 200 si se desmarca correctamente, 400 si hay error
     */
    @PostMapping("/usuario/{usuarioId}/desfavorito/{juegoId}")
    public ResponseEntity<String> unmarkAsFavorite(@PathVariable Long usuarioId, @PathVariable Long juegoId) {
        try {
            coleccionService.unmarkAsFavorite(usuarioId, juegoId);
            return ResponseEntity.ok("Juego desmarcado como favorito");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Elimina un juego de la biblioteca de un usuario.
     * 
     * @param usuarioId Identificador del usuario
     * @param juegoId   Identificador del juego a eliminar
     * @return Respuesta 200 si se elimina correctamente, 400 si hay error
     */
    @DeleteMapping("/usuario/{usuarioId}/eliminar/{juegoId}")
    public ResponseEntity<String> removeJuego(@PathVariable Long usuarioId, @PathVariable Long juegoId) {
        try {
            coleccionService.removeJuegoFromCollection(usuarioId, juegoId);
            return ResponseEntity.ok("Juego eliminado de la biblioteca correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado al eliminar el juego");
        }
    }

    /**
     * Obtiene la cantidad total de juegos en la biblioteca de un usuario.
     * 
     * @param usuarioId Identificador del usuario
     * @return El número total de juegos en la biblioteca
     */
    @GetMapping("/usuario/{usuarioId}/cantidad")
    public ResponseEntity<Integer> getQuantityGames(@PathVariable Long usuarioId) {
        try {
            List<Juego> juegos = coleccionService.getAllGamesInCollection(usuarioId);
            return ResponseEntity.ok(juegos.size());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
