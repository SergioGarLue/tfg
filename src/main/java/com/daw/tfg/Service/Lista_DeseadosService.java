package com.daw.tfg.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.daw.tfg.Repository.Lista_DeseadosRepository;
import com.daw.tfg.models.Lista_Deseados;

@Service
public class Lista_DeseadosService {

    private final Lista_DeseadosRepository listaRepository;

    public Lista_DeseadosService(Lista_DeseadosRepository listaRepository) {
        this.listaRepository = listaRepository;
    }

    public List<Lista_Deseados> findAll() {
        return listaRepository.findAll();
    }

    public Optional<Lista_Deseados> findById(Long id) {
        return listaRepository.findById(id);
    }

    public Lista_Deseados save(Lista_Deseados lista) {
        return listaRepository.save(lista);
    }

    public void deleteById(Long id) {
        listaRepository.deleteById(id);
    }
}

