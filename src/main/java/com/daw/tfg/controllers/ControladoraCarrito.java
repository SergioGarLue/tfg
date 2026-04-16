package com.daw.tfg.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.daw.tfg.models.Carrito;
import com.daw.tfg.models.Usuario;
import com.daw.tfg.service.CarritoService;
import com.daw.tfg.service.UsuarioService;

import java.util.List;

/**
 * Controladora REST para la gestión del carrito de compras.
 * Proporciona endpoints para operaciones CRUD y lógica de negocio del carrito.
 */
@RestController
@RequestMapping("/api/carrito")
public class ControladoraCarrito {

    private final CarritoService carritoService;
    private final UsuarioService usuarioService;

    public ControladoraCarrito(CarritoService carritoService, UsuarioService usuarioService) {
        this.carritoService = carritoService;
        this.usuarioService = usuarioService;
    }

    /**
     * Obtiene todos los carritos disponibles en el sistema.
     * 
     * @return Lista de todos los carritos
     */
    @GetMapping
    public List<Carrito> getAll() {
        return carritoService.findAll();
    }

    /**
     * Obtiene un carrito específico por su ID.
     * 
     * @param id Identificador del carrito
     * @return El carrito encontrado o error 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Carrito> getById(@PathVariable Long id) {
        try {
            Carrito carrito = carritoService.findById(id);
            return ResponseEntity.ok(carrito);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene el carrito asociado a un usuario específico.
     * 
     * @param usuarioId Identificador del usuario
     * @return El carrito del usuario o error 404 si no existe
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Carrito> getByUsuario(@PathVariable Long usuarioId) {
        try {
            Usuario usuario = usuarioService.findById(usuarioId);
            Carrito carrito = carritoService.findByUsuario(usuario);
            return ResponseEntity.ok(carrito);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene los juegos en el carrito de un usuario.
     * 
     * @param usuarioId Identificador del usuario
     * @return Lista de juegos en el carrito o 204 si está vacío
     */
    @GetMapping("/usuario/{usuarioId}/juegos")
    public ResponseEntity<List<?>> getGamesInCart(@PathVariable Long usuarioId) {
        try {
            List<?> juegos = (List<?>) carritoService.getAllGamesInCart(usuarioId);
            if (juegos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(juegos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Añade un juego al carrito de un usuario.
     * Si el usuario no tiene carrito, se crea uno nuevo.
     * 
     * @param usuarioId Identificador del usuario
     * @param juegoId   Identificador del juego a añadir
     * @return Respuesta 200 si se añade correctamente, 400 si hay error
     */
    @PostMapping("/usuario/{usuarioId}/agregar/{juegoId}")
    public ResponseEntity<String> addJuego(@PathVariable Long usuarioId, @PathVariable Long juegoId) {
        try {
            carritoService.addJuegoToCarrito(usuarioId, juegoId);
            return ResponseEntity.ok("Juego añadido al carrito correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Elimina un juego del carrito de un usuario.
     * 
     * @param usuarioId Identificador del usuario
     * @param juegoId   Identificador del juego a eliminar
     * @return Respuesta 200 si se elimina correctamente, 400 si hay error
     */
    @DeleteMapping("/usuario/{usuarioId}/eliminar/{juegoId}")
    public ResponseEntity<String> removeJuego(@PathVariable Long usuarioId,
            @PathVariable Long juegoId) {
        try {
            carritoService.removeJuegoFromCarrito(usuarioId, juegoId);
            return ResponseEntity.ok("Juego eliminado con éxito");
        } catch (IllegalArgumentException e) {
            // Error de cliente (datos incorrectos)
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Error genérico de servidor
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado al eliminar el juego");
        }
    }

    /**
     * Obtiene el precio total del carrito de un usuario.
     * 
     * @param usuarioId Identificador del usuario
     * @return El precio total del carrito o error 404 si no existe
     */
    @GetMapping("/total/{usuarioId}")
    public ResponseEntity<Float> getTotal(@PathVariable Long usuarioId) {
        try {
            Float total = carritoService.getTotalPrice(usuarioId);
            return ResponseEntity.ok(total);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Procesa el checkout del carrito de un usuario.
     * Crea una compra, vacía el carrito y asocia el método de pago o el paymentIntentId de Stripe.
     * 
     * @param usuarioId       Identificador del usuario
     * @param metodoPagoId    Identificador del método de pago tradicional (opcional)
     * @param paymentIntentId Identificador de Stripe PaymentIntent (opcional)
     * @return Respuesta 200 si el checkout es exitoso, 400 si hay error
     */
    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestParam Long usuarioId,
            @RequestParam(required = false) Long metodoPagoId,
            @RequestParam(required = false) String paymentIntentId) {
        try {
            carritoService.checkout(usuarioId, metodoPagoId, paymentIntentId);
            return ResponseEntity.ok("Compra realizada correctamente");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
