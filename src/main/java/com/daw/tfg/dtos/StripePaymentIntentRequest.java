package com.daw.tfg.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class StripePaymentIntentRequest {

    @NotNull(message = "El monto es obligatorio")
    @Min(value = 1, message = "El monto debe ser mayor que cero")
    private Long monto; // En centavos (por ejemplo, 1000 = 10.00)

    private String currency = "eur";

    private String descripcion;

    public Long getMonto() {
        return monto;
    }

    public void setMonto(Long monto) {
        this.monto = monto;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
