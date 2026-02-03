package com.daw.tfg.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.daw.tfg.Repository.Contenido_AdicionalRepository;
import com.daw.tfg.models.Contenido_Adicional;

@Service
public class Contenido_AdicionalService {

    private final Contenido_AdicionalRepository contenidoRepository;

    public Contenido_AdicionalService(Contenido_AdicionalRepository contenidoRepository) {
        this.contenidoRepository = contenidoRepository;
    }

    public List<Contenido_Adicional> findAll() {
        return contenidoRepository.findAll();
    }

    public Optional<Contenido_Adicional> findById(Long id) {
        return contenidoRepository.findById(id);
    }

    public Optional<Contenido_Adicional> findByTitulo(String titulo) {
        return contenidoRepository.findByTitulo(titulo);
    }

    public List<Contenido_Adicional> findByTituloContaining(String fragmento) {
        return contenidoRepository.findByTituloContainingIgnoreCase(fragmento);
    }

    public List<Contenido_Adicional> findByPrecioBetween(Float min, Float max) {
        return contenidoRepository.findByPrecioBetween(min, max);
    }

    public List<Contenido_Adicional> findByJuegoTitulo(String tituloJuego) {
        return contenidoRepository.findByJuegoTituloIgnoreCase(tituloJuego);
    }

    public Contenido_Adicional save(Contenido_Adicional contenido) {
        return contenidoRepository.save(contenido);
    }

    public void deleteById(Long id) {
        contenidoRepository.deleteById(id);
    }
}

