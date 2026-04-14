package com.daw.tfg.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.security.Principal;
import com.daw.tfg.dtos.UsuarioDTO;

@Controller
@RequestMapping("/")
public class VistaController {

    @GetMapping("")
    public String index() {
        return "index";
    }

    @GetMapping("/tienda")
    public String tienda() {
        return "tienda";
    }

    @GetMapping("/carrito")
    public String carrito(Principal principal) {
        // if (principal == null) {
        //     return "redirect:/login";
        // }
        return "carrito";
    }

    @GetMapping("/perfil")
    public String perfil(Principal principal) {
        // if (principal == null) {
        //     return "redirect:/login";
        // }
        return "perfil";
    }

    @GetMapping("/amigos")
    public String amigos(Principal principal) {
        // if (principal == null) {
        //     return "redirect:/login";
        // }
        return "amigos";
    }

    @GetMapping("/coleccion")
    public String coleccion(Principal principal) {
        // if (principal == null) {
        //     return "redirect:/login";
        // }
        return "coleccion";
    }

    @GetMapping("/configuracion")
    public String configuracion(Principal principal) {
        // if (principal == null) {
        //     return "redirect:/login";
        // }
        return "configuracion";
    }

    @GetMapping("/desarrollador")
    public String desarrollador() {
        return "desarrollador";
    }

    @GetMapping("/juego/{id}")
    public String juego(@PathVariable("id") int juegoId, Model model) {
        model.addAttribute("juegoId", juegoId);
        return "juego";
    }

@GetMapping("/registro")
    public String register(Model model) {
        model.addAttribute("usuarioDTO", new com.daw.tfg.dtos.UsuarioDTO());
        return "registro";
    }
}

