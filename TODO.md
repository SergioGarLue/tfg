# Tienda Dynamic Games (Fetch JSON + Pagination 12/page)

## Steps:
- [x] 1. Create TODO.md (current)
- [x] 2. Edit src/main/resources/templates/tienda.html:
      - Remove hardcoded .seccion-categoria + .fila-tarjetas
      - Add #games-container.games-grid
      - Add #pagination
      - Add <script> for fetch, paginate 12/page, render cards, URL params (?page=)
- [ ] 3. Test implementation:
      - mvn spring-boot:run
      - Visit /tienda → see 12 real Steam games
      - Pagination works (next/prev/?page=2)
      - Cards show img/name/tags/price/discount
- [ ] 4. attempt_completion

