package com.daw.tfg.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/juego")
    public String juego() {
        return "juego";
    }

    @GetMapping("/juegos/{id}")
    public String juegoDetalle() {
        // Devuelve el template, los datos se cargan con fetch en el cliente
        return "juego";
    }

    @GetMapping("/deseados")
    public String deseados() {
        return "deseados";
    }
}