package com.daw.tfg.controllers;

import org.springframework.web.bind.annotation.*;
import com.daw.tfg.service.JuegoService;
import com.daw.tfg.models.Juego;
import java.util.List;

@RestController
@RequestMapping("/api/juegos")
public class ControladoraJuego {

    private final JuegoService juegoService;

    public ControladoraJuego(JuegoService juegoService) {
        this.juegoService = juegoService;
    }

    @GetMapping
    public List<Juego> getAll() {
        return juegoService.findAll();
    }

}
