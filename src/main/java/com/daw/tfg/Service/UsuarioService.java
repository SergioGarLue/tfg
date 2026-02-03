package com.daw.tfg.Service;

import com.daw.tfg.Configuration.SecurityConfig;
import com.daw.tfg.Dtos.UsuarioDTO;
import com.daw.tfg.Repository.UsuarioRepository;
import com.daw.tfg.models.Usuario;

public class UsuarioService {
     private UsuarioRepository usuarioRepository;
    private SecurityConfig securityConfig;
    public UsuarioService(UsuarioRepository usuarioRepository, SecurityConfig securityConfig) {
        this.usuarioRepository = usuarioRepository;
        this.securityConfig = securityConfig;
    }

    public void registrar(UsuarioDTO userDto) {

        if (usuarioRepository.existsByNombreUsuario(userDto.getUsername())) {
            throw new IllegalArgumentException("El usuario ya exsite");
        }
        if (!validaPasswd(userDto.getPasswd())) {
            throw new IllegalArgumentException("Contraseña invalida/incorrecta");
        }

        Usuario userNuevo = new Usuario();
        userNuevo.setNombreUsuario(userDto.getUsername());
        userNuevo.setContraseñaCifrada(securityConfig.passwdEncoder().encode(userDto.getPasswd()));

        usuarioRepository.save(userNuevo);
    }

    private boolean validaPasswd(String passwd) {
        return true;
    }

}
