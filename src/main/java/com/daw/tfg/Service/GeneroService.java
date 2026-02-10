package com.daw.tfg.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.daw.tfg.Repository.GeneroRepository;
import com.daw.tfg.models.Genero;

@Service
public class GeneroService {

    private final GeneroRepository generoRepository;

    public GeneroService(GeneroRepository generoRepository) {
        this.generoRepository = generoRepository;
    }

    public List<Genero> findAll() {
        return generoRepository.findAll();
    }

    public Optional<Genero> findById(Long id) {
        return generoRepository.findById(id);
    }

    public Optional<Genero> findByNombre(String nombre) {
        return generoRepository.findByNombre(nombre);
    }

    public List<Genero> findByNombreContaining(String fragmento) {
        return generoRepository.findByNombreContainingIgnoreCase(fragmento);
    }

    public Genero save(Genero g) {
        return generoRepository.save(g);
    }

    public void deleteById(Long id) {
        generoRepository.deleteById(id);
    }
}

