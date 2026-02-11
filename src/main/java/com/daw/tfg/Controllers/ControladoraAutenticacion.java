package com.daw.tfg.Controllers;

import org.springframework.web.bind.annotation.*;
import com.daw.tfg.Service.UsuarioService;
import com.daw.tfg.models.Usuario;
import java.util.List;

@RestController
@RequestMapping("/api/autenticacion")
public class ControladoraAutenticacion {

    private final UsuarioService usuarioService;

    public ControladoraAutenticacion(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> getAll() {
        return usuarioService.findAll();
    }

}
