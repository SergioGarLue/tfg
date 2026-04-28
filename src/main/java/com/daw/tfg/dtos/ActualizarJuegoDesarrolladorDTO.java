package com.daw.tfg.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ActualizarJuegoDesarrolladorDTO {
    private Double precio;
    private Integer porcentaje;
    private Double precioRebajado;
    private Boolean disponible;
}
