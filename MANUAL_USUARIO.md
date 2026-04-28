# Xyron - Manual de Usuario

**Autores:** Sergio García, Ian Bonilla, David González
**Fecha:** Abril 2026

## Índice

1. [Introducción al Sistema](#introducción-al-sistema)
2. [Acceso al Sistema y Perfiles](#acceso-al-sistema-y-perfiles)
3. [Guía de Uso y Funcionalidades](#guía-de-uso-y-funcionalidades)
4. [Glosario de Términos](#glosario-de-términos)

## Introducción al Sistema

**Propósito general:** Xyron es una plataforma online para comprar videojuegos, gestionar tu biblioteca personal, crear listas de deseados y conectar con amigos jugadores. Resuelve el problema de encontrar, comprar y organizar juegos en un solo lugar amigable, como una tienda digital con comunidad.

**A quién va dirigido:** Jugadores casuales y apasionados de videojuegos que quieren comprar títulos seguros con tarjeta, ver su colección y compartir con amigos.

## Acceso al Sistema y Perfiles

**Cómo iniciar sesión:**

1. Ve a <http://localhost:8080> (o URL de producción).
2. En la pantalla inicial, haz clic en **\"Inicia sesión\"** o **\"Regístrate\"** si eres nuevo.
3. Rellena usuario/contraseña (o regístrate con datos nuevos).
4. Sidebar izquierda muestra tu perfil; admin ve panel especial.

**Diferencias entre perfiles:**

- **Usuario normal:** Acceso a tienda, carrito, perfil, amigos, colección. Sidebar con opciones personales.
- **Administrador:** Acceso extra a `/administrador` para gestionar usuarios/juegos. Sidebar o menú especial (ver ADMIN_PANEL_GUIDE.md).

## Guía de Uso y Funcionalidades

**Funcionalidad 1: Comprar en la Tienda**
Pasos:

1. Ve a **Tienda** (sidebar o menú).
2. Busca juegos por nombre, género, precio, dev/editor.
3. Clic **Añadir al carrito**.
4. Ve **Carrito**, revisa, **Checkout** → Stripe paga con tarjeta.
   Resultado esperado: Compra exitosa, juego añadido a Colección, notificación. Recibo en perfil.

**Funcionalidad 2: Gestionar Colección/Biblioteca**
Pasos:

1. Clic **Colección** en sidebar.
2. Ve juegos comprados.
3. Clic juego para detalles.
   Resultado esperado: Lista personal de juegos, progreso visible.

**Funcionalidad 3: Lista de Deseados**
Pasos:

1. En tienda/juego, **Añadir a Deseados**.
2. Ve **Deseados** en sidebar.
3. Elimina si quieres.
   Resultado esperado: Lista futura compras, recordatorio.

**Funcionalidad 4: Perfil y Amigos**
Pasos:

1. **Perfil** → Configura datos, métodos pago.
2. **Amigos** → Busca añade amigos, ve conectados.
   Resultado esperado: Perfil actualizado, lista amigos, chats/notifs básicas.

**Funcionalidad 5: Admin (solo admins)**
Pasos:

1. Ve `/administrador`.
2. Gestiona usuarios/juegos.
   Resultado esperado: Sistema actualizado.

## Glosario de Términos

**Carrito:** Cesta temporal para juegos antes de pagar.  
**Stripe:** Sistema pago seguro con tarjeta (como PayPal).  
**Colección:** Tu biblioteca de juegos comprados.  
**JWT:** Código seguro para mantener sesión iniciada.  
**H2:** Base datos temporal (no te preocupes, datos persisten en sesión).
