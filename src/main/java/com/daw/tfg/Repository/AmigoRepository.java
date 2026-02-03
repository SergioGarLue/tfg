package com.daw.tfg.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.tfg.models.Amistad;
import com.daw.tfg.models.Usuario;

public interface AmigoRepository extends JpaRepository<Amistad, Long> {

    List<Amistad> findByUsuarioOrigen(Usuario usuarioOrigen);
    List<Amistad> findByUsuarioDestino(Usuario usuarioDestino);
}

