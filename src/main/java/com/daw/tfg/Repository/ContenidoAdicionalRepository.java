package com.daw.tfg.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.tfg.models.ContenidoAdicional;

@Repository
public interface ContenidoAdicionalRepository extends JpaRepository<ContenidoAdicional, Long> {
    // Buscar por título exacto
    Optional<ContenidoAdicional> findByTitulo(String titulo);

    // Buscar por título parecido (contiene, case-insensitive)
    List<ContenidoAdicional> findByTituloContainingIgnoreCase(String tituloParte);

    // Buscar por título de juego ignorando masyus y minus
    List<ContenidoAdicional> findByJuegoTituloIgnoreCase(String tituloParte);
    // Buscar por género/categoría exacto
    List<ContenidoAdicional> findByGenerosNombre(String nombreGenero);

    // Buscar por género/categoría parecido (contiene, case-insensitive)
    List<ContenidoAdicional> findByGenerosNombreContainingIgnoreCase(String nombreGeneroParte);

    // Buscar entre dos precios (inclusive)
    List<ContenidoAdicional> findByPrecioBetween(Float minPrecio, Float maxPrecio);
}
