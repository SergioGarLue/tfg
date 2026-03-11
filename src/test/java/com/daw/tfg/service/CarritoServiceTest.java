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

import com.daw.tfg.enums.EstadoCompra;
import com.daw.tfg.enums.EstadoUsuario;
import com.daw.tfg.enums.RolesUsuarios;
import com.daw.tfg.exception.ResourceNotFoundException;
import com.daw.tfg.exception.ValidationException;
import com.daw.tfg.models.Carrito;
import com.daw.tfg.models.Compra;
import com.daw.tfg.models.Juego;
import com.daw.tfg.models.MetodoPago;
import com.daw.tfg.models.PerfilUsuario;
import com.daw.tfg.models.Usuario;
import com.daw.tfg.repository.CarritoRepository;

@ExtendWith(MockitoExtension.class)
class CarritoServiceTest {

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private JuegoService juegoService;

    @Mock
    private CompraService compraService;

    @Mock
    private MetodoPagoService metodoPagoService;

    @InjectMocks
    private CarritoService carritoService;

    private Usuario usuario;
    private Juego juego;
    private Carrito carrito;

    @BeforeEach
    void setUp() {
        // Crear perfil
        PerfilUsuario perfil = new PerfilUsuario(
            "avatar.png", "background.jpg", "España", "Biografia", true
        );
        perfil.setId_usuario_perfil(1L);

        // Crear usuario
        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNombreUsuario("testuser");
        usuario.setCorreoElectronico("test@example.com");
        usuario.setContraseñaCifrada("encoded");
        usuario.setConexion(EstadoUsuario.DESCONECTADO);
        usuario.setRol(RolesUsuarios.USER);
        usuario.setAmigos(new HashSet<>());
        usuario.setPerfilUsuario(perfil);

        // Crear juego
        juego = new Juego();
        juego.setIdJuego(1L);
        juego.setTitulo("Test Game");
        juego.setPrecio(29.99f);
        juego.setDescripcion("Test Description");
        juego.setFechaLanzamiento(java.time.LocalDateTime.now());
        juego.setRequerimientos("Test Requirements");
        juego.setImagen("test.jpg");
        juego.setDesarrollador(null);
        juego.setEditor(null);
        juego.setTipo("JUEGO");
        juego.setGeneros(new HashSet<>());

        // Crear carrito
        carrito = new Carrito();
        carrito.setIdCarrito(1L);
        carrito.setUsuario(usuario);
        carrito.setJuegos(new HashSet<>());
    }

    // ==================== Tests para findAll ====================

    @Test
    void findAll_DeberiaRetornarListaDeCarritos() {
        when(carritoRepository.findAll()).thenReturn(List.of(carrito));

        List<Carrito> resultado = carritoService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    void findAll_CuandoNoHayCarritos_DeberiaRetornarListaVacia() {
        when(carritoRepository.findAll()).thenReturn(List.of());

        List<Carrito> resultado = carritoService.findAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ==================== Tests para findById ====================

    @Test
    void findById_DeberiaRetornarCarritoCuandoExiste() {
        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));

        Carrito resultado = carritoService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdCarrito());
    }

    @Test
    void findById_DeberiaLanzarExcepcionCuandoNoExiste() {
        when(carritoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            carritoService.findById(999L);
        });
    }

    // ==================== Tests para findByUsuario ====================

    @Test
    void findByUsuario_DeberiaRetornarCarritoCuandoExiste() {
        when(carritoRepository.findByUsuario(usuario)).thenReturn(Optional.of(carrito));

        Carrito resultado = carritoService.findByUsuario(usuario);

        assertNotNull(resultado);
        assertEquals(usuario, resultado.getUsuario());
    }

    @Test
    void findByUsuario_DeberiaLanzarExcepcionCuandoNoExiste() {
        when(carritoRepository.findByUsuario(usuario)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            carritoService.findByUsuario(usuario);
        });
    }

    // ==================== Tests para findByUsuarioId ====================

    @Test
    void findByUsuarioId_DeberiaRetornarCarrito() {
        when(usuarioService.findById(1L)).thenReturn(usuario);
        when(carritoRepository.findByUsuario(usuario)).thenReturn(Optional.of(carrito));

        Carrito resultado = carritoService.findByUsuarioId(1L);

        assertNotNull(resultado);
    }

    // ==================== Tests para save ====================

    @Test
    void save_DeberiaGuardarCarrito() {
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);

        Carrito resultado = carritoService.save(carrito);

        assertNotNull(resultado);
        verify(carritoRepository, times(1)).save(carrito);
    }

    // ==================== Tests para deleteById ====================

    @Test
    void deleteById_DeberiaEliminarCarritoCuandoExiste() {
        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        doNothing().when(carritoRepository).deleteById(1L);

        assertDoesNotThrow(() -> carritoService.deleteById(1L));

        verify(carritoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_DeberiaLanzarExcepcionCuandoNoExiste() {
        when(carritoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            carritoService.deleteById(999L);
        });
    }

    // ==================== Tests para getAllGamesInCart ====================

    @Test
    void getAllGamesInCart_DeberiaRetornarListaDeJuegos() {
        HashSet<Juego> juegos = new HashSet<>();
        juegos.add(juego);
        carrito.setJuegos(juegos);

        when(usuarioService.findById(1L)).thenReturn(usuario);
        when(carritoRepository.findByUsuario(usuario)).thenReturn(Optional.of(carrito));

        List<Juego> resultado = carritoService.getAllGamesInCart(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Test Game", resultado.get(0).getTitulo());
    }

    @Test
    void getAllGamesInCart_CarritoVacio_DeberiaRetornarListaVacia() {
        when(usuarioService.findById(1L)).thenReturn(usuario);
        when(carritoRepository.findByUsuario(usuario)).thenReturn(Optional.of(carrito));

        List<Juego> resultado = carritoService.getAllGamesInCart(1L);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ==================== Tests para addJuegoToCarrito ====================

    @Test
    void addJuegoToCarrito_DeberiaAñadirJuegoAlCarrito() {
        when(usuarioService.findById(1L)).thenReturn(usuario);
        when(juegoService.findById(1L)).thenReturn(juego);
        when(carritoRepository.findByUsuario(usuario)).thenReturn(Optional.of(carrito));
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);

        assertDoesNotThrow(() -> carritoService.addJuegoToCarrito(1L, 1L));

        verify(carritoRepository, times(1)).save(any(Carrito.class));
    }

    @Test
    void addJuegoToCarrito_CarritoNoExiste_DeberiaCrearNuevoCarrito() {
        when(usuarioService.findById(1L)).thenReturn(usuario);
        when(juegoService.findById(1L)).thenReturn(juego);
        when(carritoRepository.findByUsuario(usuario)).thenReturn(Optional.empty());
        when(carritoRepository.save(any(Carrito.class))).thenAnswer(invocation -> {
            Carrito c = invocation.getArgument(0);
            c.setIdCarrito(1L);
            return c;
        });

        assertDoesNotThrow(() -> carritoService.addJuegoToCarrito(1L, 1L));

        verify(carritoRepository, times(1)).save(any(Carrito.class));
    }

    @Test
    void addJuegoToCarrito_JuegoYaEnCarrito_DeberiaLanzarExcepcion() {
        HashSet<Juego> juegos = new HashSet<>();
        juegos.add(juego);
        carrito.setJuegos(juegos);

        when(usuarioService.findById(1L)).thenReturn(usuario);
        when(juegoService.findById(1L)).thenReturn(juego);
        when(carritoRepository.findByUsuario(usuario)).thenReturn(Optional.of(carrito));

        assertThrows(ValidationException.class, () -> {
            carritoService.addJuegoToCarrito(1L, 1L);
        });
    }

    // ==================== Tests para removeJuegoFromCarrito ====================

    @Test
    void removeJuegoFromCarrito_DeberiaEliminarJuegoDelCarrito() {
        HashSet<Juego> juegos = new HashSet<>();
        juegos.add(juego);
        carrito.setJuegos(juegos);

        when(usuarioService.findById(1L)).thenReturn(usuario);
        when(juegoService.findById(1L)).thenReturn(juego);
        when(carritoRepository.findByUsuario(usuario)).thenReturn(Optional.of(carrito));
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carrito);

        assertDoesNotThrow(() -> carritoService.removeJuegoFromCarrito(1L, 1L));

        verify(carritoRepository, times(1)).save(any(Carrito.class));
    }

    @Test
    void removeJuegoFromCarrito_JuegoNoEstaEnCarrito_DeberiaLanzarExcepcion() {
        when(usuarioService.findById(1L)).thenReturn(usuario);
        when(juegoService.findById(1L)).thenReturn(juego);
        when(carritoRepository.findByUsuario(usuario)).thenReturn(Optional.of(carrito));

        assertThrows(ValidationException.class, () -> {
            carritoService.removeJuegoFromCarrito(1L, 1L);
        });
    }

    @Test
    void removeJuegoFromCarrito_CarritoVacio_DeberiaLanzarExcepcion() {
        when(usuarioService.findById(1L)).thenReturn(usuario);
        when(juegoService.findById(1L)).thenReturn(juego);
        when(carritoRepository.findByUsuario(usuario)).thenReturn(Optional.of(carrito));

        assertThrows(ValidationException.class, () -> {
            carritoService.removeJuegoFromCarrito(1L, 1L);
        });
    }

    // ==================== Tests para getTotalPrice ====================

    @Test
    void getTotalPrice_DeberiaCalcularTotalCorrectamente() {
        HashSet<Juego> juegos = new HashSet<>();
        juegos.add(juego);
        carrito.setJuegos(juegos);

        when(usuarioService.findById(1L)).thenReturn(usuario);
        when(carritoRepository.findByUsuario(usuario)).thenReturn(Optional.of(carrito));

        Float resultado = carritoService.getTotalPrice(1L);

        assertNotNull(resultado);
        assertEquals(29.99f, resultado);
    }

    @Test
    void getTotalPrice_CarritoVacio_DeberiaRetornarCero() {
        when(usuarioService.findById(1L)).thenReturn(usuario);
        when(carritoRepository.findByUsuario(usuario)).thenReturn(Optional.of(carrito));

        Float resultado = carritoService.getTotalPrice(1L);

        assertNotNull(resultado);
        assertEquals(0.0f, resultado);
    }

    // ==================== Tests para checkout ====================

    @Test
    void checkout_CarritoVacio_DeberiaLanzarExcepcion() {
        when(usuarioService.findById(1L)).thenReturn(usuario);
        when(carritoRepository.findByUsuario(usuario)).thenReturn(Optional.of(carrito));

        assertThrows(ValidationException.class, () -> {
            carritoService.checkout(1L, null);
        });
    }
}

