package com.daw.tfg.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladoraVistas {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/tienda")
    public String tienda() {
        return "tienda";
    }

    @GetMapping("/carrito")
    public String carrito() {
        return "carrito";
    }

    @GetMapping("/coleccion")
    public String coleccion() {
        return "coleccion";
    }

    @GetMapping("/perfil")
    public String perfil() {
        return "perfil";
    }

    @GetMapping("/configuracion")
    public String configuracion() {
        return "configuracion";
    }
}

