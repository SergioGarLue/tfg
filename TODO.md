# 📋 PLAN DETALLADO DE IMPLEMENTACIÓN DEL BACKEND

## Fase 1: Controladores Esenciales (CRUD Básico)
**Objetivo:** dotar a cada controlador de los endpoints básicos CRUD

### 1.1 ControladoraJuego (JuegoService ya existe completo)
```
Endpoints necesarios:
- GET /api/juegos              → Listar todos ✅ YA EXISTE
- GET /api/juegos/{id}         → Obtener por ID ❌ FALTA
- POST /api/juegos             → Crear juego ❌ FALTA
- PUT /api/juegos/{id}         → Actualizar juego ❌ FALTA
- DELETE /api/juegos/{id}      → Eliminar juego ❌ FALTA
- GET /api/juegos/buscar?q=    → Buscar por título ❌ FALTA
- GET /api/juegos/genero/{nombre} → Filtrar por género ❌ FALTA
- GET /api/juegos/precio?min=&max= → Filtrar por precio ❌ FALTA
- GET /api/juegos/desarrollador/{nombre} → Filtrar por desarrollador ❌ FALTA
- GET /api/juegos/editor/{nombre} → Filtrar por editor ❌ FALTA
```

### 1.2 ControladoraUsuario
```
Endpoints necesarios:
- GET /api/usuarios              → Listar todos ✅ YA EXISTE
- GET /api/usuarios/{id}         → Obtener usuario por ID ❌ FALTA
- POST /api/usuarios/registro    → Registrar usuario ❌ FALTA (existe en service)
- PUT /api/usuarios/{id}         → Actualizar usuario ❌ FALTA
- DELETE /api/usuarios/{id}      → Eliminar usuario ❌ FALTA
- GET /api/usuarios/nombre/{username} → Buscar por nombre ❌ FALTA
- PUT /api/usuarios/{id}/password → Cambiar contraseña ❌ FALTA
- POST /api/usuarios/{id}/avatar → Subir avatar ❌ FALTA
```

### 1.3 ControladoraBiblioteca (Colección)
```
Endpoints necesarios:
- GET /api/biblioteca                           → Listar todos ✅ YA EXISTE
- GET /api/biblioteca/usuario/{id}              → Obtener colección de usuario ❌ FALTA
- GET /api/biblioteca/usuario/{id}/favoritos    → Obtener favoritos ❌ FALTA
- POST /api/biblioteca/usuario/{id}/agregar/{juegoId} → Añadir juego ❌ FALTA
- DELETE /api/biblioteca/usuario/{id}/eliminar/{juegoId} → Eliminar juego ❌ FALTA
- PUT /api/biblioteca/{id}/favorito             → Marcar favorito ❌ FALTA
- DELETE /api/biblioteca/{id}/favorito           → Quitar favorito ❌ FALTA
```

### 1.4 ControladoraAmistades
```
Endpoints necesarios:
- GET /api/amistades                       → Listar todos ✅ YA EXISTE
- GET /api/amistades/{id}                  → Obtener por ID ❌ FALTA
- POST /api/amistades/enviar               → Enviar solicitud ❌ FALTA (existe service)
- PUT /api/amistades/{id}/aceptar          → Aceptar solicitud ❌ FALTA (existe service)
- PUT /api/amistades/{id}/rechazar         → Rechazar solicitud ❌ FALTA (existe service)
- DELETE /api/amistades/{id}                → Elimiar amistad ❌ FALTA
- GET /api/amistades/usuario/{id}          → Obtener amigos de usuario ❌ FALTA
- GET /api/amistades/pendientes/{id}        → Obtener solicitudes pendientes ❌ FALTA
```

### 1.5 ControladoraListaDeseados
```
Endpoints necesarios:
- GET /api/lista-deseados                       → Listar todos ✅ YA EXISTE
- GET /api/lista-deseados/{id}                  → Obtener por ID ❌ FALTA
- POST /api/lista-deseados/usuario/{id}/agregar → Añadir a deseados ❌ FALTA
- DELETE /api/lista-deseados/{id}                → Eliminar de deseados ❌ FALTA
- GET /api/lista-deseados/usuario/{id}          → Obtener lista de usuario ❌ FALTA
- GET /api/lista-deseados/usuario/{id}/contiene/{juegoId} → Verificar si contiene ❌ FALTA
```

### 1.6 ControladoraNotificaciones
```
Endpoints necesarios:
- GET /api/notificaciones                  → Listar todos ✅ YA EXISTE
- GET /api/notificaciones/{id}             → Obtener por ID ❌ FALTA
- POST /api/notificaciones                 → Crear notificación ❌ FALTA
- PUT /api/notificaciones/{id}/leida       → Marcar como leída ❌ FALTA
- DELETE /api/notificaciones/{id}          → Eliminar notificación ❌ FALTA
- GET /api/notificaciones/usuario/{id}     → Obtener notificaciones de usuario ❌ FALTA
- PUT /api/notificaciones/usuario/{id}/leidas → Marcar todas como leídas ❌ FALTA
```

### 1.7 ControladoraResenas (Nueva - No existe)
```
Endpoints necesarios:
- GET /api/resenas                         → Listar todos ❌ FALTA
- GET /api/resenas/{id}                    → Obtener por ID ❌ FALTA
- POST /api/resenas                        → Crear reseña ❌ FALTA
- DELETE /api/resenas/{id}                 → Eliminar reseña ❌ FALTA
- GET /api/resenas/juego/{juegoId}         → Obtener reseñas de juego ❌ FALTA
- GET /api/resenas/usuario/{usuarioId}    → Obtener reseñas de usuario ❌ FALTA
- GET /api/resenas/juego/{juegoId}/promedio → Obtener promedio de valoración ❌ FALTA
```

### 1.8 ControladoraTienda
```
Endpoints necesarios:
- GET /api/tienda              → Listar todos ✅ YA EXISTE
- GET /api/tienda/ofertas     → Juegos en oferta ❌ FALTA
- GET /api/tienda/populares    → Juegos populares ❌ FALTA
- GET /api/tienda/nuevos       → Juegos nuevos ❌ FALTA
```

### 1.9 ControladoraConfiguracion
```
Endpoints necesarios:
- GET /api/configuracion/{usuarioId}     → Obtener configuración ❌ FALTA
- PUT /api/configuracion/{usuarioId}     → Guardar configuración ❌ FALTA
```

---

## Fase 2: Autenticación y Seguridad
**Objetivo:** Completar el sistema de login/logout

### 2.1 ControladoraAutenticacion
```
Endpoints necesarios:
- GET /api/autenticacion                   → Listar usuarios (básico) ✅ YA EXISTE
- POST /api/autenticacion/login            → Iniciar sesión ❌ FALTA
- POST /api/autenticacion/logout           → Cerrar sesión ❌ FALTA
- GET /api/autenticacion/usuario-actual    → Obtener usuario actual ❌ FALTA
- GET /api/autenticacion/validar           → Validar token/sesión ❌ FALTA
```

### 2.2 SecurityConfig
- Implementar JWT para autenticación real
- Configurar protección de rutas
- Habilitar CSRF correctamente
- Añadir rate limiting

---

## Fase 3: Compras y Pagos
**Objetivo:** Completar el flujo de compra

### 3.1 CompraService - Ampliar
```
Métodos a añadir:
- findByUsuario(Long usuarioId)              → Obtener compras de usuario
- updateEstado(Long id, EstadoCompra estado) → Actualizar estado de compra
- findByEstado(EstadoCompra estado)         → Filtrar por estado
- getHistorialCompleto(Long usuarioId)      → Historial con detalles
```

### 3.2 ControladoraCompras (Nueva - No existe)
```
Endpoints:
- GET /api/compras                           → Listar compras ❌ FALTA
- GET /api/compras/{id}                      → Obtener compra por ID ❌ FALTA
- GET /api/compras/usuario/{id}              → Historial de compras de usuario ❌ FALTA
- PUT /api/compras/{id}/estado               → Actualizar estado ❌ FALTA
- DELETE /api/compras/{id}                   → Eliminar compra ❌ FALTA
```

### 3.3 MetodoPago (Modelo + Service + Controlador)
```
Endpoints:
- GET /api/metodos-pago/usuario/{id}        → Listar métodos de pago ❌ FALTA
- POST /api/metodos-pago                    → Añadir método de pago ❌ FALTA
- DELETE /api/metodos-pago/{id}             → Eliminar método de pago ❌ FALTA
```

---

## Fase 4: Datos Relacionados
**Objetivo:** Añadir controladores para Editor, Desarrollador, Género

### 4.1 ControladoraDesarrollador (Nueva)
```
Endpoints:
- GET /api/desarrolladores                  → Listar todos ❌ FALTA
- GET /api/desarrolladores/{id}             → Obtener por ID ❌ FALTA
- GET /api/desarrolladores/{id}/juegos     → Juegos del desarrollador ❌ FALTA
```

### 4.2 ControladoraEditor (Nueva)
```
Endpoints:
- GET /api/editores                         → Listar todos ❌ FALTA
- GET /api/editores/{id}                    → Obtener por ID ❌ FALTA
- GET /api/editores/{id}/juegos             → Juegos del editor ❌ FALTA
```

### 4.3 ControladoraGenero (Nueva)
```
Endpoints:
- GET /api/generos                          → Listar todos ❌ FALTA
- GET /api/generos/{id}                     → Obtener por ID ❌ FALTA
- GET /api/generos/{id}/juegos              → Juegos del género ❌ FALTA
```

---

## Fase 5: Extras y Mejoras
**Objetivo:** Añadir funcionalidad adicional

### 5.1 Paginación
- Añadir parámetros page, size a todos los endpoints GET list
- Devolver Page<...> en lugar de List<...>

### 5.2 DTOs
- Crear DTOs para todas las entidades
- Mapper entre Entity ↔ DTO

### 5.3 Manejo de Excepciones
- GlobalExceptionHandler
- Validación de entrada con @Valid

### 5.4 Documentación
- Añadir Swagger/OpenAPI

---

## 📌 ORDEN DE PRIORIDAD SUGERIDO

| Prioridad | Tareas |
|-----------|--------|
| **1. ALTA** | ControladoraJuego (filtros), ControladoraUsuario (CRUD), Autenticación completa |
| **2. ALTA** | ControladoraBiblioteca, ControladoraListaDeseados, CompraService/Controller |
| **3. MEDIA** | ControladoraAmistades, ControladoraNotificaciones, Resenas |
| **4. MEDIA** | Editores, Desarrolladores, Géneros |
| **5. BAJA** | Paginación, DTOs, Swagger, Mejoras seguridad |

---

## 📝 NOTAS

- **JuegoService** ya tiene casi todo implementado, solo falta exponer endpoints
- **ColeccionService** ya tiene lógica, solo falta endpoints
- **AmigoService** ya tiene lógica de enviar/aceptar/rechazar, solo falta endpoints
- **UsuarioService** tiene registro y autenticación, falta exponer más métodos

