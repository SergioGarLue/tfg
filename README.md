# 🎮 Xyron - Plataforma de Videojuegos

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![H2](https://img.shields.io/badge/H2-In--Memory-90ED7D?style=for-the-badge&logo=data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMzIiIGhlaWdodD0iMzIiIHZpZXdCb3g9IjAgMCAzMiAzMiIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPGNpcmNsZSBjeD0iMTYiIGN5PSIxNiIgcj0iMTYiIGZpbGw9IiM5MEVENCVDIi8+Cjx0ZXh0IHg9IjE2IiB5PSIyMCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjEyIiBmaWxsPSJ3aGl0ZSIgdGV4dC1hbmNob3I9Im1pZGRsZSI+SDI8L3RleHQ+Cjwvc3ZnPg==)
![Stripe](https://img.shields.io/badge/Stripe-Payments-635BFF?style=for-the-badge&logo=stripe&logoColor=white)

## 📋 Índice

- [Características](#características-principales)
- [Stack](#stack-tecnológico)
- [Instalación](#instalación-rápida-local-dev)
- [Estructura](#estructura-del-proyecto)
- [Endpoints](#endpoints-completos)
- [Base de Datos](#base-de-datos)
- [Desarrolladores](#desarrolladores)

**Xyron** gaming platform con Stripe, social features.

**Demo:** `./mvnw spring-boot:run` → localhost:8080
[... resto igual, pero change all ./mvnw to ./mvnw in code blocks]

**Xyron** es una **plataforma web completa de compra de videojuegos** con funcionalidades sociales y de gestión personal. Inspirada en Steam, permite **comprar juegos con Stripe**, gestionar **bibliotecas/colecciones**, **listas de deseados**, **amistades** y más. Fullstack monolith con backend REST + frontend server-rendered (Vanilla JS).

[![Screenshot Tienda](https://via.placeholder.com/800x400/1e3a8a/ffffff?text=Tienda+de+Videojuegos)](https://via.placeholder.com/) [![Screenshot Carrito](https://via.placeholder.com/800x400/0f766e/ffffff?text=Carrito+y+Stripe)](https://via.placeholder.com/)

## 🚀 Características Principales

| Feature                     | Descripción                                                                           |
| --------------------------- | ------------------------------------------------------------------------------------- |
| 🛒 **Tienda**               | Catálogo juegos, búsqueda título/género/desarrollador/editor/precio (`/tienda.html`). |
| 🛍️ **Carrito**              | Añadir/eliminar, checkout Stripe (`/carrito.html`).                                   |
| 📚 **Biblioteca/Colección** | Juegos comprados (`/coleccion.html`).                                                 |
| 💖 **Lista Deseados**       | Wishlist (`/deseados.html`).                                                          |
| 👥 **Amigos**               | Amigos/conectados (`/amigos.html`).                                                   |
| 👤 **Perfil**               | Configuración/pagos (`/perfil.html`, `/configuracion.html`).                          |
| 🔔 **Notificaciones**       | Compras/actividad.                                                                    |
| 🏢 **Desarrollador**        | Info devs (`/desarrollador.html`).                                                    |
| 🔧 **Admin**                | Panel gestión ([ADMIN_PANEL_GUIDE.md](ADMIN_PANEL_GUIDE.md)) (`/administrador.html`). |
| 🔐 **Auth**                 | Login/registro JWT (`/login.html`, `/registro.html`).                                 |
| 🏠 **Home**                 | Index (`/`).                                                                          |

## 🛠️ Stack Tecnológico

| Backend                                                             | Frontend                                 | DB                     | Otros                             |
| ------------------------------------------------------------------- | ---------------------------------------- | ---------------------- | --------------------------------- |
| Spring Boot 3.x, Spring Data JPA, Spring Security (JWT), Stripe SDK | Vanilla JS/CSS, FontAwesome 7 | H2 (dev), MySQL (prod) | Lombok, dotenv-java (.env), Maven |

## ⚙️ Instalación Rápida (Local Dev)

### 1. Prerrequisitos

- Java 21+
- Maven 3.6+
- [Cuenta Stripe test](https://dashboard.stripe.com/test/apikeys).

### 2. Configurar .env (fuera de src/)

Copia `.env.example` → `.env` (raíz):

```bash
cp .env.example .env
```

Edita con keys reales:

```text
STRIPE_SECRET_KEY=sk_test_...
STRIPE_WEBHOOK_SECRET=whsec_...
```

(Cargado auto por dotenv-java en `TfgApplication.main()` , overrides properties.)

### 3. Run

```bash
./mvnw clean spring-boot:run
```

- App: <http://localhost:8080>
- H2: <http://localhost:8080/h2-console> (jdbc:h2:mem:tfgdb, sa/ )
- Test card: 4242424242424242

## 📁 Estructura del Proyecto

```text
TFG-/
├── .env.example       # Template Stripe keys
├── pom.xml
├── src/main/java/com/daw/tfg/
│   ├── controllers/   # API REST + MVC views
│   ├── service/
│   ├── models/ entities
│   └── TfgApplication.java  # dotenv load
├── src/main/resources/
│   ├── templates/     # .html 
│   ├── static/js css images JSON (Steam data)
│   ├── application.properties  # Defaults
│   └── data.sql       # DB init
├── README.md
├── TODO.md
└── ADMIN_PANEL_GUIDE.md
```

## 🌐 Endpoints Completos

### Vistas (MVC)

| GET            | Ruta           | Template           |
| -------------- | -------------- | ------------------ |
| /              | /              | index.html         |
| /tienda        | /tienda        | tienda.html        |
| /carrito       | /carrito       | carrito.html       |
| /perfil        | /perfil        | perfil.html        |
| /amigos        | /amigos        | amigos.html        |
| /coleccion     | /coleccion     | coleccion.html     |
| /configuracion | /configuracion | configuracion.html |
| /desarrollador | /desarrollador | desarrollador.html |
| /juego/{id}    | /juego/{id}    | juego.html         |
| /registro      | /registro      | registro.html      |
| /login         | /login         | login.html         |
| /administrador | /administrador | administrador.html |
| /deseados      | /deseados      | deseados.html      |

### API REST (AJAX from controllers)

| Method          | Endpoint                        | Función               |
| --------------- | ------------------------------- | --------------------- |
| GET             | /api/tienda                     | Todos juegos          |
| GET             | /api/tienda/{id}                | Juego ID              |
| GET             | /api/tienda/buscar?titulo=...   | Búsq título           |
| GET             | /api/tienda/genero/{gen}        | Por género            |
| GET             | /api/tienda/precio?min=&max=    | Rango precio          |
| GET             | /api/tienda/desarrollador/{dev} | Por dev               |
| GET             | /api/tienda/editor/{edit}       | Por editor            |
| POST            | /api/carrito/add                | Add carrito           |
| DELETE          | /api/carrito/remove/{id}        | Remove carrito        |
| POST            | /stripe/checkout                | Create Stripe session |
| GET             | /api/perfil                     | User profile          |
| /api/biblioteca | Library                         | User library          |
| /api/coleccion  | Collection                      | User collection       |
| /api/amigos     | Friends                         | Friends list          |
| ...             | More in controllers/            | See source            |

## 🗄️ Base de Datos

- **Tablas**: usuario, juego (precio, generos[], plataformas[], screenshots), carrito (juegos m2m), compra (paymentIntentId), lista_deseados, coleccion_favoritos, notificacion, genero.
- Init: data.sql + JPA create-drop.

## 👥 Desarrolladores

| Nombre | GitHub                                          |
| ------ | ----------------------------------------------- |
| Sergio | [SergioGarLue](https://github.com/SergioGarLue) |
| Ian    | [IanBonilla](https://github.com/IanBonilla)     |
| David  | [Davidterp1](https://github.com/Davidterp1)     |

**Demo: `./mvnw spring-boot:run` → localhost:8080 🎮**
