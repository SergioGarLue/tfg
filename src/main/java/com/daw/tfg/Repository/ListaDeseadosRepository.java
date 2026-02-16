package com.daw.tfg.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.tfg.models.Juego;
import com.daw.tfg.models.ListaDeseados;
import com.daw.tfg.models.Usuario;

@Repository
public interface ListaDeseadosRepository extends JpaRepository<ListaDeseados, Long> {
    Optional<ListaDeseados> findByUsuario(Usuario usuario);

    Optional<ListaDeseados> findByUsuarioIdUsuario(Long idUsuario);

    List<ListaDeseados> findByJuegosContains(Juego juego);
}
