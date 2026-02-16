package com.daw.tfg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.tfg.models.Amistad;
import com.daw.tfg.models.Usuario;

import java.util.List;
import com.daw.tfg.enums.EstadoPeticion;


@Repository
public interface AmigoRepository extends JpaRepository<Amistad, Long>{
    List<Amistad> findBySolicitante(Usuario solicitante);
    List<Amistad> findByDestinatario(Usuario destinatario);
    List<Amistad> findByEstado(EstadoPeticion estado);
}
