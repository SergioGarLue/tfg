package com.daw.tfg.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * DTO para recibir los juegos gratuitos del carrito y agregarlos a la colección del usuario.
 */
public class AddFreeGamesRequest {

    @NotNull(message = "usuarioId es obligatorio")
    private Long usuarioId;

    @NotEmpty(message = "productos no puede estar vacío")
    @Valid
    private List<ProductoItem> productos;

    public AddFreeGamesRequest() {
    }

    public AddFreeGamesRequest(Long usuarioId, List<ProductoItem> productos) {
        this.usuarioId = usuarioId;
        this.productos = productos;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public List<ProductoItem> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoItem> productos) {
        this.productos = productos;
    }

    /**
     * Item del carrito con el ID del juego.
     */
    public static class ProductoItem {
        @NotNull(message = "juegoId es obligatorio")
        private Long juegoId;

        public ProductoItem() {
        }

        public ProductoItem(Long juegoId) {
            this.juegoId = juegoId;
        }

        public Long getJuegoId() {
            return juegoId;
        }

        public void setJuegoId(Long juegoId) {
            this.juegoId = juegoId;
        }

        @Override
        public String toString() {
            return "ProductoItem{" +
                    "juegoId=" + juegoId +
                    '}';
        }
    }
}
