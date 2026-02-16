package com.daw.tfg.controllers;

import org.springframework.web.bind.annotation.*;
import com.daw.tfg.service.CarritoService;
import com.daw.tfg.models.Carrito;
import java.util.List;

@RestController
@RequestMapping("/api/carrito")
public class ControladoraCarrito {

    private final CarritoService carritoService;

    public ControladoraCarrito(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping
    public List<Carrito> getAll() {
        return carritoService.findAll();
    }

}
