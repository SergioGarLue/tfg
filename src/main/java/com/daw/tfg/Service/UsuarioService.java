package com.daw.tfg.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.daw.tfg.Configuration.SecurityConfig;
import com.daw.tfg.Dtos.UsuarioDTO;
import com.daw.tfg.Repository.UsuarioRepository;
import com.daw.tfg.models.Usuario;

@Service
public class UsuarioService {
     private final UsuarioRepository usuarioRepository;
    private final SecurityConfig securityConfig;
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

    // este metodo hay que revisarlo
    private boolean validaPasswd(String passwd) {
        return true;
    }

    // Métodos CRUD
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> findByNombreUsuario(String nombre) {
        return usuarioRepository.findByNombreUsuario(nombre);
    }

    public Optional<Usuario> findByCorreoElectronico(String correo) {
        return usuarioRepository.findByCorreoElectronico(correo);
    }

    public Usuario save(Usuario u) {
        return usuarioRepository.save(u);
    }

    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }
}

