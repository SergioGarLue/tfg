package com.daw.tfg.Dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContenidoAdicionalDTO {
    private Long idContenidoAdicional;
    private String titulo;
    private Float precio;
    private String descripcion;
    private String imagen;
    private String requisitos;
    private Long idJuego;
    private String tituloJuego;
}
