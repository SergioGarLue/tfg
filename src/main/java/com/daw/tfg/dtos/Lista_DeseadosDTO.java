package com.daw.tfg.dtos;

import java.util.List;

import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Lista_DeseadosDTO {
    private Long idListaDeseados;

    @NotNull(message = "ID usuario requerido")
    private Long usuarioId;

    private List<Long> juegosIds;
}
