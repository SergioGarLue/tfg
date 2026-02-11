package com.daw.tfg.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.tfg.models.Juego;
import com.daw.tfg.models.Lista_Deseados;
import com.daw.tfg.models.Usuario;

public interface ListaDeseadosRepository extends JpaRepository<Lista_Deseados, Long> {
    Optional<Lista_Deseados> findByUsuario(Usuario usuario);
    Optional<Lista_Deseados> findByUsuarioIdUsuario(Long idUsuario);
    List<Lista_Deseados> findByJuegosContains(Juego juego);
}
