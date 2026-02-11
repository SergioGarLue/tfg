package com.daw.tfg.Controllers;

import org.springframework.web.bind.annotation.*;
import com.daw.tfg.Service.ColeccionService;
import com.daw.tfg.models.Coleccion;
import java.util.List;

@RestController
@RequestMapping("/api/biblioteca")
public class ControladoraBiblioteca {

    private final ColeccionService coleccionService;

    public ControladoraBiblioteca(ColeccionService coleccionService) {
        this.coleccionService = coleccionService;
    }

    @GetMapping
    public List<Coleccion> getAll() {
        return coleccionService.findAll();
    }

}
