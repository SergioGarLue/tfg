package com.daw.tfg.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.tfg.models.MetodoPago;
import com.daw.tfg.models.Usuario;

public interface MetodoPagoRepository extends JpaRepository<MetodoPago,Long>{
    List<MetodoPago> findByTipo(String tipo);
    List<MetodoPago> findByUsuario(Usuario user);
    List<MetodoPago> findByActivo(Boolean bool);
}
