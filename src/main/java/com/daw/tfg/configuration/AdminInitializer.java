package com.daw.tfg.configuration;


import com.daw.tfg.dtos.UsuarioDTO;
import com.daw.tfg.enums.RolesUsuarios;
import com.daw.tfg.models.Desarrollador;
import com.daw.tfg.models.Usuario;
import com.daw.tfg.service.DesarrolladorService;
import com.daw.tfg.service.UsuarioService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AdminInitializer {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DesarrolladorService desarrolladorService;

    @PostConstruct
    @Transactional
    public void initAdmin() {
        crearUsuarioSiNoExiste("admin", "admin@platform.com", "Admin@123!", RolesUsuarios.ADMIN);
        crearUsuarioSiNoExiste("testuser", "test@platform.com", "Test@123!", RolesUsuarios.USER);
        crearUsuarioSiNoExiste("testuser1", "test1@platform.com", "Test@123!", RolesUsuarios.USER);
        crearDeveloperSiNoExiste("developer1", "dev1@platform.com", "Dev@123!", "Valve");
        crearDeveloperSiNoExiste("developer2", "dev2@platform.com", "Dev@123!", "Ubisoft");
    }

    private void crearUsuarioSiNoExiste(String username, String email, String password, RolesUsuarios rol) {
        if (usuarioService.findAll().stream().noneMatch(u -> u.getNombreUsuario().equals(username))) {
            try {
                UsuarioDTO dto = new UsuarioDTO();
                dto.setUsername(username);
                dto.setCorreoElectronico(email);
                dto.setPasswd(password);

                usuarioService.registrar(dto);
                Usuario usuario = usuarioService.findByNombreUsuario(username);
                usuario.setRol(rol);
                usuarioService.save(usuario);
                
                System.out.println("Usuario creado: " + username + " / " + password + " (rol: " + rol + ")");
            } catch (Exception e) {
                System.err.println("Error creando " + username + ": " + e.getMessage());
            }
        } else {
            System.out.println("Usuario '" + username + "' ya existe.");
        }
    }

    private void crearDeveloperSiNoExiste(String username, String email, String password, String nombreDesarrollador) {
        // 1. Crear el usuario como USER si no existe (reutiliza registrar() que hashea, valida y crea perfil)
        if (usuarioService.findAll().stream().noneMatch(u -> u.getNombreUsuario().equals(username))) {
            try {
                UsuarioDTO dto = new UsuarioDTO();
                dto.setUsername(username);
                dto.setCorreoElectronico(email);
                dto.setPasswd(password);

                usuarioService.registrar(dto);
                System.out.println("Usuario creado: " + username + " / " + password + " (rol: USER)");
            } catch (Exception e) {
                System.err.println("Error creando " + username + ": " + e.getMessage());
                return;
            }
        } else {
            System.out.println("Usuario '" + username + "' ya existe.");
        }

        // 2. Actualizar a DEVELOPER y vincular desarrolladora
        try {
            Usuario usuario = usuarioService.findByNombreUsuario(username);
            if (usuario.getRol() != RolesUsuarios.DEVELOPER) {
                usuario.setRol(RolesUsuarios.DEVELOPER);
                usuario.setDesarrollador(obtenerOCrearDesarrollador(nombreDesarrollador));
                
                // Actualizar biografía del perfil para reflejar que es desarrollador
                if (usuario.getPerfilUsuario() != null) {
                    usuario.getPerfilUsuario().setBiografia("Desarrollador de " + nombreDesarrollador);
                }
                
                usuarioService.save(usuario);
                System.out.println("Usuario " + username + " actualizado a DEVELOPER vinculado a " + nombreDesarrollador);
            }
        } catch (Exception e) {
            System.err.println("Error actualizando developer " + username + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Desarrollador obtenerOCrearDesarrollador(String nombre) {
        return desarrolladorService.findByNombre(nombre)
                .orElseGet(() -> {
                    Desarrollador nuevo = new Desarrollador();
                    nuevo.setNombre(nombre);
                    return desarrolladorService.save(nuevo);
                });
    }
}
