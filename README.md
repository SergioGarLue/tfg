# 🎮 Xyron

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)

**_Xyron_** es una plataforma web diseñada para la **compra de videojuegos online**, **gestión de colecciones personales** y **conexión social entre jugadores** 🕹️🌐.  
Este proyecto busca unificar la experiencia de compra con la de una **comunidad activa y social**.

---

## ⚙️ Instalación

> 🚧 Sección en desarrollo (configuración de entorno local y base de datos)

---
## 🏗️ Arquitectura del Proyecto

### Backend
```
├── Configuration    # Clases de configuración general y seguridad
├── controllers      # Controladores REST / MVC para endpoints
├── Dtos             # Objetos de transferencia de datos (DTOs)
├── enums            # Enumeraciones para tipos y estados
├── repository       # Interfaces para acceso a base de datos (JPA)
├── service          # Lógica de negocio y servicios
└── models           # Entidades / Modelos de base de datos
```

### Frontend
```
├── static           # Archivos estáticos: CSS, JS, imágenes
└── templates        # Plantillas Thymeleaf HTML para renderizado del front-end
```
## 👨‍💻 Desarrolladores

| Foto                                                                              | Nombre     | GitHub                                          | Entorno   |
| :-------------------------------------------------------------------------------- | :--------- | :---------------------------------------------- | :-------- |
| <img src="https://avatars.githubusercontent.com/u/185503875?s=60&v=4" width="50"> | **Sergio** | [SergioGarLue](https://github.com/SergioGarLue) | Full Stack |
| <img src="https://avatars.githubusercontent.com/u/185504164?s=60&v=4" width="50"> | **Ian**    | [IanBonilla](https://github.com/IanBonilla)     | Full Stack |
| <img src="https://avatars.githubusercontent.com/u/112289352?s=60&v=4" width="50"> | **David**  | [Davidterp1](https://github.com/Davidterp1)     | Full Stack |

---

## 🚀 Funcionalidades

- 📚 **Colección:** Biblioteca personal donde cada usuario puede ver sus juegos.
- 👥 **Amistades:** Lista de amigos y visualización de usuarios conectados.
- 🔔 **Notificaciones:** Avisos de compras realizadas y conexiones de amigos.
- ⭐ **Reseñas:** Valoraciones y puntuaciones de los juegos por los usuarios.
- 🛒 **Carrito:** Almacena juegos antes de realizar la compra.
- 💳 **Método de pago:** Múltiples opciones de pago:
  - PayPal
  - Stripe
- 💖 **Wishlist:** Lista de deseados para guardar juegos favoritos.
- 🏢 **Desarrollador / Editor:** Página dedicada a editores y desarrolladores con sus juegos y DLCs.

---

## 🛠️ Tecnologías y Dependencias

### 💻 Tecnologías

| Tecnología     | Framework   | Entorno  |
| :------------- | :---------- | :------- |
| **Java**       | Spring Boot | Backend  |
| **HTML 5**     | Vanilla     | Frontend |
| **CSS3**       | FontAwesome | Frontend |
| **JavaScript** | Vanilla     | Frontend |

---

### 📦 Dependencias

| Dependencia            | Función Principal                                                             |
| :--------------------- | :---------------------------------------------------------------------------- |
| **Spring Web MVC**     | Creación de API REST y aplicaciones web.                                      |
| **Spring Data JPA**    | Gestión de base de datos y persistencia.                                      |
| **Spring Security**    | Autenticación y control de permisos.                                          |
| **JWT (JJWT)**         | Autenticación stateless mediante JSON Web Tokens (login y control de acceso). |
| **MySQL Driver**       | Conector para base de datos MySQL.                                            |
| **Thymeleaf**          | Motor de plantillas HTML para el front-end.                                   |
| **Thymeleaf Security** | Integración de roles y seguridad en HTML.                                     |
| **Lombok**             | Reducción de código repetitivo.                                               |
| **Validation**         | Validación de formularios y datos de entrada.                                 |
| **DevTools**           | Recarga automática en desarrollo.                                             |
| **Spring Test**        | Pruebas unitarias y de integración.                                           |
