# TODO Fixes Post-Deploy — RESUELTOS ✅

## 1. AbortError en login como DEVELOPER ✅
- **Causa:** `fetch-interceptor.js` agregaba `Authorization: Bearer null` en peticiones de login.
- **Fix:** Agregada condición `isAuthEndpoint` para no inyectar JWT en `/api/auth/**`.

## 2. Fotos en index.html intercaladas ✅
- **Causa:** HTML mal formado en `renderizarJuegos()` de `script.js`. Faltaba `</div>` de cierre.
- **Fix:** Corregida la plantilla HTML de las tarjetas.

## 3. Vista de edición del juego desde desarrollador sale mal ✅
- **Causa:** Estructura HTML rota en `desarrollador.html`. Faltaba `</div>` de cierre de `.estadisticas-b2b`.
- **Fix:** Reescrita estructura completa del panel B2B con HTML válido.

## 4. Perfil cambiado y sale mal ✅
- **Verificación:** `git diff HEAD -- src/main/resources/templates/perfil.html` mostró sin cambios.
- **Estado:** No requiere acción.

## 5. Precio actualizado no se refleja en carrito ✅
- **Causa:** `CarritoService.getTotalPrice()` usaba solo `juego.getPrecio()`, ignorando `precioRebajado` y `disponible`.
- **Fix:** Agregado método `getPrecioEfectivo()` que prioriza `precioRebajado` y respeta `disponible=false`.

## Archivos modificados:
- `src/main/resources/static/js/fetch-interceptor.js` — no inyecta JWT en auth endpoints
- `src/main/resources/static/js/script.js` — HTML corregido + dev-section
- `src/main/resources/templates/desarrollador.html` — estructura HTML validada
- `src/main/java/com/daw/tfg/service/CarritoService.java` — precio efectivo + disponibilidad

## También modificados previamente (B2B module):
- `JsonDatabaseInitializer.java` — emails únicos para developers
- `route-protection.js` — `ROLE_DEVELOPER` en lugar de `ROLE_DESARROLLADOR`
- `sidebar.html` — enlace al panel de desarrollador
