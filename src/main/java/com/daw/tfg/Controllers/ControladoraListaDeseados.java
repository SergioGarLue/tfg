package com.daw.tfg.Controllers;

import org.springframework.web.bind.annotation.*;
import com.daw.tfg.Service.ListaDeseadosService;
import com.daw.tfg.models.ListaDeseados;
import java.util.List;

@RestController
@RequestMapping("/api/lista-deseados")
public class ControladoraListaDeseados {

    private final ListaDeseadosService listaDeseadosService;

    public ControladoraListaDeseados(ListaDeseadosService listaDeseadosService) {
        this.listaDeseadosService = listaDeseadosService;
    }

    @GetMapping
    public List<ListaDeseados> getAll() {
        return listaDeseadosService.findAll();
    }

}
