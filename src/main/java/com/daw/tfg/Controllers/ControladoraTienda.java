package com.daw.tfg.Controllers;

import org.springframework.web.bind.annotation.*;
import com.daw.tfg.Service.JuegoService;
import com.daw.tfg.models.Juego;
import java.util.List;

@RestController
@RequestMapping("/api/tienda")
public class ControladoraTienda {

    private final JuegoService juegoService;

    public ControladoraTienda(JuegoService juegoService) {
        this.juegoService = juegoService;
    }

    @GetMapping
    public List<Juego> getAll() {
        return juegoService.findAll();
    }

}
