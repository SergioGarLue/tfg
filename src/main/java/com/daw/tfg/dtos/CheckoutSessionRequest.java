package com.daw.tfg.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * DTO para recibir los items del carrito desde el frontend y procesarlos en Stripe.
 * El frontend envía los juegos exactos que están en el carrito del usuario.
 */
public class CheckoutSessionRequest {

    @NotNull(message = "usuarioId es obligatorio")
    private Long usuarioId;

    @NotEmpty(message = "productos no puede estar vacío")
    @Valid
    private List<ProductoCarritoItem> productos;

    @NotBlank(message = "successUrl es obligatorio")
    private String successUrl;

    @NotBlank(message = "cancelUrl es obligatorio")
    private String cancelUrl;

    public CheckoutSessionRequest() {
    }

    public CheckoutSessionRequest(Long usuarioId, List<ProductoCarritoItem> productos, String successUrl, String cancelUrl) {
        this.usuarioId = usuarioId;
        this.productos = productos;
        this.successUrl = successUrl;
        this.cancelUrl = cancelUrl;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public List<ProductoCarritoItem> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoCarritoItem> productos) {
        this.productos = productos;
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

    /**
     * Representa un item del carrito con el ID del juego y su cantidad.
     */
    public static class ProductoCarritoItem {
        @NotNull(message = "juegoId es obligatorio")
        private Long juegoId;

        @NotNull(message = "cantidad es obligatoria")
        private Integer cantidad;

        public ProductoCarritoItem() {
        }

        public ProductoCarritoItem(Long juegoId, Integer cantidad) {
            this.juegoId = juegoId;
            this.cantidad = cantidad;
        }

        public Long getJuegoId() {
            return juegoId;
        }

        public void setJuegoId(Long juegoId) {
            this.juegoId = juegoId;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }

        @Override
        public String toString() {
            return "ProductoCarritoItem{" +
                    "juegoId=" + juegoId +
                    ", cantidad=" + cantidad +
                    '}';
        }
    }
}
