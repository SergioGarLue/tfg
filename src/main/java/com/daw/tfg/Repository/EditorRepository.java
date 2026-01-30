package com.daw.tfg.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.tfg.models.Editor;

public interface EditorRepository extends JpaRepository<Editor, Long> {
    Optional<Editor> findByNombre(String nombre);
    List<Editor> findByNombreContainingIgnoreCase(String nombreParte);
    List<Editor> findByJuegoTituloContainingIgnoreCase(String tituloParte);
    List<Editor> findByJuegoId(Long idJuego);
} 
