package com.daw.tfg.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.tfg.models.Desarrollador;

@Repository
public interface DesarrolladorRepository extends JpaRepository<Desarrollador, Long> {
    Optional<Desarrollador> findByNombre(String nombre);
    List<Desarrollador> findByNombreContainingIgnoreCase(String nombreParte);
    List<Desarrollador> findByJuegoTituloContainingIgnoreCase(String tituloParte);

} 
