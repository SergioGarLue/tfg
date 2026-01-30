package com.daw.tfg.Repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.tfg.models.Resena;
import com.daw.tfg.models.Juego;
import com.daw.tfg.models.Usuario;

public interface ResenaRepository extends JpaRepository<Resena, Long> {
    List<Resena> findByJuego(Juego juego);
    List<Resena> findByJuegoId(Long idJuego);
    List<Resena> findByUsuario(Usuario usuario);
    List<Resena> findByUsuarioId(Long idUsuario);
    List<Resena> findByValoracionBetween(BigDecimal minValor, BigDecimal maxValor);
} 
