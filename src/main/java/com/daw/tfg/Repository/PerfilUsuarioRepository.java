package com.daw.tfg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.tfg.models.PerfilUsuario;

@Repository
public interface PerfilUsuarioRepository extends JpaRepository<PerfilUsuario, Long> {
    List<PerfilUsuario> findByPais(String pais);

    List<PerfilUsuario> findByEstado(Boolean estado);

    List<PerfilUsuario> findByImagenUsuarioContainingIgnoreCase(String fragmentoImagen);
}
