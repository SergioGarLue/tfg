package com.daw.tfg.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompraDTO {
    private Long idCompra;

    private LocalDateTime fechaCompra;

    @NotNull(message = "Total es requerido")
    @PositiveOrZero(message = "Total debe ser positivo")
    private Double total;

    private String estado; // or EstadoCompra enum as String

    private String paymentIntentId;
}
