# TFG JuegoDTO Implementation Plan

## Status: ✅ In Progress

### 1. [✅ COMPLETED] Create/Update JuegoDTO.java
   - Match Steam JSON structure exactly
   - Nested DTOs: PriceDTO, PlatformsDTO  
   - Lombok annotations matching project style

### 2. [⬜ PENDING] Test JSON Deserialization
   - Add test in JuegoService to parse steam_top_1000_sellers.json
   - Verify ObjectMapper.readValue() works with JuegoDTO

### 3. [⬜ PENDING] Map DTO to Entity
   - Create mapper method: JuegoDTO → Juego entity
   - Handle developer/editor lookup by name (create if missing)

### 4. [⬜ PENDING] Import Steam Data
   - Service method to read JSON → List<JuegoDTO> → List<Juego> → saveAll()
   - Handle duplicates by appid

### 5. [⬜ PENDING] Validation
   - Test full pipeline: JSON → DTO → Entity → DB
   - `mvn clean compile test`

**Next Step**: Test deserialization after DTO creation.

