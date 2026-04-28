package com.daw.tfg.controllers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daw.tfg.dtos.AsignarRolDesarrolladorDTO;
import com.daw.tfg.enums.RolesUsuarios;
import com.daw.tfg.models.Desarrollador;
import com.daw.tfg.models.Usuario;
import com.daw.tfg.service.DesarrolladorService;
import com.daw.tfg.service.SteamImportService;
import com.daw.tfg.service.UsuarioService;

/**
 * Controlador administrativo para operaciones restringidas solo a admins.
 * Incluye funcionalidades como importación de datos de Steam y gestión de usuarios.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final SteamImportService steamImportService;
    private final UsuarioService usuarioService;
    private final DesarrolladorService desarrolladorService;

    public AdminController(SteamImportService steamImportService, UsuarioService usuarioService, DesarrolladorService desarrolladorService) {
        this.steamImportService = steamImportService;
        this.usuarioService = usuarioService;
        this.desarrolladorService = desarrolladorService;
    }

    /**
     * Endpoint para ejecutar el scraper de Steam e importar datos a la BD.
     * ⚠️ SOLO ADMINS PUEDEN ACCEDER
     * 
     * Este endpoint:
     * 1. Ejecuta el script Python (siis.py) para scrapear Steam
     * 2. Espera a que termine la ejecución
     * 3. Lee el archivo JSON generado (steam_top_1000_sellers.json)
     * 4. Importa los datos a la base de datos
     * 
     * @return ResponseEntity con el resultado de la operación
     */
    @PostMapping("/steam-scraper")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> ejecutarSteamScraper() {
        try {
            System.out.println("🔍 Iniciando scraper de Steam...");
            String resultado = steamImportService.ejecutarSteamScraper();
            
            return ResponseEntity.ok(resultado);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.status(500).body(
                    "❌ La ejecución del script fue interrumpida: " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(500).body(
                    "❌ Error de entrada/salida: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    "❌ Error inesperado: " + e.getMessage());
        }
    }

    /**
     * Busca usuarios por nombre de usuario (búsqueda parcial, case-insensitive).
     * ⚠️ SOLO ADMINS PUEDEN ACCEDER
     * 
     * @param nombreUsuario Nombre o parte del nombre a buscar
     * @return ResponseEntity con la lista de usuarios encontrados
     */
    @GetMapping("/usuarios/buscar")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarUsuariosPorNombre(@RequestParam String nombreUsuario) {
        try {
            List<Usuario> usuarios = usuarioService.findByNombreUsuarioContainingIgnoreCase(nombreUsuario);
            List<UsuarioResponseDTO> dtos = usuarios.stream()
                    .map(usuario -> new UsuarioResponseDTO(
                            usuario.getIdUsuario(),
                            usuario.getNombreUsuario(),
                            usuario.getCorreoElectronico(),
                            usuario.getRol().toString(),
                            usuario.getDesarrollador() != null ? usuario.getDesarrollador().getNombre() : null,
                            usuario.getDesarrollador() != null ? usuario.getDesarrollador().getIdDesarrollador() : null))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Busca un usuario exacto por nombre de usuario.
     * ⚠️ SOLO ADMINS PUEDEN ACCEDER
     * 
     * @param nombreUsuario Nombre del usuario a buscar
     * @return ResponseEntity con el usuario encontrado
     */
    @GetMapping("/usuario/buscar")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> buscarUsuarioPorNombre(@RequestParam String nombreUsuario) {
        try {
            Usuario usuario = usuarioService.findByNombreUsuario(nombreUsuario);
            return ResponseEntity.ok(new UsuarioResponseDTO(
                    usuario.getIdUsuario(),
                    usuario.getNombreUsuario(),
                    usuario.getCorreoElectronico(),
                    usuario.getRol().toString(),
                    usuario.getDesarrollador() != null ? usuario.getDesarrollador().getNombre() : null,
                    usuario.getDesarrollador() != null ? usuario.getDesarrollador().getIdDesarrollador() : null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Error: " + e.getMessage());
        }
    }

    /**
     * Obtiene información de un usuario por su ID.
     * ⚠️ SOLO ADMINS PUEDEN ACCEDER
     * 
     * @param idUsuario ID del usuario
     * @return ResponseEntity con el usuario encontrado
     */
    @GetMapping("/usuario/{idUsuario}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> obtenerUsuarioPorId(@PathVariable Long idUsuario) {
        try {
            Usuario usuario = usuarioService.findById(idUsuario);
            return ResponseEntity.ok(new UsuarioResponseDTO(
                    usuario.getIdUsuario(),
                    usuario.getNombreUsuario(),
                    usuario.getCorreoElectronico(),
                    usuario.getRol().toString(),
                    usuario.getDesarrollador() != null ? usuario.getDesarrollador().getNombre() : null,
                    usuario.getDesarrollador() != null ? usuario.getDesarrollador().getIdDesarrollador() : null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Error: " + e.getMessage());
        }
    }

    /**
     * Lista todos los usuarios del sistema.
     * ⚠️ SOLO ADMINS PUEDEN ACCEDER
     * 
     * @return ResponseEntity con la lista de usuarios
     */
    @GetMapping("/usuarios")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.findAll();
            List<UsuarioResponseDTO> dtos = usuarios.stream()
                    .map(usuario -> new UsuarioResponseDTO(
                            usuario.getIdUsuario(),
                            usuario.getNombreUsuario(),
                            usuario.getCorreoElectronico(),
                            usuario.getRol().toString(),
                            usuario.getDesarrollador() != null ? usuario.getDesarrollador().getNombre() : null,
                            usuario.getDesarrollador() != null ? usuario.getDesarrollador().getIdDesarrollador() : null))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Elimina un usuario por su ID.
     * ⚠️ SOLO ADMINS PUEDEN ACCEDER
     * 
     * @param idUsuario ID del usuario a eliminar
     * @return ResponseEntity con mensaje de confirmación
     */
    @DeleteMapping("/usuario/{idUsuario}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long idUsuario) {
        try {
            // Verificar que el usuario existe antes de eliminarlo
            usuarioService.findById(idUsuario);
            
            // Eliminar el usuario
            usuarioService.deleteById(idUsuario);
            
            return ResponseEntity.ok("✅ Usuario con ID " + idUsuario + " eliminado correctamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Error al eliminar usuario: " + e.getMessage());
        }
    }

    /**
     * Asigna un rol y empresa desarrolladora a un usuario.
     * ⚠️ SOLO ADMINS PUEDEN ACCEDER
     * 
     * @param idUsuario ID del usuario
     * @param dto DTO con rol y opcional idDesarrollador
     * @return ResponseEntity con mensaje de confirmación
     */
    @PutMapping("/usuario/{idUsuario}/rol")
    public ResponseEntity<String> asignarRolDesarrollador(
            @PathVariable Long idUsuario,
            @RequestBody AsignarRolDesarrolladorDTO dto) {
        try {
            Usuario usuario = usuarioService.findById(idUsuario);
            usuario.setRol(RolesUsuarios.valueOf(dto.getRol()));

            if (dto.getIdDesarrollador() != null) {
                Desarrollador desarrollador = desarrolladorService.findById(dto.getIdDesarrollador())
                        .orElseThrow(() -> new IllegalArgumentException("Desarrollador no encontrado"));
                usuario.setDesarrollador(desarrollador);
            } else {
                usuario.setDesarrollador(null);
            }

            usuarioService.save(usuario);
            return ResponseEntity.ok("✅ Rol actualizado correctamente para " + usuario.getNombreUsuario());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Error: " + e.getMessage());
        }
    }

    /**
     * Lista todos los desarrolladores (empresas) disponibles.
     */
    @GetMapping("/desarrolladores")
    public ResponseEntity<List<Desarrollador>> listarDesarrolladores() {
        try {
            List<Desarrollador> desarrolladores = desarrolladorService.findAll();
            return ResponseEntity.ok(desarrolladores);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * DTO simple para devolver información del usuario en las respuestas
     */
    public static class UsuarioResponseDTO {
        public Long idUsuario;
        public String nombreUsuario;
        public String correoElectronico;
        public String rol;
        public String nombreDesarrollador;
        public Long idDesarrollador;

        public UsuarioResponseDTO(Long idUsuario, String nombreUsuario, String correoElectronico, String rol, String nombreDesarrollador, Long idDesarrollador) {
            this.idUsuario = idUsuario;
            this.nombreUsuario = nombreUsuario;
            this.correoElectronico = correoElectronico;
            this.rol = rol;
            this.nombreDesarrollador = nombreDesarrollador;
            this.idDesarrollador = idDesarrollador;
        }
    }
}
