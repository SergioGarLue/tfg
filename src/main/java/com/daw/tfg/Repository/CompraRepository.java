package com.daw.tfg.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.tfg.Enums.EstadoCompra;
import com.daw.tfg.models.Compra;
import com.daw.tfg.models.Usuario;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    List<Compra> findByUsuario(Usuario usuario);
    List<Compra> findByUsuarioIdUsuario(Long idUsuario);
    List<Compra> findByFechaCompraBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin); // util si queremos hacer una lista de compras en un rango de fechas
    List<Compra> findByEstado(EstadoCompra estado);
    List<Compra> findByTotalBetween(Double minTotal, Double maxTotal);
}
