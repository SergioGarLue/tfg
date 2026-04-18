# Panel Administrador - Guía de Uso

## 📋 Descripción

Panel administrativo completo con funcionalidades para:
1. **Ejecutar Scraper de Steam** - Descargar e importar juegos automáticamente
2. **Buscar Usuarios** - Por nombre o por ID
3. **Eliminar Usuarios** - Borrar usuarios del sistema

## 🔒 Seguridad

- ⚠️ **Solo usuarios con rol ADMIN pueden acceder**
- Protegido con JWT (JSON Web Tokens)
- Anotación `@PreAuthorize("hasRole('ADMIN')")` en todos los endpoints

## 🚀 Características Implementadas

### 1. Scraper de Steam (`/api/admin/steam-scraper`)
- **Método:** POST
- **Descripción:** Ejecuta el script Python `siis.py` y importa los datos a la BD
- **Proceso:**
  1. Ejecuta el script Python para scrapear Steam
  2. Obtiene el archivo JSON generado
  3. Parsea los datos
  4. Crea/Actualiza desarrolladores y géneros
  5. Guarda los juegos en la BD
- **Respuesta:** String con el progreso y resultados

### 2. Buscar Usuario por Nombre (`/api/admin/usuario/buscar?nombreUsuario={nombre}`)
- **Método:** GET
- **Parámetro:** `nombreUsuario` (query parameter)
- **Respuesta:** JSON con datos del usuario (ID, nombre, email, rol)
- **Ejemplo:**
  ```
  GET /api/admin/usuario/buscar?nombreUsuario=juan_perez
  ```

### 3. Obtener Usuario por ID (`/api/admin/usuario/{idUsuario}`)
- **Método:** GET
- **Parámetro:** `idUsuario` (path parameter)
- **Respuesta:** JSON con datos del usuario
- **Ejemplo:**
  ```
  GET /api/admin/usuario/1
  ```

### 4. Eliminar Usuario (`/api/admin/usuario/{idUsuario}`)
- **Método:** DELETE
- **Parámetro:** `idUsuario` (path parameter)
- **Respuesta:** Mensaje de confirmación
- **Ejemplo:**
  ```
  DELETE /api/admin/usuario/1
  ```

## 📁 Archivos Creados/Modificados

### Nuevos Archivos:
1. **SteamImportService.java** - Servicio para ejecutar scraper e importar datos
2. **AdminController.java** - Controlador con endpoints administrativos
3. **administrador.html** - Vista HTML del panel admin
4. **administrador.css** - Estilos del panel admin

### Archivos Modificados:
1. **SecurityConfig.java** - Habilitada anotación `@EnableMethodSecurity`

## 🎯 Acceso al Panel

URL: `http://localhost:8080/administrador.html`

### Requisitos:
1. Tener un usuario con rol **ADMIN**
2. Estar autenticado (tener un JWT válido guardado en `localStorage.token`)

## 🔧 Configuración

### Ubicación del Script Python
```
src/main/resources/static/JSON/siis.py
```

### Archivo de Salida
```
src/main/resources/static/JSON/steam_top_1000_sellers.json
```

## 💡 Notas Importantes

1. **Ejecución del Scraper:**
   - Puede tardar varios minutos
   - No cierres la página durante la ejecución
   - El servidor continuará ejecutándose aunque cierres la interfaz

2. **Importación de Datos:**
   - Verifica automaticamente si los juegos ya existen
   - Crea nuevos desarrolladores y géneros si es necesario
   - Maneja excepciones de forma segura

3. **Gestión de Usuarios:**
   - El sistema valida que el usuario existe antes de eliminar
   - Las búsquedas son case-sensitive
   - Se devuelven errores descriptivos

## 📊 Estructura de Respuesta del Usuario

```json
{
  "idUsuario": 1,
  "nombreUsuario": "juan_perez",
  "correoElectronico": "juan@example.com",
  "rol": "ADMIN"
}
```

## 🐛 Troubleshooting

### No puedo acceder al panel
- ✅ Verifica que tienes rol ADMIN
- ✅ Verifica que el token JWT esté en localStorage
- ✅ Intenta limpiar el localStorage y vuelve a iniciar sesión

### El scraper no inicia
- ✅ Verifica que Python está instalado en el sistema (`python --version`)
- ✅ Verifica que el archivo `siis.py` existe
- ✅ Revisa la consola del servidor (logs)

### No encuentro los datos importados
- ✅ El scraper puede tardar varios minutos
- ✅ Intenta actualizar la página (`F5`)
- ✅ Revisa si hay errores en los logs del servidor

## 🔐 Mejoras de Seguridad Recomendadas

1. Implementar logging de acciones administrativas
2. Añadir confirmación de dos factores para operaciones críticas
3. Implementar rate limiting en los endpoints admin
4. Auditoría de cambios en la BD
5. Encripción de datos sensibles adicional

---

**Versión:** 1.0
**Fecha:** 2026-04-18
**Autor:** Sistema TFG
