# Xyron - Documento de Desarrollo y Mantenimiento

**Autores:** Sergio García, Ian Bonilla, David González
**Fecha:** Octubre 2024

## Índice

1. [Arquitectura y Estructura del Sistema](#arquitectura-y-estructura-del-sistema)
2. [Manual de Instalación y Despliegue](#manual-de-instalación-y-despliegue)
3. [Guía para Añadir Nuevas Funcionalidades](#guía-para-añadir-nuevas-funcionalidades)
4. [Labores de Mantenimiento](#labores-de-mantenimiento)

## Arquitectura y Estructura del Sistema

**Interfaz Web (Frontend):** Templates (\*.html), static CSS/JS/JSON. Vanilla JS fetch API, localStorage. Ejemplo sidebar-loader.js:

```html
<!-- sidebar.html fragment -->
<div th:replace="~{sidebar.html}"></div>
```

JS example:

```js
fetch("/api/tienda")
  .then((r) => r.json())
  .then(updateGrid());
```

**Entorno Servidor (Backend):** Spring Boot MVC/REST. Layers:

```java
@Entity
class NewModel { ... }

@Repository
interface NewRepo extends JpaRepository<NewModel, Long> { }

@Service
class NewService {
  @Autowired NewRepo repo;
  List<NewModel> findAll() { return repo.findAll(); }
}

@RestController
@RequestMapping("/api/new")
class NewController {
  @Autowired NewService service;
  @GetMapping List<NewModel> getAll() { return service.findAll(); }
}
```

**Base de Datos:** H2 dev, JPA relations m2m @JoinTable. Schema diagram concept:

```text
Usuario 1---1 Carrito *---* Juego
         |
         1---* Compra
```

## Manual de Instalación y Despliegue

**Requisitos:** Java 21, Maven wrapper (`./mvnw`), Stripe keys.

**Despliegue local (visual steps):**

1. Clone:

   ```bash
   git clone https://github.com/user/TFG-.git
   cd TFG-
   ```

2. Env:

   ```bash
   cp .env.example .env
   # Edit .env: STRIPE_SECRET_KEY=sk_test_...
   ```

3. Build/Run:

   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

4. Test: curl <http://localhost:8080/api/tienda>

**Producción:**

```bash
./mvnw package -DskipTests
java -jar target/tfg-0.0.1-SNAPSHOT.jar
```

Env vars Stripe.

## Guía para Añadir Nuevas Funcionalidades (Ejemplo: 'DLC')

1. **DB:**

   `@Entity public class DLC { @ManyToOne Juego juego; }` Update data.sql.

2. **Backend:**

   ```java
   // DLCRepo, DLCService, DLCController @PostMapping("/api/dlc/add")
   @PostMapping("/api/dlc/add")
   public ResponseEntity<DLC> addDLC(@RequestBody DLC dto) { ... }
   ```

3. **Frontend:** Add template-dlc.html, JS:

   ```js
   fetch('/api/dlc/add', {method:'POST', body:JSON...}).then(showDLC());
   ```

4. Test: Postman /api/dlc/add → browser refresh.

## Labores de Mantenimiento

**DB Control:**

```bash
# H2 export
# MySQL: mysqldump -u sa tfgdb > backup.sql
```

**Dependencias:**

```bash
./mvnw versions:display-dependency-updates
./mvnw dependency:tree | grep Stripe
# Update pom.xml, ./mvnw clean install
```

**Monitoring:**

- Logs: Stripe init @PostConstruct.
- Health: /actuator/health (Spring Actuator).
- Clean: git clean -fd target/
