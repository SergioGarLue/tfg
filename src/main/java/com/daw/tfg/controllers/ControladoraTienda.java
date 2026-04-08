package com.daw.tfg.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daw.tfg.models.Juego;
import com.daw.tfg.service.JuegoService;

/**
 * Controladora REST para la gestión de la tienda de juegos.
 * Proporciona endpoints para consultar juegos disponibles en la tienda.
 */
@RestController
@RequestMapping("/api/tienda")
public class ControladoraTienda {

    private final JuegoService juegoService;

    public ControladoraTienda(JuegoService juegoService) {
        this.juegoService = juegoService;
    }

    /**
     * Obtiene todos los juegos disponibles en la tienda.
     * 
     * @return Lista de todos los juegos o 204 si no hay juegos disponibles
     */
    @GetMapping
    public ResponseEntity<List<Juego>> getAll() {
        List<Juego> juegos = juegoService.findAll();
        if (juegos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(juegos);
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
     * Busca juegos por título (contiene el fragmento especificado, ignorando mayúsculas).
     * 
     * @param titulo Fragmento del título a buscar
     * @return Lista de juegos que coinciden o 204 si no se encuentran
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Juego>> getByTitulo(@RequestParam String titulo) {
        try {
            List<Juego> juegos = juegoService.findByTituloContainingIgnoreCase(titulo);
            if (juegos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(juegos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Obtiene juegos por género.
     * 
     * @param genero Nombre del género
     * @return Lista de juegos del género especificado o 204 si no se encuentran
     */
    @GetMapping("/genero/{genero}")
    public ResponseEntity<List<Juego>> getByGenero(@PathVariable String genero) {
        try {
            List<Juego> juegos = juegoService.findByGenerosNombre(genero);
            if (juegos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(juegos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Obtiene juegos dentro de un rango de precios.
     * 
     * @param min Precio mínimo
     * @param max Precio máximo
     * @return Lista de juegos en el rango de precios o 204 si no se encuentran
     */
    @GetMapping("/precio")
    public ResponseEntity<List<Juego>> getByPrecioBetween(@RequestParam Double min, @RequestParam Double max) {
        try {
            List<Juego> juegos = juegoService.findByPrecioBetween(min, max);
            if (juegos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(juegos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Obtiene juegos por desarrollador.
     * 
     * @param desarrollador Nombre del desarrollador
     * @return Lista de juegos del desarrollador especificado o 204 si no se encuentran
     */
    @GetMapping("/desarrollador/{desarrollador}")
    public ResponseEntity<List<Juego>> getByDesarrollador(@PathVariable String desarrollador) {
        try {
            List<Juego> juegos = juegoService.findByDesarrolladorNombre(desarrollador);
            if (juegos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(juegos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Obtiene juegos por editor.
     * 
     * @param editor Nombre del editor
     * @return Lista de juegos del editor especificado o 204 si no se encuentran
     */
    @GetMapping("/editor/{editor}")
    public ResponseEntity<List<Juego>> getByEditor(@PathVariable String editor) {
        try {
            List<Juego> juegos = juegoService.findByEditorNombre(editor);
            if (juegos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(juegos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
