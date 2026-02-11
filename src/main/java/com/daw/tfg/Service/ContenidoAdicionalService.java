package com.daw.tfg.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.daw.tfg.Repository.ContenidoAdicionalRepository;
import com.daw.tfg.models.ContenidoAdicional;

@Service
public class ContenidoAdicionalService {

    private final ContenidoAdicionalRepository contenidoRepository;

    public ContenidoAdicionalService(ContenidoAdicionalRepository contenidoRepository) {
        this.contenidoRepository = contenidoRepository;
    }

    public List<ContenidoAdicional> findAll() {
        return contenidoRepository.findAll();
    }

    public Optional<ContenidoAdicional> findById(Long id) {
        return contenidoRepository.findById(id);
    }

    public Optional<ContenidoAdicional> findByTitulo(String titulo) {
        return contenidoRepository.findByTitulo(titulo);
    }

    public List<ContenidoAdicional> findByTituloContaining(String fragmento) {
        return contenidoRepository.findByTituloContainingIgnoreCase(fragmento);
    }

    public List<ContenidoAdicional> findByPrecioBetween(Float min, Float max) {
        return contenidoRepository.findByPrecioBetween(min, max);
    }

    public List<ContenidoAdicional> findByJuegoTitulo(String tituloJuego) {
        return contenidoRepository.findByJuegoTituloIgnoreCase(tituloJuego);
    }

    public ContenidoAdicional save(ContenidoAdicional contenido) {
        return contenidoRepository.save(contenido);
    }

    public void deleteById(Long id) {
        contenidoRepository.deleteById(id);
    }
}


