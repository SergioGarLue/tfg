package com.daw.tfg.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.tfg.models.Perfil_Usuario;

public interface Perfil_UsuarioRepository extends JpaRepository<Perfil_Usuario, Long>{
    List<Perfil_Usuario> findByPais(String pais);
    List<Perfil_Usuario> findByEstado(Boolean estado);
    List<Perfil_Usuario> findByImagenUsuarioContainingIgnoreCase(String fragmentoImagen);
} 
