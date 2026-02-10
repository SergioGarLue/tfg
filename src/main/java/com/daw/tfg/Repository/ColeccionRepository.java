package com.daw.tfg.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.daw.tfg.models.Coleccion;
import com.daw.tfg.models.Usuario;


public interface ColeccionRepository extends JpaRepository<Coleccion, Long>{
    Optional<Coleccion> findByUsuario(Usuario usuario);
    List<Coleccion> findByUsuarioAndFechaAdquisicionBetween(Usuario usuario, LocalDate startDate, LocalDate endDate);
}
