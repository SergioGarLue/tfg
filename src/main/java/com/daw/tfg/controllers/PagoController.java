package com.daw.tfg.controllers;

import com.daw.tfg.dtos.ErrorResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controlador para confirmación de pagos post-redirect de Stripe.
 * Mapea a /api/pago para ser compatible con el frontend desacoplado.
 */
@RestController
@RequestMapping("/api/pago")
public class PagoController {

    private static final Logger logger = LoggerFactory.getLogger(PagoController.class);

    /**
     * Confirma el estado de una sesión de Stripe Checkout tras el redirect del usuario.
     * El frontend envía el session_id recibido en la URL de ?session_id=...
     *
     * @param sessionId ID de la sesión de Stripe Checkout
     * @return 200 OK si el pago está completo, 400 si no, 500 si hay error con Stripe
     */
    @PostMapping("/confirmar")
    public ResponseEntity<Object> confirmarPago(@RequestParam("session_id") String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            logger.warn("Intento de confirmación sin session_id");
            return ResponseEntity.badRequest().body(new ErrorResponse("El session_id es obligatorio"));
        }

        try {
            Session session = Session.retrieve(sessionId);

            String status = session.getStatus();
            String paymentStatus = session.getPaymentStatus();

            logger.info("Verificando sesión Stripe: {} - status: {}, paymentStatus: {}", 
                    sessionId, status, paymentStatus);

            if ("complete".equals(status) && "paid".equals(paymentStatus)) {
                logger.info("Pago confirmado exitosamente para sesión: {}", sessionId);
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Pago confirmado correctamente",
                        "sessionId", sessionId
                ));
            } else {
                logger.warn("Sesión de pago no completada: {} - status: {}, paymentStatus: {}", 
                        sessionId, status, paymentStatus);
                return ResponseEntity.badRequest().body(new ErrorResponse(
                        "El pago no ha sido completado. Estado: " + status
                ));
            }
        } catch (StripeException e) {
            logger.error("Error confirmando pago con Stripe: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
                    "Error al verificar el pago con Stripe: " + e.getMessage()
            ));
        }
    }
}
