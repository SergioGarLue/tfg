package com.daw.tfg.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.daw.tfg.service.JuegoService;
import com.daw.tfg.models.Juego;
import com.daw.tfg.dtos.JuegoDTO;
import jakarta.validation.Valid;
import java.util.List;

/**
 * Controladora REST para la gestión de juegos.
 * Proporciona endpoints para operaciones CRUD y búsquedas/filtrados de juegos.
 */
@RestController
@RequestMapping("/api/juegos")
public class ControladoraJuego {

    private final JuegoService juegoService;

    public ControladoraJuego(JuegoService juegoService) {
        this.juegoService = juegoService;
    }

    /**
     * Obtiene todos los juegos del sistema.
     * 
     * @return Lista de todos los juegos
     */
    @GetMapping
    public ResponseEntity<List<Juego>> getAll() {
        List<Juego> juegos = juegoService.findAll();
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
        Juego juego = juegoService.findById(id);
        return ResponseEntity.ok(juego);
    }

    /**
     * Crea un nuevo juego en el sistema.
     * Valida los datos del DTO antes de crear.
     * 
     * @param juegoDTO Datos del juego a crear
     * @return El juego creado
     */
    @PostMapping
    public ResponseEntity<Juego> create(@Valid @RequestBody JuegoDTO juegoDTO) {
        Juego juegoCreado = juegoService.createFromDTO(juegoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(juegoCreado);
    }

    /**
     * Actualiza un juego existente.
     * Solo actualiza los campos presentes en el DTO.
     * 
     * @param id       Identificador del juego a actualizar
     * @param juegoDTO Datos actualizados del juego
     * @return El juego actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<Juego> update(@PathVariable Long id, @Valid @RequestBody JuegoDTO juegoDTO) {
        Juego juegoActualizado = juegoService.updateFromDTO(id, juegoDTO);
        return ResponseEntity.ok(juegoActualizado);
    }

    /**
     * Elimina un juego del sistema.
     * 
     * @param id Identificador del juego a eliminar
     * @return Respuesta 204 si se elimina correctamente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        juegoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Busca juegos por título (contiene el texto buscado).
     * 
     * @param q Texto a buscar en el título
     * @return Lista de juegos que contienen el texto en el título
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Juego>> search(@RequestParam("q") String q) {
        List<Juego> juegos = juegoService.findByTituloContainingIgnoreCase(q);
        return ResponseEntity.ok(juegos);
    }

    /**
     * Filtra juegos por género.
     * 
     * @param genero Nombre del género
     * @return Lista de juegos del género especificado
     */
    @GetMapping("/genero/{genero}")
    public ResponseEntity<List<Juego>> getByGenero(@PathVariable String genero) {
        List<Juego> juegos = juegoService.findByGenerosNombre(genero);
        return ResponseEntity.ok(juegos);
    }

    /**
     * Filtra juegos por rango de precio.
     * 
     * @param min Precio mínimo
     * @param max Precio máximo
     * @return Lista de juegos dentro del rango de precio
     */
    @GetMapping("/precio")
    public ResponseEntity<List<Juego>> getByPrecio(
            @RequestParam("min") Float min, 
            @RequestParam("max") Float max) {
        List<Juego> juegos = juegoService.findByPrecioBetween(min, max);
        return ResponseEntity.ok(juegos);
    }

    /**
     * Filtra juegos por desarrollador.
     * 
     * @param desarrollador Nombre del desarrollador
     * @return Lista de juegos del desarrollador especificado
     */
    @GetMapping("/desarrollador/{desarrollador}")
    public ResponseEntity<List<Juego>> getByDesarrollador(@PathVariable String desarrollador) {
        List<Juego> juegos = juegoService.findByDesarrolladorNombre(desarrollador);
        return ResponseEntity.ok(juegos);
    }

    /**
     * Filtra juegos por editor.
     * 
     * @param editor Nombre del editor
     * @return Lista de juegos del editor especificado
     */
    @GetMapping("/editor/{editor}")
    public ResponseEntity<List<Juego>> getByEditor(@PathVariable String editor) {
        List<Juego> juegos = juegoService.findByEditorNombre(editor);
        return ResponseEntity.ok(juegos);
    }
}

