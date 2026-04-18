package com.daw.tfg.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StripeCheckoutSessionRequest {

    @NotNull(message = "usuarioId es obligatorio")
    private Long usuarioId;

    @NotBlank(message = "successUrl es obligatorio")
    private String successUrl;

    @NotBlank(message = "cancelUrl es obligatorio")
    private String cancelUrl;

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getCancelUrl() {
        return cancelUrl;
    }

    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }
}
