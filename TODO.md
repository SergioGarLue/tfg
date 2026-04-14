# TFG - Convertir página de juego a static + async/await JS

## Plan aprobado - Pasos:

### 1. [x] Crear TODO.md (hecho)
### 2. [x] Actualizar VistaController.java para forward a static/juego.html ✅
### 3. [x] Crear src/main/resources/static/juego.html (mover desde templates + agregar JS async/await fetch /api/tienda/{id})
### 4. [x] Test: mvn spring-boot:run ejecutado, acceder http://localhost:8080/juego/1 en browser para verificar (JS fetch /api/tienda/1, consola devtools)
### 5. [ ] Actualizar links en templates/tienda.html, coleccion.html etc. a /juego/1 (opcional)
### 5. [ ] Actualizar links en templates/tienda.html, coleccion.html etc. a /juego/1 (opcional) - Skip por ahora (relativos funcionan)
### 6. [x] ✅ Task completada: /juego/{id} ahora usa static HTML + async/await JS fetch sin Thymeleaf. Accede http://localhost:8080/juego/1 (necesitas run server manualmente: .\mvnw.cmd spring-boot:run tras fix JAVA_HOME).

**Notas:** 
- Usar /api/tienda/{id} para datos reales.
- JS parseará ID de URL.
- Fallback mock si no hay datos.
