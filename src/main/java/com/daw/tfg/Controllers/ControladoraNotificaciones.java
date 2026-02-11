package com.daw.tfg.Controllers;

import org.springframework.web.bind.annotation.*;
import com.daw.tfg.Service.NotificacionService;
import com.daw.tfg.models.Notificacion;
import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class ControladoraNotificaciones {

    private final NotificacionService notificacionService;

    public ControladoraNotificaciones(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping
    public List<Notificacion> getAll() {
        return notificacionService.findAll();
    }

}
