package com.daw.tfg.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.tfg.models.Juego;

@Repository
public interface JuegoRepository extends JpaRepository<Juego, Long> {

    Optional<Juego> findById(Long id);
    
    // Buscar por título parecido (contiene, case-insensitive)
    List<Juego> findByTituloContainingIgnoreCase(String tituloParte);

    // Buscar por género/categoría exacto
    List<Juego> findByGenerosNombre(String nombreGenero);

    // Buscar entre dos precios (inclusive)
    List<Juego> findByPrecioBetween(Double minPrecio, Double maxPrecio);

    // Buscar por desarrollador (exacto o case-insensitive)
    List<Juego> findByDesarrolladorNombreIgnoreCase(String nombreDesarrollador);

    // Buscar por editor (exacto o case-insensitive)
    List<Juego> findByEditorNombreIgnoreCase(String nombreEditor);

    // Buscar los primeros 100 juegos por orden de id (populares)
    List<Juego> findTop100ByOrderByIdJuegoAsc();

    // Buscar juegos gratis
    List<Juego> findByPrecio(Double precio);

    // Buscar por género ignorando mayúsculas
    List<Juego> findByGenerosNombreIgnoreCase(String nombreGenero);

    // Ofertas con porcentaje de descuento mayor o igual
    List<Juego> findByPorcentajeGreaterThanEqual(Integer porcentaje);
}
