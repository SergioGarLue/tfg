package com.daw.tfg.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.daw.tfg.Repository.ListaDeseadosRepository;
import com.daw.tfg.models.ListaDeseados;

@Service
public class ListaDeseadosService {

    private final ListaDeseadosRepository listaRepository;

    public ListaDeseadosService(ListaDeseadosRepository listaRepository) {
        this.listaRepository = listaRepository;
    }

    public List<ListaDeseados> findAll() {
        return listaRepository.findAll();
    }

    public Optional<ListaDeseados> findById(Long id) {
        return listaRepository.findById(id);
    }

    public ListaDeseados save(ListaDeseados lista) {
        return listaRepository.save(lista);
    }

    public void deleteById(Long id) {
        listaRepository.deleteById(id);
    }
}

