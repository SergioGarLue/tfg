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

    public Juego findById(Long id) {
        Optional<Juego> juego = juegoRepository.findById(id);
        if (juego.isPresent()) {
            return juego.get();
        } else {
            throw new RuntimeException("Juego no encontrado con id: " + id);
        }
    }

    public Juego save(Juego juego) {
        return juegoRepository.save(juego);
    }

    public void deleteById(Long id) {
        juegoRepository.deleteById(id);
    }

    // Delegados a repo
    public List<Juego> findByTituloContainingIgnoreCase(String tituloParte) {
        List<Juego> juego = juegoRepository.findByTituloContainingIgnoreCase(tituloParte);
        if (juego.isEmpty()) {
            throw new RuntimeException("No se encontraron juegos con el título que contiene: " + tituloParte);
        }
        return juego;
    }

    public List<Juego> findByTituloContaining(String fragmento) {
        List<Juego> juego = juegoRepository.findByTituloContainingIgnoreCase(fragmento);
        if (juego.isEmpty()) {
            throw new RuntimeException("No se encontraron juegos con el fragmento: " + fragmento);
        }
        return juego;
    }

    public List<Juego> findByGenerosNombre(String nombreGenero) {
        List<Juego> juego = juegoRepository.findByGenerosNombre(nombreGenero);
        if (juego.isEmpty()) {
            throw new RuntimeException("No se encontraron juegos con el género: " + nombreGenero);
        }
        return juego;
    }

    public List<Juego> findByPrecioBetween(Float min, Float max) {
        if (min == max) {
            throw new RuntimeException("El precio mínimo y máximo no pueden ser iguales.");
        }
        List<Juego> juego = juegoRepository.findByPrecioBetween(min, max);
        if (juego.isEmpty()) {
            throw new RuntimeException("No se encontraron juegos con precio entre: " + min + " y " + max);
        }
        return juego;
    }

    public List<Juego> findByDesarrolladorNombre(String nombre) {
        List<Juego> juego =  juegoRepository.findByDesarrolladorNombreIgnoreCase(nombre);
        if(juego.isEmpty()) {
            throw new RuntimeException("No se encontraron juegos con el desarrollador: " + nombre);
        }
        return juego;
    }

    public List<Juego> findByEditorNombre(String nombre) {
        List<Juego> juego = juegoRepository.findByEditorNombreIgnoreCase(nombre);
        if(juego.isEmpty()) {
            throw new RuntimeException("No se encontraron juegos con el desarrollador: " + nombre);
        }
        return juego;
    }
}
