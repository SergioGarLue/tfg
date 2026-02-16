package com.daw.tfg.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.tfg.models.Carrito;
import com.daw.tfg.models.Juego;
import com.daw.tfg.models.Usuario;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Optional<Carrito> findByUsuario(Usuario usuario);

    List<Carrito> findByJuegosContains(Juego juego);
}
