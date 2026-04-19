package com.daw.tfg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daw.tfg.dtos.Perfil_UsuarioDTO;
import com.daw.tfg.dtos.UsuarioDTO;

import com.daw.tfg.repository.PerfilUsuarioRepository;
import com.daw.tfg.repository.UsuarioRepository;
import com.daw.tfg.enums.EstadoUsuario;
import com.daw.tfg.enums.RolesUsuarios;
import com.daw.tfg.mappers.DtoMapper;
import com.daw.tfg.models.PerfilUsuario;
import com.daw.tfg.models.Carrito;
import com.daw.tfg.models.Usuario;
import com.daw.tfg.repository.CarritoRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
private final PerfilUsuarioRepository perfilUsuarioRepository;
    private final CarritoRepository carritoRepository;
private final PasswordEncoder passwordEncoder;
    

    public UsuarioService(UsuarioRepository usuarioRepository, PerfilUsuarioRepository perfilUsuarioRepository, CarritoRepository carritoRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.perfilUsuarioRepository = perfilUsuarioRepository;
        this.carritoRepository = carritoRepository;
        this.passwordEncoder = passwordEncoder;
        
    }

@Transactional
    public void registrar(UsuarioDTO userDto) {

        if (usuarioRepository.existsByNombreUsuario(userDto.getUsername())) {
throw new IllegalArgumentException("El usuario ya existe");
        }
        if (!validaPasswd(userDto.getPasswd())) {
            throw new IllegalArgumentException("Contraseña invalida/incorrecta");
        }

        // Create default profile
        PerfilUsuario perfilNuevo = new PerfilUsuario(
            "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/Default_pfp.svg/500px-Default_pfp.svg.png",
            "https://images.unsplash.com/photo-1462331940025-496dfbfc7564?w=500&fit=crop&crop=center",
            "España",
            "Bienvenido " + userDto.getUsername() + "!",
            true
        );

        Usuario userNuevo = DtoMapper.fromUsuarioDTO(userDto);
        userNuevo.setContraseñaCifrada(passwordEncoder.encode(userDto.getPasswd())); // override
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

    @Transactional(readOnly = true)
    public Usuario findByNombreUsuario(String nombre) {
        Optional<Usuario> user = usuarioRepository.findByNombreUsuario(nombre);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        Usuario usuario = user.get();
        // Inicializar el perfil para evitar proxy detached al acceder después de cerrar la sesión
        if (usuario.getPerfilUsuario() != null) {
            usuario.getPerfilUsuario().getImagenUsuario();
        }
        return usuario;
        
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
            .filter(user -> passwordEncoder.matches(password, user.getContraseñaCifrada()));
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
        }

        Usuario updated = DtoMapper.fromUsuarioDTO(userDto);
        usuario.setNombreUsuario(updated.getNombreUsuario());
        usuario.setCorreoElectronico(updated.getCorreoElectronico());

        if (userDto.getPasswd() != null) {
            if (!validaPasswd(userDto.getPasswd())) {
                throw new IllegalArgumentException("Contraseña inválida");
            }
            usuario.setContraseñaCifrada(passwordEncoder.encode(userDto.getPasswd()));
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
        
        PerfilUsuario updatedPerfil = DtoMapper.fromPerfilUsuarioDTO(dto);
        perfil.setImagenUsuario(updatedPerfil.getImagenUsuario());
        perfil.setImagenFondoPerfil(updatedPerfil.getImagenFondoPerfil());
        perfil.setPais(updatedPerfil.getPais());
        perfil.setBiografia(updatedPerfil.getBiografia());
        perfil.setEstado(updatedPerfil.getEstado());
        
        perfilUsuarioRepository.save(perfil);
    }
}

