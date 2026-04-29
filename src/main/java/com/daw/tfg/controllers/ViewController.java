package com.daw.tfg.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/juego/**")
    public String juego() {
        return "juego";
    }

    @GetMapping("/deseados")
    public String deseados() {
        return "deseados";
    }

    @GetMapping("/admin")
    public String admin() {
        return "administrador";
    }

    @GetMapping("/perfil")
    public String perfil() {
        return "perfil";
    }

    @GetMapping("/coleccion")
    public String coleccion() {
        return "coleccion";
    }

    @GetMapping("/tienda")
    public String tienda() {
        return "tienda";
    }

    @GetMapping("/amigos")
    public String amigos() {
        return "amigos";
    }

    @GetMapping("/carrito")
    public String carrito() {
        return "carrito";
    }

    @GetMapping("/configuracion")
    public String configuracion() {
        return "configuracion";
    }

    @GetMapping("/desarrollador")
    public String desarrollador() {
        return "desarrollador";
    }

    @GetMapping("")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registro")
    public String register() {
        return "registro";
    }

    @GetMapping("/administrador")
    public String administrador() {
        return "administrador";
    }
    @GetMapping("/success")
    public String success() {
        return "success";
    }

    @GetMapping("/pago-exitoso")
    public String pagoExitoso() {
        return "success";
    }
}
