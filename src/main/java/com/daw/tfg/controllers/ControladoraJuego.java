package com.daw.tfg.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daw.tfg.models.Juego;
import com.daw.tfg.service.JuegoService;

/**
 * Controladora REST para la gestión de juegos.
 * Proporciona endpoints para operaciones CRUD y consultas de juegos.
 */
@RestController
@RequestMapping("/api/juegos")
public class ControladoraJuego {

    private final JuegoService juegoService;

    public ControladoraJuego(JuegoService juegoService) {
        this.juegoService = juegoService;
    }

    /**
     * Obtiene todos los juegos disponibles en el sistema.
     *
     * @return Lista de todos los juegos
     */
    @GetMapping
    public List<Juego> getAll() {
        return juegoService.findAll();
    }

    /**
     * Obtiene un juego específico por su ID.
     *
     * @param id Identificador del juego
     * @return El juego encontrado o error 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Juego> getById(@PathVariable Long id) {
        try {
            Juego juego = juegoService.findById(id);
            return ResponseEntity.ok(juego);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Busca juegos por título (búsqueda parcial, insensible a mayúsculas).
     *
     * @param titulo Fragmento del título a buscar
     * @return Lista de juegos que contienen el fragmento en el título
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Juego>> searchByTitulo(@RequestParam String titulo) {
        try {
            List<Juego> juegos = juegoService.findByTituloContainingIgnoreCase(titulo);
            return ResponseEntity.ok(juegos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtiene juegos por género.
     *
     * @param genero Nombre del género
     * @return Lista de juegos del género especificado
     */
    @GetMapping("/genero/{genero}")
    public ResponseEntity<List<Juego>> getByGenero(@PathVariable String genero) {
        try {
            List<Juego> juegos = juegoService.findByGenerosNombre(genero);
            return ResponseEntity.ok(juegos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtiene juegos dentro de un rango de precios.
     *
     * @param min Precio mínimo
     * @param max Precio máximo
     * @return Lista de juegos en el rango de precios
     */
    @GetMapping("/precio")
    public ResponseEntity<List<Juego>> getByPrecioRange(@RequestParam Double min, @RequestParam Double max) {
        try {
            List<Juego> juegos = juegoService.findByPrecioBetween(min, max);
            return ResponseEntity.ok(juegos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtiene juegos por desarrollador.
     *
     * @param desarrollador Nombre del desarrollador
     * @return Lista de juegos del desarrollador especificado
     */
    @GetMapping("/desarrollador/{desarrollador}")
    public ResponseEntity<List<Juego>> getByDesarrollador(@PathVariable String desarrollador) {
        try {
            List<Juego> juegos = juegoService.findByDesarrolladorNombre(desarrollador);
            return ResponseEntity.ok(juegos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtiene juegos por editor.
     *
     * @param editor Nombre del editor
     * @return Lista de juegos del editor especificado
     */
    @GetMapping("/editor/{editor}")
    public ResponseEntity<List<Juego>> getByEditor(@PathVariable String editor) {
        try {
            List<Juego> juegos = juegoService.findByEditorNombre(editor);
            return ResponseEntity.ok(juegos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Crea un nuevo juego.
     *
     * @param juego Datos del juego a crear
     * @return El juego creado o error 400 si hay datos inválidos
     */
    @PostMapping
    public ResponseEntity<Juego> create(@RequestBody Juego juego) {
        try {
            Juego nuevoJuego = juegoService.save(juego);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoJuego);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualiza un juego existente.
     *
     * @param id     Identificador del juego
     * @param cambios Datos a actualizar
     * @return El juego actualizado o error si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<Juego> update(@PathVariable Long id, @RequestBody Juego cambios) {
        try {
            Juego juegoActualizado = juegoService.update(id, cambios);
            return ResponseEntity.ok(juegoActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Elimina un juego por su ID.
     *
     * @param id Identificador del juego
     * @return Respuesta 204 si se elimina correctamente, 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            juegoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
