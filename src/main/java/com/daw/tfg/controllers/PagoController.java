package com.daw.tfg.controllers;

import com.daw.tfg.dtos.ErrorResponse;
import com.daw.tfg.models.Carrito;
import com.daw.tfg.models.Juego;
import com.daw.tfg.models.Usuario;
import com.daw.tfg.repository.UsuarioRepository;
import com.daw.tfg.service.CarritoService;
import com.daw.tfg.service.ColeccionService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Controlador para confirmación de pagos post-redirect de Stripe.
 * Mapea a /api/pago para ser compatible con el frontend desacoplado.
 */
@RestController
@RequestMapping("/api/pago")
public class PagoController {

    private static final Logger logger = LoggerFactory.getLogger(PagoController.class);

    private final UsuarioRepository usuarioRepository;
    private final CarritoService carritoService;
    private final ColeccionService coleccionService;

    public PagoController(UsuarioRepository usuarioRepository, CarritoService carritoService, ColeccionService coleccionService) {
        this.usuarioRepository = usuarioRepository;
        this.carritoService = carritoService;
        this.coleccionService = coleccionService;
    }

    /**
     * Confirma el estado de una sesión de Stripe Checkout y completa el ciclo de compra:
     * 1. Verifica que el pago esté completado ("complete" + "paid")
     * 2. Obtiene el usuario autenticado desde el JWT en el SecurityContextHolder
     * 3. Recupera el carrito del usuario desde la base de datos
     * 4. Mueve cada juego del carrito a la colección del usuario
     * 5. Limpia el carrito del usuario
     *
     * @param sessionId ID de la sesión de Stripe Checkout
     * @return 200 OK con los juegos añadidos, o error si el pago no está completado
     */
    @PostMapping("/confirmar")
    @Transactional
    public ResponseEntity<Object> confirmarPago(@RequestParam("session_id") String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            logger.warn("Intento de confirmación sin session_id");
            return ResponseEntity.badRequest().body(new ErrorResponse("El session_id es obligatorio"));
        }

        try {
            // 1. Verificar sesión de Stripe
            Session session = Session.retrieve(sessionId);
            String status = session.getStatus();
            String paymentStatus = session.getPaymentStatus();

            logger.info("Verificando sesión Stripe: {} - status: {}, paymentStatus: {}", 
                    sessionId, status, paymentStatus);

            if (!"complete".equals(status) || !"paid".equals(paymentStatus)) {
                logger.warn("Sesión de pago no completada: {} - status: {}, paymentStatus: {}", 
                        sessionId, status, paymentStatus);
                return ResponseEntity.badRequest().body(new ErrorResponse(
                        "El pago no ha sido completado. Estado: " + status
                ));
            }

            // 2. Obtener usuario autenticado desde el JWT (SecurityContextHolder)
            Usuario usuario = obtenerUsuarioAutenticado();
            if (usuario == null) {
                logger.error("No se pudo obtener el usuario autenticado");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(
                        "Usuario no autenticado"
                ));
            }

            logger.info("Procesando compra para usuario: {} ({})", usuario.getIdUsuario(), usuario.getNombreUsuario());

            // 3. Obtener carrito del usuario
            Carrito carrito;
            try {
                carrito = carritoService.findByUsuario(usuario);
            } catch (IllegalArgumentException e) {
                // Usuario no tiene carrito, pero el pago fue exitoso
                logger.warn("Usuario {} no tiene carrito, pero el pago fue exitoso", usuario.getIdUsuario());
                return ResponseEntity.ok(crearRespuestaExito(sessionId, 0, 0, List.of()));
            }

            // 4. Mover juegos del carrito a la colección
            Set<Juego> juegos = carrito.getJuegos();
            int juegosAnadidos = 0;
            int juegosYaEnColeccion = 0;
            List<String> nombresJuegos = new ArrayList<>();

            if (juegos != null && !juegos.isEmpty()) {
                for (Juego juego : juegos) {
                    try {
                        coleccionService.addJuegoToCollectionIfAbsent(
                                usuario.getIdUsuario(), 
                                juego.getIdJuego(), 
                                new Date()
                        );
                        juegosAnadidos++;
                        nombresJuegos.add(juego.getTitulo());
                        logger.info("Juego añadido a colección: {} para usuario {}", 
                                juego.getTitulo(), usuario.getIdUsuario());
                    } catch (Exception e) {
                        // Probablemente ya está en la colección u otro error
                        juegosYaEnColeccion++;
                        logger.warn("Juego {} no se pudo añadir (posiblemente ya en colección): {}", 
                                juego.getTitulo(), e.getMessage());
                    }
                }
            }

            // 5. Limpiar carrito
            if (juegos != null) {
                carrito.setJuegos(new HashSet<>());
                carritoService.save(carrito);

                logger.info("Carrito limpiado para usuario: {}", usuario.getIdUsuario());
            }

            // 6. Respuesta exitosa
            logger.info("Compra completada para usuario {}: {} juegos añadidos, {} ya en colección", 
                    usuario.getIdUsuario(), juegosAnadidos, juegosYaEnColeccion);

            return ResponseEntity.ok(crearRespuestaExito(sessionId, juegosAnadidos, juegosYaEnColeccion, nombresJuegos));

        } catch (StripeException e) {
            logger.error("Error confirmando pago con Stripe: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
                    "Error al verificar el pago con Stripe: " + e.getMessage()
            ));
        }
    }

    /**
     * Obtiene el usuario autenticado desde el SecurityContextHolder (JWT).
     * El JwtAuthenticationFilter ya establece la autenticación en el contexto.
     */
    private Usuario obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("No hay autenticación en el SecurityContext");
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return usuarioRepository.findByNombreUsuario(username).orElse(null);
        }

        logger.warn("Principal no es UserDetails: {}", principal.getClass().getName());
        return null;
    }

    /**
     * Crea la respuesta de éxito estandarizada.
     */
    private Map<String, Object> crearRespuestaExito(String sessionId, int juegosAnadidos, int juegosYaEnColeccion, List<String> nombresJuegos) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Compra completada exitosamente");
        response.put("sessionId", sessionId);
        response.put("juegosAnadidos", juegosAnadidos);
        response.put("juegosYaEnColeccion", juegosYaEnColeccion);
        response.put("nombresJuegos", nombresJuegos);
        return response;
    }
}
