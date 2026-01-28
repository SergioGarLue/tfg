package com.daw.tfg.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.tfg.Enums.EstadoAmigos;
import com.daw.tfg.models.Amigo;

@Repository
public interface AmigoRepository extends JpaRepository<Amigo, Long> {
    List<Amigo> findByNombreContaining(String nombre);
    List<Amigo> findByEstado(EstadoAmigos estado);

}
