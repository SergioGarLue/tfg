package com.daw.tfg.dtos;

import java.util.Date;

import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ColeccionDTO {
    private Long idColeccionJuego;

    @NotNull(message = "ID usuario requerido")
    private Long usuarioId;

    @NotNull(message = "ID juego requerido")
    private Long juegoId;

    private Boolean esFavorito;

    private Date fechaAdquisicion;
}
