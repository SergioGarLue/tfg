package com.daw.tfg.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.daw.tfg.Repository.ColeccionRepository;
import com.daw.tfg.models.Coleccion;

@Service
public class ColeccionService {

    private final ColeccionRepository coleccionRepository;

    public ColeccionService(ColeccionRepository coleccionRepository) {
        this.coleccionRepository = coleccionRepository;
    }

    public List<Coleccion> findAll() {
        return coleccionRepository.findAll();
    }

    public Optional<Coleccion> findById(Long id) {
        return coleccionRepository.findById(id);
    }

    public Coleccion save(Coleccion coleccion) {
        return coleccionRepository.save(coleccion);
    }

    public void deleteById(Long id) {
        coleccionRepository.deleteById(id);
    }
}

