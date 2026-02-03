package com.daw.tfg.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.daw.tfg.Repository.AmigoRepository;
import com.daw.tfg.models.Amistad;
import com.daw.tfg.models.Usuario;

@Service
public class AmigoService {

    private final AmigoRepository amigoRepository;

    public AmigoService(AmigoRepository amigoRepository) {
        this.amigoRepository = amigoRepository;
    }

    public List<Amistad> findAll() {
        return amigoRepository.findAll();
    }

    public Optional<Amistad> findById(Long id) {
        return amigoRepository.findById(id);
    }

    public List<Amistad> findByUsuarioOrigen(Usuario u) {
        return amigoRepository.findByUsuarioOrigen(u);
    }

    public List<Amistad> findByUsuarioDestino(Usuario u) {
        return amigoRepository.findByUsuarioDestino(u);
    }

    public Amistad save(Amistad a) {
        return amigoRepository.save(a);
    }

    public void deleteById(Long id) {
        amigoRepository.deleteById(id);
    }
}

