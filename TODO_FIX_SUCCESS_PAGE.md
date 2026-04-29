# TODO: Fix /success 401 Unauthorized + Completar Ciclo de Compra Stripe

## Steps

- [x] 1. Fix SecurityConfig.java
  - Moved /api/stripe/webhook BEFORE /api/**.authenticated()
  - Removed duplicate /api/auth/** line
  - Added /success.html and /pago-exitoso to public routes
- [x] 2. Fix ViewController.java
  - Changed return "forward:/success.html" → return "success"
  - Added /pago-exitoso mapping as alias
- [x] 3. Fix route-protection.js
  - Added /success and /pago-exitoso to PUBLIC_ROUTES array
- [x] 4. Add /api/pago/confirmar endpoint
  - Created PagoController.java with POST /api/pago/confirmar
  - Verifica sesión Stripe (complete + paid)
  - Obtiene usuario autenticado desde SecurityContextHolder (JWT)
  - Mueve juegos del Carrito a Colección vía @Transactional
  - Limpia el carrito del usuario
  - Devuelve JSON con juegos añadidos y nombres
- [x] 5. Update success.html frontend
  - Parsea respuesta JSON del backend
  - Muestra cuántos juegos se añadieron
  - Limpia localStorage ('carrito' y 'carritoCount')
  - Actualiza badge del carrito a 0
  - Redirige a /coleccion después de 3 segundos con cuenta regresiva
- [ ] 6. Test the complete flow (requires app restart)

## Archivos Modificados/Creados

| Archivo | Cambio |
|---------|--------|
| `SecurityConfig.java` | Reordenado webhook, rutas públicas extra |
| `ViewController.java` | Fixed /success mapping, added /pago-exitoso |
| `route-protection.js` | /success y /pago-exitoso como públicas |
| `PagoController.java` | **NUEVO** - Endpoint completo de confirmación |
| `success.html` | Lógica de éxito con limpieza y redirección |

## Flujo Completo de Compra

1. Usuario completa pago en Stripe
2. Stripe redirige a `/success?session_id=xxx`
3. success.html carga y llama `POST /api/pago/confirmar?session_id=xxx`
4. fetch-interceptor.js añade JWT automáticamente
5. Backend verifica sesión Stripe → obtiene usuario → mueve carrito→colección → limpia carrito
6. Frontend recibe 200 OK, limpia localStorage, actualiza badge, redirige a /coleccion en 3s
