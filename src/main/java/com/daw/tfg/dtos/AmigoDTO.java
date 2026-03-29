package com.daw.tfg.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmigoDTO {
    private Long idAmistad;

    @NotNull(message = "ID solicitante requerido")
    private Long idSolicitante;

    @NotNull(message = "ID destinatario requerido")
    private Long idDestinatario;

    private String estado; // EstadoPeticion as String

    private LocalDateTime fechaPeticion;
}
