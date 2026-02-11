package com.daw.tfg.Controllers;

import org.springframework.web.bind.annotation.*;
import com.daw.tfg.Service.UsuarioService;
import com.daw.tfg.models.Usuario;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class ControladoraUsuario {

    private final UsuarioService usuarioService;

    public ControladoraUsuario(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> getAll() {
        return usuarioService.findAll();
    }

}
