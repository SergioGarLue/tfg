package com.daw.tfg.configuration;

import com.daw.tfg.dtos.UsuarioDTO;
import com.daw.tfg.enums.RolesUsuarios;
import com.daw.tfg.models.Usuario;
import com.daw.tfg.service.UsuarioService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AdminInitializer {

    @Autowired
    private UsuarioService usuarioService;

    @PostConstruct
    @Transactional
    public void initAdmin() {
        // Crear admin
        crearUsuarioSiNoExiste("admin", "admin@platform.com", "Admin@123!", RolesUsuarios.ADMIN);
        // Crear usuario de prueba para testear amistades
        crearUsuarioSiNoExiste("testuser", "test@platform.com", "Test@123!", RolesUsuarios.USER);
        crearUsuarioSiNoExiste("testuser1", "test1@platform.com", "Test@123!", RolesUsuarios.USER);
    }

    private void crearUsuarioSiNoExiste(String username, String email, String password, RolesUsuarios rol) {
        if (usuarioService.findAll().stream().noneMatch(u -> u.getNombreUsuario().equals(username))) {
            try {
                UsuarioDTO dto = new UsuarioDTO();
                dto.setUsername(username);
                dto.setCorreoElectronico(email);
                dto.setPasswd(password);

                usuarioService.registrar(dto);
                // Forzar rol correcto
                Usuario usuario = usuarioService.findByNombreUsuario(username);
                usuario.setRol(rol);
                usuarioService.save(usuario);
                
                System.out.println("✅ Usuario creado: " + username + " / " + password + " (rol: " + rol + ")");
            } catch (Exception e) {
                System.err.println("❌ Error creando " + username + ": " + e.getMessage());
            }
        } else {
            System.out.println("ℹ️ Usuario '" + username + "' ya existe.");
        }
    }
}
