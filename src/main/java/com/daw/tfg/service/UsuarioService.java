package com.daw.tfg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daw.tfg.dtos.UsuarioDTO;
import com.daw.tfg.configuration.SecurityConfig;
import com.daw.tfg.repository.PerfilUsuarioRepository;
import com.daw.tfg.repository.UsuarioRepository;
import com.daw.tfg.models.PerfilUsuario;
import com.daw.tfg.models.Usuario;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PerfilUsuarioRepository perfilUsuarioRepository;
    private final SecurityConfig securityConfig;
    
    public UsuarioService(UsuarioRepository usuarioRepository, PerfilUsuarioRepository perfilUsuarioRepository, SecurityConfig securityConfig) {
        this.usuarioRepository = usuarioRepository;
        this.perfilUsuarioRepository = perfilUsuarioRepository;
        this.securityConfig = securityConfig;
    }

    @Transactional
    public void registrar(UsuarioDTO userDto) {

        if (usuarioRepository.existsByNombreUsuario(userDto.getUsername())) {
            throw new IllegalArgumentException("El usuario ya exsite");
        }
        if (!validaPasswd(userDto.getPasswd())) {
            throw new IllegalArgumentException("Contraseña invalida/incorrecta");
        }

        // Crear el perfil de usuario automáticamente con valores por defecto
        PerfilUsuario perfilNuevo = new PerfilUsuario(
            "default_avatar.png",  // imagenUsuario
            "default_background.jpg",  // imagenFondoPerfil
            "España",  // pais
            "Bienvenido a tu nuevo perfil!",  // biografia
            true  // estado (activo)
        );
        
        // Guardar primero el perfil para obtener su ID
        perfilNuevo = perfilUsuarioRepository.save(perfilNuevo);

        Usuario userNuevo = new Usuario();
        userNuevo.setNombreUsuario(userDto.getUsername());
        userNuevo.setContraseñaCifrada(securityConfig.passwdEncoder().encode(userDto.getPasswd()));
        userNuevo.setPerfilUsuario(perfilNuevo);

        usuarioRepository.save(userNuevo);
    }

    // Pattern: al menos 8 caracteres, 1 número, 1 minúscula, 1 mayúscula, 1 carácter especial
    private static final String PASSWORD_PATTERN = 
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$";

    private boolean validaPasswd(String passwd) {
        if (passwd == null) {
            return false;
        }
        return passwd.matches(PASSWORD_PATTERN);
    }

    // Métodos CRUD
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario findById(Long id) {
        Optional<Usuario> user = usuarioRepository.findById(id);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        return user.get();
    }

    public Usuario findByNombreUsuario(String nombre) {
        Optional<Usuario> user = usuarioRepository.findByNombreUsuario(nombre);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        return user.get();
        
    }

    public Usuario findByCorreoElectronico(String correo) {
        Optional<Usuario> user = usuarioRepository.findByCorreoElectronico(correo);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        return user.get();
        
    }

    public Usuario save(Usuario u) {
        return usuarioRepository.save(u);
    }

    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Optional<Usuario> authenticate(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return Optional.empty();
        }
        
        return usuarioRepository.findByNombreUsuario(username)
            .filter(user -> securityConfig.passwdEncoder().matches(password, user.getContraseñaCifrada()));
    }

    /**
     * Updates user profile with validations.
     */
    public void updateProfile(Long userId, UsuarioDTO userDto) {
        Usuario usuario = findById(userId);
        
        if (userDto.getUsername() != null && !userDto.getUsername().equals(usuario.getNombreUsuario())) {
            if (usuarioRepository.existsByNombreUsuario(userDto.getUsername())) {
                throw new IllegalArgumentException("Nombre de usuario ya existe");
            }
            usuario.setNombreUsuario(userDto.getUsername());
        }

        if (userDto.getPasswd() != null) {
            if (!validaPasswd(userDto.getPasswd())) {
                throw new IllegalArgumentException("Contraseña inválida");
            }
            usuario.setContraseñaCifrada(securityConfig.passwdEncoder().encode(userDto.getPasswd()));
        }

        save(usuario);
    }
}

