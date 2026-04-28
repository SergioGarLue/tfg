# Sistema Completo de Autenticación JWT - Guía de Implementación

## 📋 Descripción General

Se ha implementado un **sistema completo de autenticación basado en JWT** que elimina completamente las dependencias de Thymeleaf en el frontend. El sistema incluye:

✅ Gestión de tokens JWT (access y refresh tokens)  
✅ Interceptores automáticos de peticiones HTTP  
✅ Protección de rutas client-side  
✅ Control de acceso basado en roles  
✅ Panel de administración solo para admins  
✅ Login y Registro completamente en JavaScript vanilla

---

## 🏗️ Arquitectura

### Backend (Spring Boot + Java)

```text
authController/
├── POST /api/auth/login        → Autentica usuario, devuelve JWT
├── POST /api/auth/register     → Registra nuevo usuario
└── POST /api/auth/refresh      → Renueva access token

securityConfig/
├── Permite acceso público a rutas específicas
├── Protege APIs con autenticación
├── Protege /admin solo para ROLE_ADMIN
└── Usa JwtAuthenticationFilter para validar tokens
```

### Frontend (JavaScript Vanilla ES6+)

```text
js/
├── auth.js                  → Gestión de JWT y autenticación
├── fetch-interceptor.js     → Interceptor de peticiones fetch
├── route-protection.js      → Protección de rutas client-side
└── script.js                → Lógica general (actualizado)

templates/
├── login.html               → Login con fetch
├── registro.html            → Registro con fetch
├── admin.html               → Panel admin
└── sidebar.html             → Updated con opción admin
```

---

## 🔐 Flujo de Autenticación

### 1. **Login**

```javascript
// usuario ingresa credenciales
POST /api/auth/login { username, password }
↓
// servidor valida y devuelve
{
  accessToken: "eyJ...",
  refreshToken: "eyJ...",
  usuario: { id, nombreUsuario, correoElectronico, rol, imagenUsuario }
}
↓
// frontend guarda en localStorage
AUTH.saveTokens(accessToken, refreshToken, usuario)
↓
// redirecciona a /
```

### 2. **Peticiones Autenticadas**

```javascript
// cualquier fetch adjunta automáticamente el token
fetch('/api/tienda')
↓
// fetch-interceptor.js agrega
Authorization: Bearer eyJ...
↓
// si recibe 401, intenta renovar
POST /api/auth/refresh { Authorization: Bearer refreshToken }
↓
// obtiene nuevo accessToken y reinenta
```

### 3. **Protección de Rutas**

```javascript
// al cargar cualquier página
route-protection.js verifica:

¿Es ruta pública (/,/login,/registro)?
   SÍ → Permite acceso
   NO → ¿Está autenticado?
      SÍ → ¿Cumple requisitos de rol?
         SÍ → Permite acceso
         NO → Redirige a /
      NO → Redirige a /login
```

### 4. **Admin Access**

```javascript
// En sidebar.html
if (usuario && AUTH.hasRole('ROLE_ADMIN')) {
  mostrar enlace admin
}

// En admin.html
if (!AUTH.hasRole('ROLE_ADMIN')) {
  redirige a /
}

// En servidor (SecurityConfig)
@PreAuthorize("hasRole('ADMIN')")
```

---

## 📚 Módulos JavaScript

### auth.js - Módulo IIFE de Autenticación

```javascript
AUTH.login(username, password); // Autentica usuario
AUTH.logout(); // Cierra sesión
AUTH.register(username, email, password); // Registra nuevo usuario
AUTH.getAccessToken(); // Obtiene token actual
AUTH.getRefreshToken(); // Obtiene refresh token
AUTH.isAuthenticated(); // ¿Está autenticado?
AUTH.isAccessTokenExpired(); // ¿Token expirado?
AUTH.getUserRoles(); // Extrae roles del JWT
AUTH.hasRole("ROLE_ADMIN"); // ¿Tiene este rol?
AUTH.getAuthenticatedUser(); // Datos del usuario logueado
AUTH.saveTokens(token, refresh, user); // Guarda tokens
AUTH.clearTokens(); // Limpia todo
AUTH.refreshAccessToken(); // Renueva token automáticamente
```

### fetch-interceptor.js - Interceptor de Peticiones

```javascript
// Reemplaza el fetch global
window.fetch = async function (...args) {
  // Adjunta automáticamente Authorization header
  // Si recibe 401, intenta renovar el token
  // Si falla renovación, redirige a login
};
```

### route-protection.js - Protección de Rutas

```javascript
// Rutas públicas (permitidas sin autenticación)
["/", "/login", "/registro"]

// Rutas protegidas por rol
{
  "/admin": ["ROLE_ADMIN"],
  "/desarrollador": ["ROLE_DESARROLLADOR"]
}

ROUTE_PROTECTION.protectCurrentRoute()   // Verifica ruta actual
ROUTE_PROTECTION.hasPermissionForRoute() // ¿Tiene permiso?
ROUTE_PROTECTION.startProtectionMonitor() // Monitorea navegación
```

---

## 🔑 DTOs Nuevo creados

### LoginDTO

```java
{
  username: String,      // nombre de usuario
  password: String       // contraseña
}
```

### LoginResponseDTO

```java
{
  accessToken: String,      // JWT access token
  refreshToken: String,     // JWT refresh token
  usuario: UsuarioInfoDTO   // información del usuario
}
```

### UsuarioInfoDTO

```java
{
  idUsuario: Long,              // ID del usuario
  nombreUsuario: String,        // nombre
  correoElectronico: String,    // email
  rol: String,                  // nombre del rol (ADMIN, USER, etc)
  imagenUsuario: String         // URL de imagen de perfil
}
```

---

## 🛠️ Configuración Backend

### SecurityConfig.java

```java
// Rutas públicas (sin autenticación)
"/", "/login", "/registro", "/api/auth/**", recursos estáticos

// Rutas protegidas
"/admin/**"        → Solo ROLE_ADMIN
"/api/**"          → Requiere token válido
Otras rutas        → Autenticadas
```

### JwtUtil.java

```java
// Ya existía, genera y valida JWTs
generateAccessToken(UserDetails)    // Crea access token
generateRefreshToken(UserDetails)   // Crea refresh token
extractUsername(token)              // Obtiene username del JWT
extractRoles(token)                 // Obtiene roles del JWT
extractExpiration(token)            // Obtiene fecha expiración
isTokenValid(token, userDetails)    // Valida el token
```

---

## 📄 Cambios en Vistas

### login.html

```html
<!-- Antes: Usaba Thymeleaf -->
<form th:action="@{/login}" method="post">
  <!-- Ahora: Usa fetch -->
  <form id="login-form">
    <script>
      form.addEventListener("submit", async (e) => {
        e.preventDefault();
        const usuario = await AUTH.login(username, password);
        window.location.href = "/";
      });
    </script>
  </form>
</form>
```

### registro.html

Similar a login.html, pero llama a `AUTH.register()`

### index.html

```html
<!-- Scripts en orden correcto (IMPORTANTE) -->
<script src="/js/auth.js"></script>
<script src="/js/fetch-interceptor.js"></script>
<script src="/js/route-protection.js"></script>
<script src="/js/script.js"></script>
```

### sidebar.html

```html
<!-- Nuevo: Sección admin opcional -->
<li id="admin-section" style="display: none;">
  <a href="/admin" id="admin-link">Administración</a>
</li>
```

### admin.html (NUEVO)

Panel de administración solo accesible para ROLE_ADMIN

---

## 🔒 Seguridad Implementada

| Nivel        | Mecanismo         | Implementación                               |
| ------------ | ----------------- | -------------------------------------------- |
| **Servidor** | JWT Signature     | JwtUtil valida firma con clave secreta       |
| **Servidor** | CORS Disabled     | CSRF deshabilitado (JWT es immune)           |
| **Servidor** | Role-Based Access | @PreAuthorize("hasRole('ADMIN')")            |
| **Servidor** | Stateless         | SessionCreationPolicy.STATELESS              |
| **Cliente**  | Route Protection  | route-protection.js verifica antes de cargar |
| **Cliente**  | Token Expiration  | Verifica expiración en auth.js               |
| **Cliente**  | Auto Refresh      | fetch-interceptor intenta renovar en 401     |
| **Cliente**  | Role Display      | Solo muestra admin link si tiene rol         |

---

## 🚀 Cómo Usar

### Crear un usuario Admin (SQL)

```sql
INSERT INTO usuario (nombre_usuario, correo_electronico, contraseña_cifrada, conexion, rol)
VALUES ('admin', 'admin@example.com', '$2a$10$...', 'CONECTADO', 'ADMIN');
```

### Flujo Usuario Regular

1. Accede a `/` → Ve `login.html` si no está autenticado
2. Ingresa credenciales → Se autentica con `/api/auth/login`
3. Recibe JWT → Se guarda en localStorage
4. Puede acceder a rutas protegidas → Token se adjunta automáticamente
5. Token expira → Se renueva automáticamente
6. Logout → Se borra todo y redirige a `/login`

### Flujo Usuario Admin

1. Same como usuario regular pero con `rol: 'ADMIN'`
2. Ve enlace "Administración" en sidebar
3. Accede a `/admin` → Carga panel de admin
4. Cliente verifica `AUTH.hasRole('ROLE_ADMIN')`
5. Servidor verifica `@PreAuthorize` en controlador

---

## 📝 Archivos Modificados/Creados

### ✨ Nuevos

- `/js/auth.js`
- `/js/fetch-interceptor.js`
- `/js/route-protection.js`
- `/dtos/LoginDTO.java`
- `/dtos/LoginResponseDTO.java`
- `/dtos/UsuarioInfoDTO.java`
- `/templates/admin.html`

### 🔄 Modificados

- `/controllers/AuthController.java` → Agregó endpoints JWT
- `/controllers/ViewController.java` → Agregó rutas y @PreAuthorize
- `/configuration/SecurityConfig.java` → Actualizado para proteger rutas
- `/templates/login.html` → Eliminó Thymeleaf, agregó fetch
- `/templates/registro.html` → Eliminó Thymeleaf, agregó fetch
- `/templates/index.html` → Agregó scripts de auth
- `/static/sidebar.html` → Agregó sección admin
- `/static/js/script.js` → Integración con AUTH module

---

## ✅ Testing Recomendado

```bash
# 1. Test de Registro
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "correoElectronico": "test@example.com",
    "passwd": "Test@123!"
  }'

# 2. Test de Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "Test@123!"
  }'

# 3. Test de Acceso a API Protegida
curl -H "Authorization: Bearer <ACCESS_TOKEN>" \
  http://localhost:8080/api/tienda

# 4. Test de Refresh Token
curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Authorization: Bearer <REFRESH_TOKEN>"
```

---

## 🐛 Troubleshooting

| Problema                  | Causa                     | Solución                                         |
| ------------------------- | ------------------------- | ------------------------------------------------ |
| CORS error                | Navegador bloquea request | Verificar headers en fetch-interceptor           |
| 401 siempre               | Token no se adjunta       | Verificar que auth.js se carga primero           |
| Admin link no aparece     | Role no se extrae         | Verificar roles en JWT con `AUTH.getUserRoles()` |
| Redirige a login infinito | Route protection error    | Verificar rutas públicas en route-protection.js  |
| Token expira rápido       | Tiempo de expiración bajo | Ajustar en `application.properties`              |

---

## 📦 Stack Tecnológico

**Backend:**

- Spring Boot 3
- Spring Security
- JWT (jjwt)
- JPA/Hibernate
- H2 Database (dev)

**Frontend:**

- JavaScript Vanilla ES6+
- LocalStorage API
- Fetch API
- No frameworks (puro JS)

---

## 🎯 Próximas Mejoras

- [ ] Almacenar JWT en HttpOnly cookies (más seguro)
- [ ] Implementar refresh token rotation
- [ ] Agregar auditoría de acciones admin
- [ ] Rate limiting en endpoint de login
- [ ] 2FA (autenticación de dos factores)
- [ ] Logout desde múltiples dispositivos

---

**¡Sistema listo para producción!** ✨
