# Modulo B2B DESARROLLADOR - Implementacion Completada

## Resumen
Se ha implementado un modulo B2B completo para el rol `DESARROLLADOR` que permite a usuarios vinculados a una empresa desarrolladora gestionar los precios, descuentos y disponibilidad de sus juegos de forma segura.

---

## Archivos Modificados

### 1. Entidades
- **src/main/java/com/daw/tfg/models/Usuario.java**
  - Anadida relacion `@ManyToOne` con `Desarrollador` (`id_desarrollador_usuario`, nullable)
- **src/main/java/com/daw/tfg/models/Juego.java**
  - Anadido campo `disponible` (Boolean, default true)
  - Anadido campo `precioRebajado` (Double, nullable)

### 2. Repositorios
- **src/main/java/com/daw/tfg/repository/JuegoRepository.java**
  - Anadido metodo `findByDesarrolladorIdDesarrollador(Long idDesarrollador)`

### 3. Servicios
- **src/main/java/com/daw/tfg/service/DesarrolladorB2BService.java** (NUEVO)
  - `obtenerMisJuegos(String username)`: Lista juegos del desarrollador vinculado al usuario
  - `actualizarJuego(...)`: Actualiza precio, descuento, precio rebajado y disponibilidad con validacion de propiedad
- **src/main/java/com/daw/tfg/service/CustomUserDetails.java** (NUEVO/SOBRESCRITO)
  - Fuerza carga eager de `desarrollador` en `loadUserByUsername`

### 4. Controladores
- **src/main/java/com/daw/tfg/controllers/DesarrolladorController.java** (NUEVO)
  - `@PreAuthorize("hasRole('DESARROLLADOR')")`
  - `GET /api/desarrollador/mis-juegos`
  - `PUT /api/desarrollador/juego/{idJuego}`
- **src/main/java/com/daw/tfg/controllers/AdminController.java** (ACTUALIZADO)
  - `GET /api/admin/usuarios` - incluye nombre de desarrollador
  - `GET /api/admin/usuario/{id}` - incluye nombre de desarrollador
  - `GET /api/admin/usuario/buscar` - incluye nombre de desarrollador
  - `PUT /api/admin/usuario/{id}/rol` - asigna rol + desarrollador
  - `GET /api/admin/desarrolladores` - lista empresas desarrolladoras
  - `DELETE /api/admin/usuario/{id}` - elimina usuario

### 5. DTOs (NUEVOS)
- **src/main/java/com/daw/tfg/dtos/ActualizarJuegoDesarrolladorDTO.java**
  - Campos: `precio`, `porcentaje`, `precioRebajado`, `disponible`
- **src/main/java/com/daw/tfg/dtos/AsignarRolDesarrolladorDTO.java**
  - Campos: `rol`, `idDesarrollador`

### 6. Frontend
- **src/main/resources/templates/desarrollador.html** (SOBRESCRITO)
  - Panel B2B con tarjetas de estadisticas, tabla editable de juegos, notificaciones toast
- **src/main/resources/static/js/desarrollador.js** (NUEVO)
  - Carga juegos via JWT, renderiza tabla con inputs inline, maneja estados (disponible/gratis/no-disponible), envia actualizaciones PUT
- **src/main/resources/static/estilos/desarrollador.css** (SOBRESCRITO)
  - Estilos para panel, tabla, badges, inputs y toast
- **src/main/resources/templates/administrador.html** (SOBRESCRITO)
  - Panel admin actualizado con seccion para asignar rol DESARROLLADOR + empresa desarrolladora

### 7. Sidebar / Navegacion
- **src/main/resources/static/sidebar.html**
  - Anadido `<li id="dev-section" style="display:none;">` con enlace a `/desarrollador`
- **src/main/resources/static/js/script.js** (SOBRESCRITO)
  - Logica para mostrar/ocultar seccion desarrollador segun `ROLE_DESARROLLADOR`

### 8. Inicializacion de Datos
- **src/main/java/com/daw/tfg/configuration/JsonDatabaseInitializer.java** (ACTUALIZADO)
  - Crea automaticamente usuario `devuser` / `Dev@123!` con rol DESARROLLADOR vinculado al primer desarrollador importado del JSON

---

## Seguridad

| Aspecto | Implementacion |
|---------|---------------|
| Autenticacion | JWT via `JwtAuthenticationFilter` |
| Autorizacion | `@PreAuthorize("hasRole('DESARROLLADOR')")` en controller |
| Propiedad | Servicio verifica `juego.desarrollador.id == usuario.desarrollador.id` |
| Aislamiento | Dev solo ve/modifica juegos de SU empresa |

---

## Endpoints API

### Desarrollador (requiere ROLE_DESARROLLADOR)
```
GET  /api/desarrollador/mis-juegos     -> Lista mis juegos
PUT  /api/desarrollador/juego/{id}     -> Actualizar juego (body: ActualizarJuegoDesarrolladorDTO)
```

### Admin (requiere ROLE_ADMIN)
```
GET  /api/admin/usuarios               -> Listar usuarios
GET  /api/admin/usuario/{id}           -> Ver usuario por ID
GET  /api/admin/usuario/buscar?nombreUsuario=xxx  -> Buscar por nombre
PUT  /api/admin/usuario/{id}/rol       -> Asignar rol + desarrollador
GET  /api/admin/desarrolladores        -> Listar empresas desarrolladoras
DELETE /api/admin/usuario/{id}         -> Eliminar usuario
POST /api/admin/steam-scraper          -> Ejecutar scraper Steam
```

---

## Flujo de Uso

1. **Admin** accede a `/admin` y asigna rol `DESARROLLADOR` + empresa a un usuario existente
2. **Usuario** con rol `DESARROLLADOR` inicia sesion
3. El sidebar muestra "Panel Desarrollador"
4. Al acceder a `/desarrollador`, ve una tabla con los juegos de SU empresa
5. Puede editar precio, descuento, precio rebajado y cambiar estado (disponible/gratis/no-disponible)
6. Las actualizaciones se envian via PUT a `/api/desarrollador/juego/{id}`
7. El backend valida que el juego pertenezca al desarrollador del usuario autenticado

---

## Datos de Prueba

| Usuario | Contrasena | Rol | Desarrollador |
|---------|-----------|-----|---------------|
| admin | Admin@123! | ADMIN | - |
| devuser | Dev@123! | DESARROLLADOR | Primer desarrollador del JSON |

---

## Proximos Pasos Sugeridos (Opcional)

- [ ] Anadir paginacion en la tabla de juegos del desarrollador
- [ ] Implementar busqueda/filtros dentro del panel B2B
- [ ] Anadir graficos de ventas/estadisticas
- [ ] Notificaciones por email al cambiar precios
- [ ] Validacion de precio rebajado < precio original
- [ ] Tests unitarios para `DesarrolladorB2BService`
