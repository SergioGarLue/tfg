package com.daw.tfg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.daw.tfg.models.MetodoPago;
import com.daw.tfg.models.Usuario;
import com.daw.tfg.repository.MetodoPagoRepository;

@Service
public class MetodoPagoService {

    private final MetodoPagoRepository metodoPagoRepository;

    public MetodoPagoService(MetodoPagoRepository metodoPagoRepository) {
        this.metodoPagoRepository = metodoPagoRepository;
    }

    public List<MetodoPago> findAll() {
        return metodoPagoRepository.findAll();
    }

    public MetodoPago findById(Long id) {
        Optional<MetodoPago> mp = metodoPagoRepository.findById(id);
        if (mp.isEmpty()) {
            throw new IllegalArgumentException("Metodo de pago no encontrado");
        }
        return mp.get();
    }

    public List<MetodoPago> findByUsuario(Usuario u) {
        if (u == null) {
            throw new IllegalArgumentException("Usuario inválido");
        }
        List<MetodoPago> list = metodoPagoRepository.findByUsuario(u);
        return list;
    }

    public List<MetodoPago> findByTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("Tipo de método de pago inválido");
        }
        return metodoPagoRepository.findByTipo(tipo);
    }

    public List<MetodoPago> findByActivo(Boolean activo) {
        if (activo == null) {
            throw new IllegalArgumentException("Parámetro 'activo' inválido");
        }
        return metodoPagoRepository.findByActivo(activo);
    }

    public MetodoPago save(MetodoPago metodoPago) {
        if (metodoPago == null) {
            throw new IllegalArgumentException("MetodoPago inválido");
        }
        // Validaciones básicas: proveedor y token obligatorios
        if (metodoPago.getProveedor() == null || metodoPago.getProveedor().isBlank()) {
            throw new IllegalArgumentException("Proveedor del método de pago es obligatorio");
        }
        if (metodoPago.getToken() == null || metodoPago.getToken().isBlank()) {
            throw new IllegalArgumentException("Token del método de pago es obligatorio");
        }
        return metodoPagoRepository.save(metodoPago);
    }

    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id inválido");
        }
        if (metodoPagoRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Metodo de pago no encontrado");
        }
        metodoPagoRepository.deleteById(id);
    }
}
