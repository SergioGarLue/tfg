package com.daw.tfg.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.tfg.models.Contenido_Adicional;

@Repository
public interface Contenido_AdicionalRepository extends JpaRepository<Contenido_Adicional, Long> {

    // Buscar por título exacto
    Optional<Contenido_Adicional> findByTitulo(String titulo);

    // Buscar por título parecido (contiene, case-insensitive)
    List<Contenido_Adicional> findByTituloContainingIgnoreCase(String tituloParte);

    // Buscar entre dos precios (inclusive)
    List<Contenido_Adicional> findByPrecioBetween(Float minPrecio, Float maxPrecio);

    // Buscar por juego (por título o por id)
    List<Contenido_Adicional> findByJuegoTituloIgnoreCase(String tituloJuego);
    List<Contenido_Adicional> findByJuegoIdJuego(Long idJuego);

    // Buscar por género del juego (si lo necesitas)
    List<Contenido_Adicional> findByJuegoGenerosNombre(String nombreGenero);
    List<Contenido_Adicional> findByJuegoGenerosNombreContainingIgnoreCase(String nombreGeneroParte);
}
