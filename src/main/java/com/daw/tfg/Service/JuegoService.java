package com.daw.tfg.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.daw.tfg.Repository.JuegoRepository;
import com.daw.tfg.models.Juego;

@Service
public class JuegoService {

    private final JuegoRepository juegoRepository;

    public JuegoService(JuegoRepository juegoRepository) {
        this.juegoRepository = juegoRepository;
    }

    public List<Juego> findAll() {
        return juegoRepository.findAll();
    }

    public Optional<Juego> findById(Long id) {
        return juegoRepository.findById(id);
    }

    public Juego save(Juego juego) {
        return juegoRepository.save(juego);
    }

    public void deleteById(Long id) {
        juegoRepository.deleteById(id);
    }

    // Delegados a repo
    public Optional<Juego> findByTitulo(String titulo) {
        return juegoRepository.findByTitulo(titulo);
    }

    public List<Juego> findByTituloContaining(String fragmento) {
        return juegoRepository.findByTituloContainingIgnoreCase(fragmento);
    }

    public List<Juego> findByGeneroNombre(String nombreGenero) {
        return juegoRepository.findByGenerosNombre(nombreGenero);
    }

    public List<Juego> findByPrecioBetween(Float min, Float max) {
        return juegoRepository.findByPrecioBetween(min, max);
    }

    public List<Juego> findByDesarrolladorNombre(String nombre) {
        return juegoRepository.findByDesarrolladorNombreIgnoreCase(nombre);
    }

    public List<Juego> findByEditorNombre(String nombre) {
        return juegoRepository.findByEditorNombreIgnoreCase(nombre);
    }
}

