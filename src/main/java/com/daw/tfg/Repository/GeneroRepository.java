package com.daw.tfg.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.tfg.models.Genero;

public interface GeneroRepository extends JpaRepository<Genero, Long> {
    Optional<Genero> findByNombre(String nombre);
    List<Genero> findByNombreContainingIgnoreCase(String fragmento);
}
