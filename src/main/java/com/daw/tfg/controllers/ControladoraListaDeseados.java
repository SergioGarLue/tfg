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

import com.daw.tfg.models.ListaDeseados;
import com.daw.tfg.models.Usuario;
import com.daw.tfg.service.ListaDeseadosService;
import com.daw.tfg.service.UsuarioService;

/**
 * Controladora REST para la gestión de la lista de deseados.
 * Proporciona endpoints para operaciones CRUD y lógica de negocio de la lista de deseados.
 */
@RestController
@RequestMapping("/api/lista-deseados")
public class ControladoraListaDeseados {

    private final ListaDeseadosService listaDeseadosService;
    private final UsuarioService usuarioService;

    public ControladoraListaDeseados(ListaDeseadosService listaDeseadosService, UsuarioService usuarioService) {
        this.listaDeseadosService = listaDeseadosService;
        this.usuarioService = usuarioService;
    }

    /**
     * Obtiene todas las listas de deseados disponibles en el sistema.
     *
     * @return Lista de todas las listas de deseados
     */
    @GetMapping
    public List<ListaDeseados> getAll() {
        return listaDeseadosService.findAll();
    }

    /**
     * Obtiene una lista de deseados específica por su ID.
     *
     * @param id Identificador de la lista de deseados
     * @return La lista de deseados encontrada o error 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<ListaDeseados> getById(@PathVariable Long id) {
        try {
            ListaDeseados lista = listaDeseadosService.findById(id);
            return ResponseEntity.ok(lista);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene la lista de deseados asociada a un usuario específico.
     *
     * @param usuarioId Identificador del usuario
     * @return La lista de deseados del usuario o error 404 si no existe
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<ListaDeseados> getByUsuario(@PathVariable Long usuarioId) {
        try {
            Usuario usuario = usuarioService.findById(usuarioId);
            ListaDeseados lista = listaDeseadosService.findByUsuario(usuario);
            return ResponseEntity.ok(lista);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene los juegos en la lista de deseados de un usuario.
     *
     * @param usuarioId Identificador del usuario
     * @return Lista de juegos en la lista de deseados o 204 si está vacía
     */
    @GetMapping("/usuario/{usuarioId}/juegos")
    public ResponseEntity<List<?>> getGamesInLista(@PathVariable Long usuarioId) {
        try {
            List<?> juegos = (List<?>) listaDeseadosService.getAllGamesInLista(usuarioId);
            if (juegos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(juegos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene la cantidad de juegos en la lista de deseados de un usuario.
     *
     * @param usuarioId Identificador del usuario
     * @return La cantidad de juegos en la lista o error 404 si no existe
     */
    @GetMapping("/usuario/{usuarioId}/cantidad")
    public ResponseEntity<Integer> getQuantity(@PathVariable Long usuarioId) {
        try {
            int cantidad = listaDeseadosService.getQuantityGames(usuarioId);
            return ResponseEntity.ok(cantidad);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Añade un juego a la lista de deseados de un usuario.
     * Si el usuario no tiene lista de deseados, se crea una nueva.
     *
     * @param usuarioId Identificador del usuario
     * @param juegoId   Identificador del juego a añadir
     * @return Respuesta 200 si se añade correctamente, 400 si hay error
     */
    @PostMapping("/usuario/{usuarioId}/agregar/{juegoId}")
    public ResponseEntity<String> addJuego(@PathVariable Long usuarioId, @PathVariable Long juegoId) {
        try {
            listaDeseadosService.addJuegoToLista(usuarioId, juegoId);
            return ResponseEntity.ok("Juego añadido a la lista de deseados correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Elimina un juego de la lista de deseados de un usuario.
     *
     * @param usuarioId Identificador del usuario
     * @param juegoId   Identificador del juego a eliminar
     * @return Respuesta 200 si se elimina correctamente, 400 si hay error
     */
    @DeleteMapping("/usuario/{usuarioId}/eliminar/{juegoId}")
    public ResponseEntity<String> removeJuego(@PathVariable Long usuarioId, @PathVariable Long juegoId) {
        try {
            listaDeseadosService.removeJuegoFromLista(usuarioId, juegoId);
            return ResponseEntity.ok("Juego eliminado de la lista de deseados con éxito");
        } catch (IllegalArgumentException e) {
            // Error de cliente (datos incorrectos)
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Error genérico de servidor
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado al eliminar el juego");
        }
    }
}
