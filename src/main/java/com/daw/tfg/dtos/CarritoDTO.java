package com.daw.tfg.dtos;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object para Carrito.
 * Se usa para enviar los datos del carrito a la vista de forma estructurada.
 */
@Data
@NoArgsConstructor
public class CarritoDTO {

    /**
     * Identificador único del carrito
     */
    private Long id;
    
    /**
     * Lista de juegos contenidos en el carrito
     */
    private List<JuegoDTO> juegos;
    
    /**
     * Precio total del carrito
     */
    private Float total;
    
    /**
     * Usuario asociado al carrito
     */
    private UsuarioDTO usuario;
    
    /**
     * Contenidos adicionales del carrito (DLCs, expansiones, etc.)
     */
    private List<JuegoDTO> contenidosAdicionales;
    
    /**
     * Compra asociada al carrito (una vez realizada la compra)
     */
    private CompraDTO compra;

}
