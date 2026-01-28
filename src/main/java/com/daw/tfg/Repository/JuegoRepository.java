package com.daw.tfg.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.tfg.models.Juego;
import java.util.List;
import com.daw.tfg.models.Genero;
import com.daw.tfg.models.Resena;



@Repository
public interface JuegoRepository extends JpaRepository<Juego, Long>{

    // Query Method para filtrar por géneros
    List<Juego> findByGeneros(List<Genero> generos);
    // Query Method para filtrar por nombres
    List<Juego> findByNombreContaining(String nombre);
    // Query Method para filtrar por rangos de precios
    List<Juego> findByPrecioBetween(int precioMin, int precioMax);

    // Query Method para filtrar por juegos con contenido explicito,
    // todavía se está barajando si en el Query hay que buscar los juegos que contengan categorías como:
    // "Violencia", "Lenguaje fuerte", "Contenido sexual", etc.
    // o si hay que filtrar por un genero en común llamado "Contenido explícito".
    // List<Juego> findByGenerosContaining(String contenidoExplicito);

    // Query Method para filtrar por calidad de reseñas
    List<Juego> findByResenas(List<Resena> resenas);
}