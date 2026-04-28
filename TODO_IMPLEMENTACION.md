## Requisitos exactos del usuario:
1. AdminInitializer: crear rol DEVELOPER, 2+ usuarios de prueba, asociar juegos
2. Admin (admin.html): listar usuarios, asignar ROLE_DEVELOPER via PUT con DTO estandar
3. Panel B2B (desarrollador.html/js): tabla de juegos del dev, editar precio, descuento, activar/desactivar
4. Backend: @PreAuthorize, dev solo edita juegos donde desarrollador_id coincida con su usuario
5. Precio modificado se persiste en BD

## Archivos a modificar/crear:

### Backend
- [x] `RolesUsuarios.java` - Anadir `DEVELOPER` (como reemplazo principal de DESARROLLADOR)
- [x] `AdminInitializer.java` - Crear 2+ usuarios DEVELOPER (developer1, developer2)
- [x] `JsonDatabaseInitializer.java` - Vincular DEVELOPER users a multiples Desarrolladores post-import
- [x] `DesarrolladorB2BService.java` - Validar rol DEVELOPER, ownership por desarrollador_id
- [x] `DesarrolladorController.java` - @PreAuthorize("hasRole('DEVELOPER')"), usar Principal
- [x] `AdminController.java` - DTO estandar para asignacion de roles, endpoints de desarrolladores

### Frontend
- [x] `administrador.html` - Seccion de gestion de usuarios con tabla y asignador de roles
- [x] `desarrollador.html` - Panel B2B limpio con tabla editable
- [x] `desarrollador.js` - Logica de carga, edicion inline, descuento, activar/desactivar
- [x] `route-protection.js` - Actualizado a ROLE_DEVELOPER
- [x] `script.js` - Mostrar panel dev para ROLE_DEVELOPER

## Estado: COMPLETADO
