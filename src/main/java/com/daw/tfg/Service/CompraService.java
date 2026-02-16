package com.daw.tfg.service;

// import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.daw.tfg.repository.CompraRepository;
import com.daw.tfg.models.Compra;

@Service
public class CompraService {

    private CompraRepository compraRepository;

    public CompraService(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }

    // esto serviría si queremos hacer un historial de compras
    // public List<Compra> findAll() {
    //     return compraRepository.findAll();
    // }

    public Optional<Compra> findById(Long id) {
        return compraRepository.findById(id);
    }

    public Compra save(Compra compra) {
        return compraRepository.save(compra);
    }
}
