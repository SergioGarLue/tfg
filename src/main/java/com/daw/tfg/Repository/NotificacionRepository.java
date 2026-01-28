package com.daw.tfg.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.tfg.models.Notificacion;
import java.util.List;
import com.daw.tfg.models.Usuario;


public interface NotificacionRepository extends JpaRepository<Notificacion, Long>{
    List<Notificacion> findByUsuario(Usuario usuario);
    // para las notis de estado de amgigos
    // List<Notificacion> finByAmigo(Usuario usuario);
}
