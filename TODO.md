# TODO - PLAN INTEGRADO PARA FUNCIONALIDAD COMPLETA

**Estado General**: 53% → Objetivo: 95%+ funcional  
**Cuello de botella**: Frontend JavaScript (Backend 82% listo)

---

## 🔥 URGENTE - SEMANA 1 (Amistades + Notificaciones)

### AMISTADES (2-3 horas)

- [x] Backend 95% completo
- [ ] 1.1 Crear `amigos.js` en `static/js/` (copiar de IMPLEMENTACION_CODIGO.md)
- [ ] 1.2 Conectar botón "Aceptar solicitud" → `PUT /api/amistades/{id}/aceptar`
- [ ] 1.3 Conectar botón "Rechazar solicitud" → `PUT /api/amistades/{id}/rechazar`
- [ ] 1.4 Conectar botón "Eliminar amigo" → `DELETE /api/amistades/{id}`
- [ ] 1.5 Implementar `GET /api/amistades/usuario/{usuarioId}/aceptadas` en AmigoService
- [ ] 1.6 Implementar `GET /api/amistades/usuario/{usuarioId}/pendientes` en AmigoService
- [ ] 1.7 Testing con Postman + navegador
- [ ] 1.8 Agregar badge "n solicitudes" en sidebar

### NOTIFICACIONES (4-5 horas) - CRÍTICO

- [ ] 2.1 Crear enum `TipoNotificacion.java` (COMPRA, SOLICITUD_AMISTAD, AMISTAD_ACEPTADA, RESEÑA, DESCUENTO)
- [ ] 2.2 Refactorizar `Notificacion.java` - agregar campos: usuarioDestino, tipo, leida, fecha, contenido
- [ ] 2.3 Crear/actualizar `NotificacionRepository.java` con queries adicionales
- [ ] 2.4 Expandir `NotificacionService.java` con 8 métodos nuevos (ver IMPLEMENTACION_CODIGO.md)
- [ ] 2.5 Actualizar `ControladoraNotificaciones.java` con 7 endpoints REST
- [ ] 2.6 Crear `notificaciones.html` (ver IMPLEMENTACION_CODIGO.md)
- [ ] 2.7 Crear `notificaciones.js` (ver IMPLEMENTACION_CODIGO.md)
- [ ] 2.8 Crear `notificaciones.css`
- [ ] 2.9 Integrar badge de notificaciones en sidebar
- [ ] 2.10 Auto-refresh cada 10 segundos
- [ ] 2.11 Testing completo

---

## 📌 IMPORTANTE - SEMANA 2 (Reseñas + Gestor B2B + Refactor)

### RESEÑAS (3-4 horas)

- [ ] 3.1 Crear `ResenaController.java` con endpoints REST
  - `POST /api/resenas/{juegoId}` - Crear reseña
  - `GET /api/resenas/juego/{juegoId}` - Listar por juego
  - `PUT /api/resenas/{id}` - Editar propia
  - `DELETE /api/resenas/{id}` - Eliminar propia
- [ ] 3.2 Validar en ResenaService que usuario compró el juego
- [ ] 3.3 Agregar método `calcularCalificacionPromedio()` en JuegoService
- [ ] 3.4 Mostrar calificación en fichas de juego (tienda.html, coleccion.html)
- [ ] 3.5 Crear modal/página para ver/crear reseñas en `juego.html`
- [ ] 3.6 Crear `resenas.js` para conectar con API
- [ ] 3.7 Testing

### GESTOR B2B FRONTEND (2-3 horas)

- [ ] 4.1 Crear `gestor.html` con:
  - Listado de juegos segmentado por género
  - Edición individual de PVP (PATCH)
  - Aplicación masiva de descuentos (POST)
- [ ] 4.2 Crear `gestor.js` para conectar con API
- [ ] 4.3 Integrar con JWT (solo DESARROLLADOR, EDITOR, GESTOR_CONTENIDO)
- [ ] 4.4 Testing

### UNIFICAR DESARROLLADOR + EDITOR (20-30 minutos) ✨

- [ ] 5.1 Crear `Proveedor.java` + `TipoProveedor.java` enum
- [ ] 5.2 Actualizar `Juego.java` (referencias a Proveedor)
- [ ] 5.3 Crear `ProveedorRepository`, `ProveedorService`, `ProveedorController`
- [ ] 5.4 Actualizar `data.sql` con tabla proveedor
- [ ] 5.5 Buscar-reemplazar en HTML/JS: `/api/desarrolladores` → `/api/proveedores`
- [ ] 5.6 Eliminar Editor.java, EditorRepository, EditorService
- [ ] 5.7 Testing
- [ ] 5.8 Commit: "refactor: unificar Desarrollador y Editor en Proveedor"

---

## 🎯 NICE-TO-HAVE - SEMANA 3 (Si queda tiempo)

### MEJORAS OPCIONALES

- [ ] 6.1 WebSocket para notificaciones en tiempo real
- [ ] 6.2 Chat entre amigos
- [ ] 6.3 Dashboard personal mejorado
- [ ] 6.4 Búsqueda de usuarios mejorada
- [ ] 6.5 Perfil público de desarrolladores
- [ ] 6.6 Estadísticas de ventas (solo para admin/gestor)

### DOCUMENTACIÓN PARA TRIBUNAL

- [ ] 7.1 `docs/diagrama-conceptual.md` (Mermaid)
- [ ] 7.2 `docs/slide-tecnico.md` (explicación módulos)
- [ ] 7.3 `docs/guion-defensa.md` (argumentario)

---

## B2B - GESTOR DE CONTENIDO (ORIGINAL - YA COMPLETADO) ✅

- [x] 1.1 `RolesUsuarios.java` - Agregar rol `GESTOR_CONTENIDO`  
- [x] 1.2 `Juego.java` - Agregar método `getPrecioFinal()`  
- [x] 1.3 `JuegoRepository.java` - Agregar `findByGenerosId(Long idGenero)`  
- [x] 1.4 `SecurityConfig.java` - Agregar reglas `/gestor/**` con roles adecuados
- [x] 1.5 `ViewController.java` - Agregar `@GetMapping("/gestor")`  
- [x] 2.1 `GestorContenidoService.java` - Métodos completos
- [x] 2.2 `GestorController.java` - Endpoints REST implementados
- [ ] 3.1 Frontend `gestor.html` - **PENDIENTE (semana 2)**

---

## 📊 RESUMEN DE PRIORIDADES

| Prioridad | Tarea | Horas | Dependencias |
|-----------|-------|-------|--------------|
| 🔥 CRÍTICA | Amistades JS | 3 | Ninguna |
| 🔥 CRÍTICA | Notificaciones | 5 | Ninguna |
| 📌 ALTA | Reseñas | 4 | Ninguna |
| 📌 ALTA | Gestor.html | 3 | GestorController (✅) |
| 📌 ALTA | Unificar Dev/Editor | 0.5 | Ninguna |
| 🟢 MEDIA | WebSocket (opcional) | 3 | Notificaciones |
| 🟢 MEDIA | Docs Tribunal | 2 | Todo completo |
| **TOTAL** | | **~20h** | |

---

## ✅ VERIFICACIÓN FINAL

### Backend Checklist

- [ ] `mvn clean compile` sin errores
- [ ] Todos los endpoints en Postman funcionan
- [ ] JWT en rutas protegidas
- [ ] Validaciones de permisos por rol

### Frontend Checklist

- [ ] Todos los `.js` hacen fetch correcto
- [ ] Errores se muestran al usuario
- [ ] Interceptor JWT en todas las llamadas
- [ ] Sidebar con badges de notificaciones

### BD Checklist

- [ ] Migración a tabla proveedor (si aplica)
- [ ] Foreign keys correctas
- [ ] Data de prueba en data.sql
- [ ] No hay errores de integridad

---

## 📁 DOCUMENTOS DE REFERENCIA GENERADOS

Todos estos archivos están en la raíz del proyecto:

1. **ANALISIS_PENDIENTES.md** - Análisis detallado + tablas
2. **IMPLEMENTACION_CODIGO.md** - Código listo para copiar-pegar
3. **RESUMEN_EJECUTIVO.md** - Resumen para tribunal
4. **REFACTOR_DESARROLLADOR_EDITOR.md** - Plan completo del refactor
5. **Este archivo** - Plan integrado

---

**📝 Nota**: Actualizar este archivo conforme completes tareas.  
