package com.daw.tfg.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StripeService {

    private static final Logger logger = LoggerFactory.getLogger(StripeService.class);

    @Value("${stripe.key.secret}")
    private String stripeSecretKey;

    @Value("${stripe.webhook.secret}")
    private String stripeWebhookSecret;

    @PostConstruct
    public void init() {
        if (stripeSecretKey == null || stripeSecretKey.isBlank() || stripeSecretKey.contains("your_secret_key_here")) {
            logger.error("Stripe secret API key is not configured or uses a placeholder value");
            throw new IllegalStateException("Stripe secret key is required and must not be a placeholder");
        }
        if (stripeWebhookSecret == null || stripeWebhookSecret.isBlank() || stripeWebhookSecret.contains("your_webhook_secret_here")) {
            logger.error("Stripe webhook secret is not configured or uses a placeholder value");
            throw new IllegalStateException("Stripe webhook secret is required and must not be a placeholder");
        }
        Stripe.apiKey = stripeSecretKey;
    }

    public PaymentIntent createPaymentIntent(Long amount, String currency, String description) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", currency != null ? currency : "eur");
        params.put("payment_method_types", List.of("card"));

        if (description != null && !description.isBlank()) {
            params.put("description", description);
        }

        return PaymentIntent.create(params);
    }

    public Session createCheckoutSession(List<com.daw.tfg.models.Juego> juegos, String successUrl, String cancelUrl) throws StripeException {
        if (juegos == null || juegos.isEmpty()) {
            throw new IllegalArgumentException("El carrito no puede estar vacío para crear una sesión de compra");
        }

        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(cancelUrl);

        for (com.daw.tfg.models.Juego juego : juegos) {
if (juego.getPrecio() == null || juego.getPrecio() < 0) {
                throw new IllegalArgumentException("El juego " + juego.getTitulo() + " tiene precio inválido" + (juego.getPrecio() == null ? ": null" : ": negativo"));
            }

            SessionCreateParams.LineItem.PriceData.ProductData.Builder productDataBuilder =
                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName(juego.getTitulo());

            if (juego.getDescripcion() != null && !juego.getDescripcion().isBlank()) {
                productDataBuilder.setDescription(juego.getDescripcion());
            }

            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity(1L)
                    .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("eur")
                                    .setUnitAmount(Math.round(juego.getPrecio() * 100))
                                    .setProductData(productDataBuilder.build())
                                    .build()
                    )
                    .build();

            paramsBuilder.addLineItem(lineItem);
        }

        SessionCreateParams params = paramsBuilder.build();
        return Session.create(params);
    }

    public Event constructEvent(String payload, String signatureHeader) throws StripeException {
        return Webhook.constructEvent(payload, signatureHeader, stripeWebhookSecret);
    }

    public PaymentIntent retrievePaymentIntent(String paymentIntentId) throws StripeException {
        return PaymentIntent.retrieve(paymentIntentId);
    }
}
