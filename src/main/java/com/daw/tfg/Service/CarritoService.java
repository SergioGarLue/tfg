package com.daw.tfg.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.daw.tfg.Repository.CarritoRepository;
import com.daw.tfg.models.Carrito;
import com.daw.tfg.models.Juego;
import com.daw.tfg.models.Usuario;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepository;

    public CarritoService(CarritoRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    public List<Carrito> findAll() {
        return carritoRepository.findAll();
    }

    public Optional<Carrito> findById(Long id) {
        return carritoRepository.findById(id);
    }

    public Optional<Carrito> findByIdCarrito(Long idCarrito) {
        return carritoRepository.findByIdCarrito(idCarrito);
    }

    public Optional<Carrito> findByUsuario(Usuario usuario) {
        return carritoRepository.findByIdUsuario(usuario);
    }

    public List<Carrito> findByJuegoContains(Juego juego) {
        return carritoRepository.findByJuegosContains(juego);
    }

    public Carrito save(Carrito carrito) {
        return carritoRepository.save(carrito);
    }

    public void deleteById(Long id) {
        carritoRepository.deleteById(id);
    }
}

