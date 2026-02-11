package com.daw.tfg.Controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/configuracion")
public class ControladoraConfiguracion {

    // No hay servicio específico para configuración, así que básico por ahora

    @GetMapping
    public String getConfig() {
        return "Configuración básica";
    }

}
