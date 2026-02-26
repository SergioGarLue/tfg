package com.daw.tfg.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.tfg.models.ColeccionFavoritos;
import com.daw.tfg.models.Juego;
import com.daw.tfg.models.Usuario;

@Repository
public interface ColeccionFavoritosRepository extends JpaRepository<ColeccionFavoritos, Long> {
    Optional<ColeccionFavoritos> findByUsuarioAndJuego(Usuario usuario, Juego juego);
    List<ColeccionFavoritos> findByUsuario(Usuario usuario);
    List<ColeccionFavoritos> findByUsuarioAndEsFavorito(Usuario usuario, Boolean esFavorito);
}
