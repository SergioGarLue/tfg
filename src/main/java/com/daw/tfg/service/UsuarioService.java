package com.daw.tfg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daw.tfg.dtos.Perfil_UsuarioDTO;
import com.daw.tfg.dtos.UsuarioDTO;
import com.daw.tfg.configuration.SecurityConfig;
import com.daw.tfg.repository.PerfilUsuarioRepository;
import com.daw.tfg.repository.UsuarioRepository;
import com.daw.tfg.enums.EstadoUsuario;
import com.daw.tfg.enums.RolesUsuarios;
import com.daw.tfg.models.PerfilUsuario;
import com.daw.tfg.models.Carrito;
import com.daw.tfg.models.Usuario;
import com.daw.tfg.repository.CarritoRepository;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
private final PerfilUsuarioRepository perfilUsuarioRepository;
    private final CarritoRepository carritoRepository;
    private final SecurityConfig securityConfig;
    
public UsuarioService(UsuarioRepository usuarioRepository, PerfilUsuarioRepository perfilUsuarioRepository, CarritoRepository carritoRepository, SecurityConfig securityConfig) {
        this.usuarioRepository = usuarioRepository;
        this.perfilUsuarioRepository = perfilUsuarioRepository;
        this.carritoRepository = carritoRepository;
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
            "Bienvenido " + userDto.getUsername() + "!",  // biografia única
            true  // estado (activo)
        );
        
        // Guardar primero el perfil para obtener su ID
        perfilNuevo = perfilUsuarioRepository.save(perfilNuevo);

        Usuario userNuevo = new Usuario();
        userNuevo.setNombreUsuario(userDto.getUsername());
        userNuevo.setCorreoElectronico(userDto.getCorreoElectronico());
        userNuevo.setContraseñaCifrada(securityConfig.passwdEncoder().encode(userDto.getPasswd()));
        userNuevo.setConexion(EstadoUsuario.DESCONECTADO);
        userNuevo.setRol(RolesUsuarios.USER);
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

@Transactional
    public void deleteById(Long id) {
        Usuario usuario = findById(id);
        
        // Delete associated Carrito first to avoid FK constraint
        Optional<Carrito> carritoOpt = carritoRepository.findByUsuario(usuario);
        carritoOpt.ifPresent(carritoRepository::delete);
        
        // Now delete Usuario (cascades PerfilUsuario)
        usuarioRepository.delete(usuario);
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

    /**
     * Obtiene el perfil del usuario por ID de usuario.
     */
    @Transactional(readOnly = true)
    public PerfilUsuario getPerfilByUsuarioId(Long userId) {
        Usuario usuario = findById(userId);
        return usuario.getPerfilUsuario();
    }

    /**
     * Actualiza el perfil del usuario usando DTO.
     */
    public void updatePerfilUsuario(Long userId, Perfil_UsuarioDTO dto) {
        Usuario usuario = findById(userId);
        PerfilUsuario perfil = usuario.getPerfilUsuario();
        
        perfil.setImagenUsuario(dto.getImagenUsuario());
        perfil.setImagenFondoPerfil(dto.getImagenFondoPerfil());
        perfil.setPais(dto.getPais());
        perfil.setBiografia(dto.getBiografia());
        perfil.setEstado(dto.getEstado());
        
        perfilUsuarioRepository.save(perfil);
    }
}

