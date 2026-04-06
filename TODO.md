# TFG HTML Static Paths and Navigation Fix

## Approved Plan Implementation

**Status:** Approved by user

### Information Gathered (Summary):
- 10 Thymeleaf templates with "../static" CSS paths in 4 files
- All templates use hardcoded `href="filename.html"` navigation
- Controllers match routes: /tienda, /coleccion, /perfil, etc.
- login/registro already use proper th: syntax for forms

### Detailed Steps:

1. **✅ Step 1: Create this TODO.md**

2. **✅ Step 2: Fix static CSS paths** (../static/ → /static/)
   - login.html (2 edits) ✅
   - registro.html (2 edits) ✅
   - tienda.html (3 edits) ✅
   - perfil.html (3 edits) ✅

3. **⏳ Step 3: Convert navigation links** (href="xxx.html" → th:href="@{/xxx}")
   - All 10 templates (logo, sidebar nav, carrito badge, etc.)
   - Specific mappings:
     * index.html → {@/}
     * tienda.html → {@{/tienda}}
     * coleccion.html → {@{/coleccion}}
     * perfil.html → {@{/perfil}}
     * configuracion.html → {@{/configuracion}}
     * carrito.html → {@{/carrito}}
     * amigos.html → {@{/amigos}}
     * juego.html → {@{/juego}}
     * desarrollador.html → {@{/desarrollador}}

4. **Step 4: Update TODO.md** (mark progress)

5. **Step 5: Test** 
   - `mvn clean spring-boot:run`
   - Verify CSS loads, all navigation routes work

6. **Step 6: Complete task**

**Next Action:** Step 4 - Fix formatting artifacts and complete remaining links
**Progress:** Started nav updates on index.html (logo, tienda updated, others pending)


