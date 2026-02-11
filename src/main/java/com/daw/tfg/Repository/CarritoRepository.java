package com.daw.tfg.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.tfg.models.Carrito;
import com.daw.tfg.models.Juego;
import com.daw.tfg.models.Usuario;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Optional<Carrito> findByIdUsuario(Usuario idUsuario);
    List<Carrito> findByJuegosContains(Juego juego);
} 
