# Fix Race Condition & Friends Display - TODO

## Steps
- [x] Edit `src/main/resources/static/js/route-protection.js` - Modify `hasPermissionForRoute()` to only redirect if BOTH access token and refresh token are missing
- [x] Edit `src/main/resources/templates/amigos.html` - Replace `sidebar-loader.js` with `script.js`
- [ ] Edit `src/main/java/com/daw/tfg/service/AmigoService.java` - Fix `obtenerAmigosAceptados()` to compare user IDs instead of object references
- [ ] Edit `src/main/resources/static/js/amigos.js` - Fix `CONECTADO` → `ACTIVO` enum mismatch and improve error handling
- [ ] Verify changes are correct
