package com.daw.tfg.controllers;

import com.daw.tfg.dtos.ErrorResponse;
import com.daw.tfg.dtos.StripeCheckoutSessionRequest;
import com.daw.tfg.dtos.StripeCheckoutSessionResponse;
import com.daw.tfg.dtos.StripePaymentIntentRequest;
import com.daw.tfg.dtos.StripePaymentIntentResponse;
import com.daw.tfg.dtos.CheckoutSessionRequest;
import com.daw.tfg.enums.EstadoCompra;
import com.daw.tfg.models.Juego;
import com.daw.tfg.service.CarritoService;
import com.daw.tfg.service.CompraServiceImpl;
import com.daw.tfg.service.JuegoService;
import com.daw.tfg.service.StripeService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@Validated
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/payments")
public class StripePaymentController {

    private static final Logger logger = LoggerFactory.getLogger(StripePaymentController.class);

    private final StripeService stripeService;
    private final CompraServiceImpl compraService;
    private final CarritoService carritoService;
    private final JuegoService juegoService;

    public StripePaymentController(StripeService stripeService, CompraServiceImpl compraService, CarritoService carritoService, JuegoService juegoService) {
        this.stripeService = stripeService;
        this.compraService = compraService;
        this.carritoService = carritoService;
        this.juegoService = juegoService;
    }

    @PostMapping("/create-intent")
    public ResponseEntity<Object> createPaymentIntent(
            @Valid @RequestBody StripePaymentIntentRequest request) {
        try {
            PaymentIntent paymentIntent = stripeService.createPaymentIntent(request.getMonto(), request.getCurrency(), request.getDescripcion());
            StripePaymentIntentResponse response = new StripePaymentIntentResponse(
                    paymentIntent.getClientSecret(),
                    paymentIntent.getId(),
                    paymentIntent.getAmount(),
                    paymentIntent.getCurrency()
            );
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            logger.error("Error Stripe intent: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error procesando PaymentIntent con Stripe"));
        }
    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<Object> createCheckoutSession(
            @Valid @RequestBody StripeCheckoutSessionRequest request) {
        try {
            List<Juego> juegos = carritoService.getAllGamesInCart(request.getUsuarioId());
            if (juegos.isEmpty()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("El carrito está vacío"));
            }

            String finalSuccessUrl = request.getSuccessUrl() != null ? request.getSuccessUrl() : "http://localhost:8080/pago-exitoso";
            String finalCancelUrl = request.getCancelUrl() != null ? request.getCancelUrl() : "http://localhost:8080/carrito";
            Session session = stripeService.createCheckoutSession(juegos, finalSuccessUrl, finalCancelUrl);
            StripeCheckoutSessionResponse response = new StripeCheckoutSessionResponse(session.getId(), session.getUrl());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.error("Error validación checkout: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (StripeException e) {
            logger.error("Error Stripe checkout: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error procesando pago con Stripe"));
        }
    }

    /**
     * Endpoint específico para crear sesión de checkout con los juegos exactos del carrito del frontend.
     * El frontend envía los productos específicos que tiene en localStorage.
     */
    @PostMapping("/create-checkout-session-v2")
    public ResponseEntity<Object> createCheckoutSessionV2(
            @Valid @RequestBody CheckoutSessionRequest request) {
        try {
            if (request.getProductos() == null || request.getProductos().isEmpty()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("El carrito está vacío"));
            }

            // Obtener los juegos específicos enviados por el frontend
            List<Juego> juegos = request.getProductos().stream()
                    .map(producto -> juegoService.findById(producto.getJuegoId()))
                    .toList();

            if (juegos.isEmpty()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("No se encontraron los juegos del carrito"));
            }

            logger.info("Creando sesión de checkout para usuario {} con {} juego(s)", 
                    request.getUsuarioId(), juegos.size());
            juegos.forEach(j -> logger.info("  - {} (${} )", j.getTitulo(), j.getPrecio()));

            String finalSuccessUrl = request.getSuccessUrl() != null ? request.getSuccessUrl() : "http://localhost:8080/pago-exitoso";
            String finalCancelUrl = request.getCancelUrl() != null ? request.getCancelUrl() : "http://localhost:8080/carrito";
            
            Session session = stripeService.createCheckoutSession(juegos, finalSuccessUrl, finalCancelUrl);
            StripeCheckoutSessionResponse response = new StripeCheckoutSessionResponse(session.getId(), session.getUrl());
            
            logger.info("Sesión de Stripe creada: {}", session.getId());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.error("Error validación checkout: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (StripeException e) {
            logger.error("Error Stripe checkout: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error procesando pago con Stripe"));
        } catch (Exception e) {
            logger.error("Error inesperado en checkout: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error procesando el pago"));
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestHeader(value = "Stripe-Signature", required = false) String signatureHeader,
                                                @RequestBody(required = false) String payload) {
        if (payload == null || payload.isBlank()) {
            logger.warn("Stripe webhook received empty payload");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Empty webhook payload");
        }
        if (signatureHeader == null || signatureHeader.isBlank()) {
            logger.warn("Stripe webhook missing signature header");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing Stripe-Signature header");
        }

        try {
            Event event = stripeService.constructEvent(payload, signatureHeader);
            String eventType = event.getType();
            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();

            if (!dataObjectDeserializer.getObject().isPresent()) {
                logger.warn("Stripe event data object is missing for event type: {}", eventType);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Event data missing");
            }

            PaymentIntent paymentIntent = (PaymentIntent) dataObjectDeserializer.getObject().get();
            String paymentIntentId = paymentIntent.getId();

            if ("payment_intent.succeeded".equals(eventType)) {
                compraService.actualizarEstadoCompraPorPaymentIntent(paymentIntentId, EstadoCompra.COMPLETADA);
                return ResponseEntity.ok("PaymentIntent succeeded: " + paymentIntentId);
            } else if ("payment_intent.payment_failed".equals(eventType)
                    || "payment_intent.canceled".equals(eventType)) {
                compraService.actualizarEstadoCompraPorPaymentIntent(paymentIntentId, EstadoCompra.CANCELADA);
                return ResponseEntity.ok("PaymentIntent failed/canceled: " + paymentIntentId);
            }

            logger.info("Stripe webhook ignored event type: {}", eventType);
            return ResponseEntity.ok("Event received: " + eventType);
        } catch (SignatureVerificationException e) {
            logger.warn("Invalid Stripe webhook signature", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Stripe webhook signature");
        } catch (StripeException e) {
            logger.error("Stripe event construction failed", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Stripe webhook event");
        } catch (Exception e) {
            logger.error("Unexpected error processing Stripe webhook", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing webhook");
        }
    }
}
