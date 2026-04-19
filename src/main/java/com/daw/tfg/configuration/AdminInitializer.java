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
        String adminUsername = "admin";
        if (usuarioService.findAll().stream().noneMatch(u -> u.getNombreUsuario().equals(adminUsername))) {
            try {
                UsuarioDTO adminDto = new UsuarioDTO();
                adminDto.setUsername(adminUsername);
                adminDto.setCorreoElectronico("admin@platform.com");
                adminDto.setPasswd("Admin@123!");

                usuarioService.registrar(adminDto);
                // Force rol ADMIN (service defaults USER, override via save)
                Usuario admin = usuarioService.findByNombreUsuario(adminUsername);
                admin.setRol(RolesUsuarios.ADMIN);
                usuarioService.save(admin);
                
                System.out.println("✅ ADMIN creado programáticamente: " + adminUsername + " / admin@123!");
            } catch (Exception e) {
                System.err.println("❌ Error creando admin: " + e.getMessage());
            }
        } else {
            System.out.println("ℹ️ Admin '" + adminUsername + "' ya existe.");
        }
    }
}

// -- **Credentials** (plain PW - no BCrypt warning):
// -- Username: admin | Password: admin@123!
