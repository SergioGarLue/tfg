package com.daw.tfg.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.tfg.models.Coleccion;
import com.daw.tfg.models.Usuario;

@Repository
public interface ColeccionRepository extends JpaRepository<Coleccion, Long> {
    Optional<Coleccion> findByUsuario(Usuario usuario);
}
