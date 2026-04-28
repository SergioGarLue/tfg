# TODO: Fix /success 401 Unauthorized

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
  - Created new PagoController.java with POST /api/pago/confirmar
- [ ] 5. Test the complete flow (requires app restart)

