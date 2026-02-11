package com.daw.tfg.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.tfg.models.PerfilUsuario;

public interface PerfilUsuarioRepository extends JpaRepository<PerfilUsuario, Long>{
    List<PerfilUsuario> findByPais(String pais);
    List<PerfilUsuario> findByEstado(Boolean estado);
    List<PerfilUsuario> findByImagenUsuarioContainingIgnoreCase(String fragmentoImagen);
} 
