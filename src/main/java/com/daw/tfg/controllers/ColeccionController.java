package com.daw.tfg.controllers;

import com.daw.tfg.dtos.AddFreeGamesRequest;
import com.daw.tfg.dtos.ErrorResponse;
import com.daw.tfg.models.Juego;
import com.daw.tfg.models.Usuario;
import com.daw.tfg.service.ColeccionService;
import com.daw.tfg.service.JuegoService;
import com.daw.tfg.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/coleccion")
@CrossOrigin(origins = "*")
public class ColeccionController {

    private static final Logger logger = LoggerFactory.getLogger(ColeccionController.class);

    private final ColeccionService coleccionService;
    private final UsuarioService usuarioService;
    private final JuegoService juegoService;

    public ColeccionController(ColeccionService coleccionService, UsuarioService usuarioService, JuegoService juegoService) {
        this.coleccionService = coleccionService;
        this.usuarioService = usuarioService;
        this.juegoService = juegoService;
    }

    /**
     * Endpoint para añadir juegos gratuitos directamente a la colección del usuario.
     * Se utiliza cuando el carrito contiene solo juegos con precio 0.
     */
    @PostMapping("/add-gratis")
    public ResponseEntity<Object> addFreeGames(@Valid @RequestBody AddFreeGamesRequest request) {
        try {
            if (request.getProductos() == null || request.getProductos().isEmpty()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("No hay juegos para añadir"));
            }

            // Obtener el usuario
            Usuario usuario = usuarioService.findById(request.getUsuarioId());
            logger.info("Añadiendo juegos gratuitos a la colección del usuario: {}", usuario.getIdUsuario());

            int juegosAñadidos = 0;
            int juegosYaEnColeccion = 0;

            // Procesar cada juego
            for (AddFreeGamesRequest.ProductoItem producto : request.getProductos()) {
                try {
                    Juego juego = juegoService.findById(producto.getJuegoId());

                    // Verificar que el juego sea gratuito
                    if (juego.getPrecio() != null && juego.getPrecio() > 0.01) {
                        logger.warn("Intento de añadir juego de pago a través de endpoint gratuito: {} (${} )", 
                                juego.getTitulo(), juego.getPrecio());
                        continue;
                    }

                    // Intentar añadir a la colección (sin excepción si ya existe)
                    coleccionService.addJuegoToCollectionIfAbsent(request.getUsuarioId(), producto.getJuegoId(), new java.util.Date());
                    juegosAñadidos++;
                    logger.info("Juego gratuito añadido a colección: {}", juego.getTitulo());
                } catch (Exception e) {
                    logger.warn("Error procesando juego {}: {}", producto.getJuegoId(), e.getMessage());
                    juegosYaEnColeccion++;
                }
            }

            // Respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Proceso completado");
            response.put("juegosAnadidos", juegosAñadidos);
            response.put("juegosYaEnColeccion", juegosYaEnColeccion);
            response.put("total", request.getProductos().size());

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.error("Error validación: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            logger.error("Error inesperado al añadir juegos gratuitos: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error procesando la solicitud"));
        }
    }
}
