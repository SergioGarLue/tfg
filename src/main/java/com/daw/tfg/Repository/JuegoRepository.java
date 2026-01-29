package com.daw.tfg.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.tfg.models.Juego;

@Repository
public interface JuegoRepository extends JpaRepository<Juego, Long> {

    // Buscar por título exacto
    Optional<Juego> findByTitulo(String titulo);

    // Buscar por título parecido (contiene, case-insensitive)
    List<Juego> findByTituloContainingIgnoreCase(String tituloParte);

    // Buscar por género/categoría exacto
    List<Juego> findByGenerosNombre(String nombreGenero);

    // Buscar por género/categoría parecido (contiene, case-insensitive)
    List<Juego> findByGenerosNombreContainingIgnoreCase(String nombreGeneroParte);

    // Buscar entre dos precios (inclusive)
    List<Juego> findByPrecioBetween(Float minPrecio, Float maxPrecio);

    // Buscar por desarrollador (exacto o case-insensitive)
    List<Juego> findByDesarrolladorNombreIgnoreCase(String nombreDesarrollador);

    // Buscar por editor (exacto o case-insensitive)
    List<Juego> findByEditorNombreIgnoreCase(String nombreEditor);
}

