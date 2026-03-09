package com.daw.tfg.dtos;

import jakarta.validation.constraints.NotNull;

/**
 * DTO para el proceso de checkout del carrito.
 */
public class CheckoutDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    private Long metodoPagoId;

    public CheckoutDTO() {}

    public CheckoutDTO(Long usuarioId, Long metodoPagoId) {
        this.usuarioId = usuarioId;
        this.metodoPagoId = metodoPagoId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getMetodoPagoId() {
        return metodoPagoId;
    }

    public void setMetodoPagoId(Long metodoPagoId) {
        this.metodoPagoId = metodoPagoId;
    }
}

