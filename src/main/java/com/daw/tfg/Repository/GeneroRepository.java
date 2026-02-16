package com.daw.tfg.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.tfg.models.Genero;

@Repository
public interface GeneroRepository extends JpaRepository<Genero, Long> {
    Optional<Genero> findByNombre(String nombre);
    List<Genero> findByNombreContainingIgnoreCase(String fragmento);
}
