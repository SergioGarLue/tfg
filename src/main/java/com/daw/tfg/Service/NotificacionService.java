package com.daw.tfg.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.daw.tfg.Repository.NotificacionRepository;
import com.daw.tfg.models.Notificacion;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    public List<Notificacion> findAll() {
        return notificacionRepository.findAll();
    }

    public Optional<Notificacion> findById(Long id) {
        return notificacionRepository.findById(id);
    }

    public Notificacion save(Notificacion n) {
        return notificacionRepository.save(n);
    }

    public void deleteById(Long id) {
        notificacionRepository.deleteById(id);
    }
}

