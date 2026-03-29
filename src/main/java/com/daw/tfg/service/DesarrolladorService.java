package com.daw.tfg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.daw.tfg.repository.DesarrolladorRepository;
import com.daw.tfg.models.Desarrollador;

@Service
public class DesarrolladorService {

    private final DesarrolladorRepository desarrolladorRepository;

    public DesarrolladorService(DesarrolladorRepository desarrolladorRepository) {
        this.desarrolladorRepository = desarrolladorRepository;
    }

    public List<Desarrollador> findAll() {
        return desarrolladorRepository.findAll();
    }

    public Optional<Desarrollador> findById(Long id) {
        return desarrolladorRepository.findById(id);
    }

    public Optional<Desarrollador> findByNombre(String nombre) {
        return desarrolladorRepository.findByNombre(nombre);
    }

    public List<Desarrollador> findByNombreContaining(String fragmento) {
        return desarrolladorRepository.findByNombreContainingIgnoreCase(fragmento);
    }

    public Desarrollador save(Desarrollador d) {
        String imagenDefault = "default_dev_" + System.currentTimeMillis() + ".jpg";
        if (d.getImagen() == null || d.getImagen().isEmpty()) {
            d.setImagen(imagenDefault);
        }
        return desarrolladorRepository.save(d);
    }

    public void deleteById(Long id) {
        desarrolladorRepository.deleteById(id);
    }
}

