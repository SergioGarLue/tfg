package com.daw.tfg.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.daw.tfg.models.Juego;
import com.daw.tfg.repository.JuegoRepository;

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
        if (id == null) {
            throw new IllegalArgumentException("Id de juego inválido");
        }
        return juegoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Juego no encontrado con id: " + id));
    }

    public Juego save(Juego juego) {
        if (juego == null) {
            throw new IllegalArgumentException("Juego inválido");
        }
        if (juego.getTitulo() == null || juego.getTitulo().isBlank()) {
            throw new IllegalArgumentException("El título del juego es obligatorio");
        }
        if (juego.getPrecio() == null || juego.getPrecio() < 0) {
            throw new IllegalArgumentException("El precio del juego es obligatorio y no puede ser negativo");
        }
        if (juego.getDesarrollador() == null) {
            throw new IllegalArgumentException("Desarrollador obligatorio");
        }
        // Editor ahora es opcional para importación JSON inicial
        return juegoRepository.save(juego);
    }

    public boolean existsById(Long id) {
        return juegoRepository.existsById(id);
    }

    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id inválido");
        }
        if (juegoRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Juego no encontrado con id: " + id);
        }
        juegoRepository.deleteById(id);
    }

    // Delegados a repo
    public List<Juego> findByTituloContainingIgnoreCase(String tituloParte) {
        if (tituloParte == null || tituloParte.isBlank()) {
            throw new IllegalArgumentException("Fragmento de título inválido");
        }
        List<Juego> juego = juegoRepository.findByTituloContainingIgnoreCase(tituloParte);
        if (juego.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron juegos con el título que contiene: " + tituloParte);
        }
        return juego;
    }

    public List<Juego> findByTituloContaining(String fragmento) {
        return findByTituloContainingIgnoreCase(fragmento);
    }

    public List<Juego> findByGenerosNombre(String nombreGenero) {
        if (nombreGenero == null || nombreGenero.isBlank()) {
            throw new IllegalArgumentException("Nombre de género inválido");
        }
        List<Juego> juego = juegoRepository.findByGenerosNombre(nombreGenero);
        if (juego.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron juegos con el género: " + nombreGenero);
        }
        return juego;
    }

    public List<Juego> findByPrecioBetween(Double min, Double max) {
        if (min == null || max == null) {
            throw new IllegalArgumentException("Rango de precio inválido");
        }
        if (Objects.equals(min, max)) {
            throw new IllegalArgumentException("El precio mínimo y máximo no pueden ser iguales.");
        }
        if (min > max) {
            throw new IllegalArgumentException("El precio mínimo no puede ser mayor que el máximo");
        }
        List<Juego> juego = juegoRepository.findByPrecioBetween(min, max);
        if (juego.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron juegos con precio entre: " + min + " y " + max);
        }
        return juego;
    }

    public List<Juego> findByDesarrolladorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Nombre de desarrollador inválido");
        }
        List<Juego> juego =  juegoRepository.findByDesarrolladorNombreIgnoreCase(nombre);
        if(juego.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron juegos con el desarrollador: " + nombre);
        }
        return juego;
    }

    public List<Juego> findByEditorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Nombre de editor inválido");
        }
        List<Juego> juego = juegoRepository.findByEditorNombreIgnoreCase(nombre);
        if(juego.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron juegos con el editor: " + nombre);
        }
        return juego;
    }

    public Optional<Juego> findOptionalById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return juegoRepository.findById(id);
    }

    public Juego update(Long id, Juego cambios) {
        Juego existente = findById(id);
        if (cambios == null) {
            throw new IllegalArgumentException("Datos de actualización inválidos");
        }
        if (cambios.getTitulo() != null && !cambios.getTitulo().isBlank()) {
            existente.setTitulo(cambios.getTitulo());
        }
        if (cambios.getPrecio() != null) {
            if (cambios.getPrecio() < 0) {
                throw new IllegalArgumentException("El precio no puede ser negativo");
            }
            existente.setPrecio(cambios.getPrecio());
        }
        if (cambios.getDescripcion() != null) {
            existente.setDescripcion(cambios.getDescripcion());
        }
        if (cambios.getImagen() != null) {
            existente.setImagen(cambios.getImagen());
        }
        if (cambios.getScreenshots() != null) {
            existente.setScreenshots(cambios.getScreenshots());
        }
        if (cambios.getPesoJuego() != null) {
            existente.setPesoJuego(cambios.getPesoJuego());
        }
        if (cambios.getPlataformas() != null) {
            existente.setPlataformas(cambios.getPlataformas());
        }
        if (cambios.getGeneros() != null) {
            existente.setGeneros(cambios.getGeneros());
        }
        if (cambios.getDesarrollador() != null) {
            existente.setDesarrollador(cambios.getDesarrollador());
        }
        if (cambios.getEditor() != null) {
            existente.setEditor(cambios.getEditor());
        }
        return save(existente);
    }
}
