package com.daw.tfg.Controllers;

import org.springframework.web.bind.annotation.*;
import com.daw.tfg.Service.AmigoService;
import com.daw.tfg.models.Amistad;
import java.util.List;

@RestController
@RequestMapping("/api/amistades")
public class ControladoraAmistades {

    private final AmigoService amigoService;

    public ControladoraAmistades(AmigoService amigoService) {
        this.amigoService = amigoService;
    }

    @GetMapping
    public List<Amistad> getAll() {
        return amigoService.findAll();
    }

}
