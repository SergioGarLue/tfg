package com.daw.tfg.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.tfg.models.Editor;

@Repository
public interface EditorRepository extends JpaRepository<Editor, Long> {
    Optional<Editor> findByNombre(String nombre);

    List<Editor> findByNombreContainingIgnoreCase(String nombreParte);

    List<Editor> findByJuegoTituloContainingIgnoreCase(String tituloParte);

    List<Editor> findByJuegoIdJuego(Long idJuego);
}
