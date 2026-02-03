package com.daw.tfg.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.daw.tfg.Repository.ResenaRepository;
import com.daw.tfg.models.Resena;

@Service
public class ResenaService {

    private final ResenaRepository resenaRepository;

    public ResenaService(ResenaRepository resenaRepository) {
        this.resenaRepository = resenaRepository;
    }

    public List<Resena> findAll() {
        return resenaRepository.findAll();
    }

    public Optional<Resena> findById(Long id) {
        return resenaRepository.findById(id);
    }

    public Resena save(Resena r) {
        return resenaRepository.save(r);
    }

    public void deleteById(Long id) {
        resenaRepository.deleteById(id);
    }
}

