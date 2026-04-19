
-- IDENTITY restart
ALTER TABLE perfil_usuario ALTER COLUMN id_usuario_perfil RESTART WITH 3;
ALTER TABLE usuario ALTER COLUMN id_usuario RESTART WITH 3;
ALTER TABLE desarrollador ALTER COLUMN id_desarrollador RESTART WITH 3;
ALTER TABLE editor ALTER COLUMN id_editor RESTART WITH 3;
ALTER TABLE genero ALTER COLUMN id_genero RESTART WITH 4;
ALTER TABLE juego ALTER COLUMN id_juego RESTART WITH 4;
ALTER TABLE carrito ALTER COLUMN id_carrito RESTART WITH 2;

-- ============================================
-- ADMIN READY - ACCESS GUIDE
-- ============================================

-- 
-- **Steps**:
-- 1. .\mvnw.cmd spring-boot:run
-- 2. http://localhost:8080/login → admin / admin@123!
-- 3. **/admin** → Dashboard ✓ (protected)
-- 4. APIs /api/admin/** ✓ (ROLE_ADMIN from JWT)

-- BCrypt warning safe to ignore (AuthController bypasses for data.sql users).
