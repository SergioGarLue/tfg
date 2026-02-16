-- ============================================
-- Script de datos de prueba para Carrito
-- ============================================

-- 1. Insertar Perfil de Usuario
INSERT INTO perfil_usuario (id_usuario_perfil, biografia, estado, imagen_fondo_perfil, imagen_usuario, pais) 
VALUES (1, 'Usuario de prueba para carrito', true, 'fondo_default.jpg', 'avatar_default.jpg', 'España');

-- 2. Insertar Usuario
INSERT INTO usuario (id_usuario, id_usuario_perfil, contraseña_cifrada, correo_electronico, nombre_usuario, conexion, rol) 
VALUES (1, 1, '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqQzBZN0UfGNEKjN.Kq0T4QAKYw9C', 'test@example.com', 'testuser', 'ACTIVO', 'USER');

-- 3. Insertar Desarrollador
INSERT INTO desarrollador (id_desarrollador, imagen, nombre) 
VALUES (1, 'dev_logo.jpg', 'GameStudio Inc.');

-- 4. Insertar Editor
INSERT INTO editor (id_editor, imagen, nombre) 
VALUES (1, 'editor_logo.jpg', 'SuperGames Publishing');

-- 5. Insertar Géneros
INSERT INTO genero (id_genero, nombre) VALUES (1, 'Acción');
INSERT INTO genero (id_genero, nombre) VALUES (2, 'Aventura');
INSERT INTO genero (id_genero, nombre) VALUES (3, 'RPG');

-- 6. Insertar Juegos
INSERT INTO juego (id_juego, descripcion, fecha_lanzamiento, imagen, precio, requerimientos, tipo, titulo, id_desarrollador, id_editor, id_juego_padre) 
VALUES (1, 'Juego de acción épico con gráficos increíbles', '2024-01-15 00:00:00', 'game1.jpg', 59.99, 'Windows 10, 8GB RAM, GTX 1060', 'BASE', 'Epic Adventure', 1, 1, NULL);

INSERT INTO juego (id_juego, descripcion, fecha_lanzamiento, imagen, precio, requerimientos, tipo, titulo, id_desarrollador, id_editor, id_juego_padre) 
VALUES (2, 'DLC con nuevas misiones y personajes', '2024-03-20 00:00:00', 'dlc1.jpg', 19.99, 'Windows 10, 8GB RAM, GTX 1060', 'DLC', 'Epic Adventure - DLC Pack', 1, 1, 1);

INSERT INTO juego (id_juego, descripcion, fecha_lanzamiento, imagen, precio, requerimientos, tipo, titulo, id_desarrollador, id_editor, id_juego_padre) 
VALUES (3, 'Juego RPG con mundo abierto', '2024-02-10 00:00:00', 'game2.jpg', 49.99, 'Windows 10, 16GB RAM, RTX 2060', 'BASE', 'Fantasy RPG', 1, 1, NULL);

-- 7. Relacionar Juegos con Géneros
INSERT INTO juego_genero (id_juego, id_genero) VALUES (1, 1);
INSERT INTO juego_genero (id_juego, id_genero) VALUES (1, 2);
INSERT INTO juego_genero (id_juego, id_genero) VALUES (2, 1);
INSERT INTO juego_genero (id_juego, id_genero) VALUES (3, 3);

-- 8. Insertar Carrito (sin compra inicialmente)
INSERT INTO carrito (id_carrito, id_compra, id_usuario) 
VALUES (1, NULL, 1);

-- 9. Insertar Juegos en el Carrito
INSERT INTO juegos_carrito (id_carrito, id_juego) VALUES (1, 1);
INSERT INTO juegos_carrito (id_carrito, id_juego) VALUES (1, 3);

-- ============================================
-- Datos de prueba listos!
-- ============================================
-- Ahora puedes probar estos endpoints:
-- 
-- GET  /api/carrito           -> Debería mostrar 1 carrito
-- GET  /api/carrito/1         -> Debería mostrar el carrito con ID 1
-- GET  /api/carrito/usuario/1  -> Debería mostrar el carrito del usuario 1
-- GET  /api/carrito/total/1    -> Debería mostrar 109.98 (59.99 + 49.99)
-- 
-- POST /api/carrito/agregar?usuarioId=1&juegoId=2  -> Añade DLC al carrito
-- DELETE /api/carrito/eliminar/1?usuarioId=1       -> Elimina juego 1 del carrito
-- ============================================
