package com.daw.tfg.controllers;

import com.daw.tfg.dtos.StripePaymentIntentRequest;
import com.daw.tfg.dtos.StripePaymentIntentResponse;
import com.daw.tfg.enums.EstadoCompra;
import com.daw.tfg.service.CompraServiceImpl;
import com.daw.tfg.service.StripeService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/v1/payments")
public class StripePaymentController {

    private final StripeService stripeService;
    private final CompraServiceImpl compraService;

    public StripePaymentController(StripeService stripeService, CompraServiceImpl compraService) {
        this.stripeService = stripeService;
        this.compraService = compraService;
    }

    @PostMapping("/create-intent")
    public ResponseEntity<StripePaymentIntentResponse> createPaymentIntent(
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestHeader("Stripe-Signature") String signatureHeader,
                                                @RequestBody String payload) {
        try {
            Event event = stripeService.constructEvent(payload, signatureHeader);
            String eventType = event.getType();

            if ("payment_intent.succeeded".equals(eventType)) {
                EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
                if (dataObjectDeserializer.getObject().isPresent()) {
                    PaymentIntent paymentIntent = (PaymentIntent) dataObjectDeserializer.getObject().get();
                    compraService.actualizarEstadoCompraPorPaymentIntent(paymentIntent.getId(), EstadoCompra.COMPLETADA);
                    return ResponseEntity.ok("PaymentIntent succeeded: " + paymentIntent.getId());
                }
            } else if ("payment_intent.payment_failed".equals(eventType)
                    || "payment_intent.canceled".equals(eventType)) {
                EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
                if (dataObjectDeserializer.getObject().isPresent()) {
                    PaymentIntent paymentIntent = (PaymentIntent) dataObjectDeserializer.getObject().get();
                    compraService.actualizarEstadoCompraPorPaymentIntent(paymentIntent.getId(), EstadoCompra.CANCELADA);
                    return ResponseEntity.ok("PaymentIntent failed/canceled: " + paymentIntent.getId());
                }
            }

            return ResponseEntity.ok("Event received: " + eventType);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Stripe webhook signature");
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Stripe webhook event");
        }
    }
}
