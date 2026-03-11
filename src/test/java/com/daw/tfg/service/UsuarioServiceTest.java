package com.daw.tfg.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.daw.tfg.configuration.SecurityConfig;
import com.daw.tfg.dtos.UsuarioDTO;
import com.daw.tfg.enums.EstadoUsuario;
import com.daw.tfg.enums.RolesUsuarios;
import com.daw.tfg.models.PerfilUsuario;
import com.daw.tfg.models.Usuario;
import com.daw.tfg.repository.PerfilUsuarioRepository;
import com.daw.tfg.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PerfilUsuarioRepository perfilUsuarioRepository;

    @Mock
    private SecurityConfig securityConfig;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private PerfilUsuario perfilUsuario;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        // Crear perfil de usuario
        perfilUsuario = new PerfilUsuario(
            "avatar.png",
            "background.jpg",
            "España",
            "Mi biografia",
            true
        );
        perfilUsuario.setId_usuario_perfil(1L);

        // Crear usuario
        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNombreUsuario("testuser");
        usuario.setContraseñaCifrada("encodedPassword123");
        usuario.setCorreoElectronico("test@example.com");
        usuario.setConexion(EstadoUsuario.DESCONECTADO);
        usuario.setRol(RolesUsuarios.USER);
        usuario.setAmigos(new HashSet<>());
        usuario.setPerfilUsuario(perfilUsuario);

        // Crear DTO
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setUsername("newuser");
        usuarioDTO.setPasswd("Pass123!");
    }

    // ==================== Tests para findAll ====================

    @Test
    void findAll_DeberiaRetornarListaDeUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<Usuario> resultado = usuarioService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("testuser", resultado.get(0).getNombreUsuario());
    }

    @Test
    void findAll_CuandoNoHayUsuarios_DeberiaRetornarListaVacia() {
        when(usuarioRepository.findAll()).thenReturn(List.of());

        List<Usuario> resultado = usuarioService.findAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ==================== Tests para findById ====================

    @Test
    void findById_DeberiaRetornarUsuarioCuandoExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.findById(1L);

        assertNotNull(resultado);
        assertEquals("testuser", resultado.getNombreUsuario());
    }

    @Test
    void findById_DeberiaLanzarExcepcionCuandoNoExiste() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.findById(999L);
        });
    }

    // ==================== Tests para findByNombreUsuario ====================

    @Test
    void findByNombreUsuario_DeberiaRetornarUsuarioCuandoExiste() {
        when(usuarioRepository.findByNombreUsuario("testuser")).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.findByNombreUsuario("testuser");

        assertNotNull(resultado);
        assertEquals("testuser", resultado.getNombreUsuario());
    }

    @Test
    void findByNombreUsuario_DeberiaLanzarExcepcionCuandoNoExiste() {
        when(usuarioRepository.findByNombreUsuario("noexiste")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.findByNombreUsuario("noexiste");
        });
    }

    // ==================== Tests para findByCorreoElectronico ====================

    @Test
    void findByCorreoElectronico_DeberiaRetornarUsuarioCuandoExiste() {
        when(usuarioRepository.findByCorreoElectronico("test@example.com")).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.findByCorreoElectronico("test@example.com");

        assertNotNull(resultado);
        assertEquals("test@example.com", resultado.getCorreoElectronico());
    }

    @Test
    void findByCorreoElectronico_DeberiaLanzarExcepcionCuandoNoExiste() {
        when(usuarioRepository.findByCorreoElectronico("noexiste@email.com")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.findByCorreoElectronico("noexiste@email.com");
        });
    }

    // ==================== Tests para save ====================

    @Test
    void save_DeberiaGuardarUsuarioCorrectamente() {
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.save(usuario);

        assertNotNull(resultado);
        assertEquals("testuser", resultado.getNombreUsuario());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    // ==================== Tests para deleteById ====================

    @Test
    void deleteById_DeberiaEliminarUsuario() {
        doNothing().when(usuarioRepository).deleteById(1L);

        assertDoesNotThrow(() -> usuarioService.deleteById(1L));

        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    // ==================== Tests para authenticate ====================

    @Test
    void authenticate_DeberiaRetornarUsuarioCuandoCredencialesSonValidas() {
        when(securityConfig.passwdEncoder()).thenReturn(passwordEncoder);
        when(usuarioRepository.findByNombreUsuario("testuser")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("password123", "encodedPassword123")).thenReturn(true);

        Optional<Usuario> resultado = usuarioService.authenticate("testuser", "password123");

        assertTrue(resultado.isPresent());
        assertEquals("testuser", resultado.get().getNombreUsuario());
    }

    @Test
    void authenticate_DeberiaRetornarVacioCuandoUsuarioNoExiste() {
        when(usuarioRepository.findByNombreUsuario("noexiste")).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.authenticate("noexiste", "password123");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void authenticate_DeberiaRetornarVacioCuandoPasswordEsIncorrecto() {
        when(securityConfig.passwdEncoder()).thenReturn(passwordEncoder);
        when(usuarioRepository.findByNombreUsuario("testuser")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("wrongpassword", "encodedPassword123")).thenReturn(false);

        Optional<Usuario> resultado = usuarioService.authenticate("testuser", "wrongpassword");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void authenticate_DeberiaRetornarVacioCuandoUsernameEsNulo() {
        Optional<Usuario> resultado = usuarioService.authenticate(null, "password123");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void authenticate_DeberiaRetornarVacioCuandoPasswordEsNulo() {
        Optional<Usuario> resultado = usuarioService.authenticate("testuser", null);

        assertTrue(resultado.isEmpty());
    }

    // ==================== Tests para updateProfile ====================

    @Test
    void updateProfile_DeberiaActualizarNombreUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByNombreUsuario("newusername")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsername("newusername");

        assertDoesNotThrow(() -> usuarioService.updateProfile(1L, dto));

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void updateProfile_DeberiaLanzarExcepcionCuandoNuevoUsernameYaExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByNombreUsuario("existinguser")).thenReturn(true);

        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsername("existinguser");

        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.updateProfile(1L, dto);
        });
    }

    @Test
    void updateProfile_DeberiaLanzarExcepcionCuandoPasswordEsInvalida() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioDTO dto = new UsuarioDTO();
        dto.setPasswd("short"); // Contraseña inválida

        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.updateProfile(1L, dto);
        });
    }

    // ==================== Tests para validaPasswd ====================
    // Estos tests verifican la validación de contraseña a través de registrar()

    @Test
    void registrar_DeberiaLanzarExcepcionCuandoUsuarioYaExiste() {
        when(usuarioRepository.existsByNombreUsuario("testuser")).thenReturn(true);

        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsername("testuser");
        dto.setPasswd("Pass123!");

        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.registrar(dto);
        });
    }

    @Test
    void registrar_DeberiaLanzarExcepcionCuandoPasswordEsInvalida() {
        when(usuarioRepository.existsByNombreUsuario("newuser")).thenReturn(false);

        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsername("newuser");
        dto.setPasswd("short"); // No cumple los requisitos

        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.registrar(dto);
        });
    }

    @Test
    void registrar_DeberiaCrearUsuarioCorrectamente() {
        when(usuarioRepository.existsByNombreUsuario("newuser")).thenReturn(false);
        when(securityConfig.passwdEncoder()).thenReturn(passwordEncoder);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(perfilUsuarioRepository.save(any(PerfilUsuario.class))).thenAnswer(invocation -> {
            PerfilUsuario p = invocation.getArgument(0);
            p.setId_usuario_perfil(1L);
            return p;
        });
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setIdUsuario(1L);
            return u;
        });

        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsername("newuser");
        dto.setPasswd("Pass123!"); // Contraseña válida

        assertDoesNotThrow(() -> usuarioService.registrar(dto));

        verify(perfilUsuarioRepository, times(1)).save(any(PerfilUsuario.class));
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void registrar_DeberiaLanzarExcepcionCuandoPasswordEsNula() {
        when(usuarioRepository.existsByNombreUsuario("newuser")).thenReturn(false);

        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsername("newuser");
        dto.setPasswd(null);

        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.registrar(dto);
        });
    }
}

