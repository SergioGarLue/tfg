const stripePublicKey = 'pk_test_XXXXXXXXXXXXXXXXXXXXXXXX'; // Reemplaza con tu clave de prueba Stripe
const backendBaseUrl = 'http://localhost:8080';

const paymentForm = document.getElementById('payment-form');
const cardElementContainer = document.getElementById('card-element');
const cardErrors = document.getElementById('card-errors');
const paymentResult = document.getElementById('payment-result');
const payButton = document.getElementById('pay-button');

let stripe;
let cardElement;

window.addEventListener('DOMContentLoaded', () => {
  initializeStripeElements();
  paymentForm.addEventListener('submit', handlePaymentSubmit);
});

function initializeStripeElements() {
  if (!window.Stripe) {
    showError('Stripe.js no se ha cargado. Revisa el script de CDN.');
    return;
  }

  stripe = Stripe(stripePublicKey);
  const elements = stripe.elements();

  const style = {
    base: {
      color: '#e2e8f0',
      fontFamily: 'Inter, system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif',
      fontSmoothing: 'antialiased',
      fontSize: '16px',
      '::placeholder': {
        color: '#94a3b8'
      }
    },
    invalid: {
      color: '#f87171',
      iconColor: '#f87171'
    }
  };

  cardElement = elements.create('card', {
    hidePostalCode: true,
    style,
  });

  cardElement.mount(cardElementContainer);
  cardElement.on('change', event => {
    if (event.error) {
      showError(event.error.message);
    } else {
      resetError();
    }
  });
}

async function handlePaymentSubmit(event) {
  event.preventDefault();
  resetError();
  setLoading(true);

  const usuarioId = document.getElementById('usuario-id').value.trim();
  const amountValue = parseFloat(document.getElementById('amount').value.trim());
  const descripcion = document.getElementById('descripcion').value.trim() || `Pago de carrito para usuario ${usuarioId}`;
  const currency = 'eur';

  if (!usuarioId) {
    showError('El ID de usuario es obligatorio.');
    setLoading(false);
    return;
  }

  if (!amountValue || amountValue <= 0) {
    showError('Introduce un importe válido mayor que cero.');
    setLoading(false);
    return;
  }

  const montoEnCentavos = Math.round(amountValue * 100);

  try {
    const intentPayload = {
      monto: montoEnCentavos,
      currency,
      descripcion
    };

    const intentResponse = await fetch(`${backendBaseUrl}/api/v1/payments/create-intent`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(intentPayload)
    });

    if (!intentResponse.ok) {
      const errorBody = await intentResponse.text();
      throw new Error(errorBody || 'No se pudo crear el PaymentIntent.');
    }

    const intentData = await intentResponse.json();
    const clientSecret = intentData.clientSecret;
    const paymentIntentId = intentData.id;

    const result = await stripe.confirmCardPayment(clientSecret, {
      payment_method: {
        card: cardElement
      }
    });

    if (result.error) {
      showError(result.error.message || 'Error procesando el pago.');
      setLoading(false);
      return;
    }

    if (result.paymentIntent && result.paymentIntent.status === 'succeeded') {
      await completarCheckout(usuarioId, paymentIntentId);
      return;
    }

    showError(`Pago no completado. Estado: ${result.paymentIntent?.status || 'desconocido'}`);
    setLoading(false);
  } catch (error) {
    showError(error.message || 'Error inesperado en el pago.');
    setLoading(false);
  }
}

async function completarCheckout(usuarioId, paymentIntentId) {
  const response = await fetch(`${backendBaseUrl}/api/v1/carrito/checkout?usuarioId=${encodeURIComponent(usuarioId)}&paymentIntentId=${encodeURIComponent(paymentIntentId)}`, {
    method: 'POST',
    mode: 'cors'
  });

  if (!response.ok) {
    const body = await response.text();
    throw new Error(body || `Error al cerrar compra (${response.status})`);
  }

  const message = await response.text();
  paymentResult.textContent = message || 'Compra confirmada y productos entregados en la biblioteca.';
  paymentResult.style.color = '#a7f3d0';
  payButton.textContent = 'Pago completado';
}

function showError(message) {
  cardErrors.textContent = message;
  paymentResult.textContent = '';
}

function resetError() {
  cardErrors.textContent = '';
}

function setLoading(isLoading) {
  payButton.disabled = isLoading;
  payButton.textContent = isLoading ? 'Procesando...' : 'Pagar ahora';
}
