package com.daw.tfg.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.daw.tfg.dtos.JuegoDTO;
import com.daw.tfg.exception.ResourceNotFoundException;
import com.daw.tfg.exception.ValidationException;
import com.daw.tfg.models.Desarrollador;
import com.daw.tfg.models.Editor;
import com.daw.tfg.models.Juego;
import com.daw.tfg.repository.JuegoRepository;

/**
 * Ejemplo de test unitario con Mockito para JuegoService.
 * 
 * NOTA: Para tests con base de datos H2 real (integración),
 * usarías @DataJpaTest o @SpringBootTest en lugar de Mockito.
 */
@ExtendWith(MockitoExtension.class)
class JuegoServiceTest {

    @Mock
    private JuegoRepository juegoRepository;

    @Mock
    private DesarrolladorService desarrolladorService;

    @Mock
    private EditorService editorService;

    @InjectMocks
    private JuegoService juegoService;

    private Desarrollador desarrollador;
    private Editor editor;
    private Juego juego;

    @BeforeEach
    void setUp() {
        // Crear datos de prueba
        desarrollador = new Desarrollador();
        desarrollador.setIdDesarrollador(1L);
        desarrollador.setNombre("Test Desarrollador");
        desarrollador.setImagen("test-dev.jpg");
        desarrollador.setJuego(new HashSet<>());

        editor = new Editor();
        editor.setIdEditor(1L);
        editor.setNombre("Test Editor");
        editor.setImagen("test-editor.jpg");
        editor.setJuego(new HashSet<>());

        juego = new Juego();
        juego.setIdJuego(1L);
        juego.setTitulo("Test Game");
        juego.setPrecio(29.99f);
        juego.setDescripcion("Test Description");
        juego.setFechaLanzamiento(LocalDateTime.now());
        juego.setRequerimientos("Test Requirements");
        juego.setImagen("test-game.jpg");
        juego.setDesarrollador(desarrollador);
        juego.setEditor(editor);
        juego.setTipo("JUEGO");
        juego.setGeneros(new HashSet<>());
    }

    // ==================== Tests para findAll ====================

    @Test
    void findAll_DeberiaRetornarListaDeJuegos() {
        when(juegoRepository.findAll()).thenReturn(List.of(juego));

        List<Juego> resultado = juegoService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Test Game", resultado.get(0).getTitulo());
        verify(juegoRepository, times(1)).findAll();
    }

    @Test
    void findAll_CuandoNoHayJuegos_DeberiaRetornarListaVacia() {
        when(juegoRepository.findAll()).thenReturn(List.of());

        List<Juego> resultado = juegoService.findAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ==================== Tests para findById ====================

    @Test
    void findById_DeberiaRetornarJuegoCuandoExiste() {
        when(juegoRepository.findById(1L)).thenReturn(Optional.of(juego));

        Juego resultado = juegoService.findById(1L);

        assertNotNull(resultado);
        assertEquals("Test Game", resultado.getTitulo());
    }

    @Test
    void findById_DeberiaLanzarExcepcionCuandoNoExiste() {
        when(juegoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            juegoService.findById(999L);
        });
    }

    @Test
    void findById_DeberiaLanzarExcepcionCuandoIdEsNulo() {
        assertThrows(ValidationException.class, () -> {
            juegoService.findById(null);
        });
    }

    // ==================== Tests para save ====================

    @Test
    void save_DeberiaGuardarJuegoCorrectamente() {
        when(juegoRepository.save(any(Juego.class))).thenReturn(juego);

        Juego resultado = juegoService.save(juego);

        assertNotNull(resultado);
        assertEquals("Test Game", resultado.getTitulo());
        verify(juegoRepository, times(1)).save(juego);
    }

    @Test
    void save_DeberiaLanzarExcepcionCuandoJuegoEsNulo() {
        assertThrows(ValidationException.class, () -> {
            juegoService.save(null);
        });
    }

    @Test
    void save_DeberiaLanzarExcepcionCuandoFaltaDesarrollador() {
        juego.setDesarrollador(null);

        assertThrows(ValidationException.class, () -> {
            juegoService.save(juego);
        });
    }

    @Test
    void save_DeberiaLanzarExcepcionCuandoFaltaEditor() {
        juego.setEditor(null);

        assertThrows(ValidationException.class, () -> {
            juegoService.save(juego);
        });
    }

    // ==================== Tests para deleteById ====================

    @Test
    void deleteById_DeberiaEliminarJuegoCuandoExiste() {
        when(juegoRepository.findById(1L)).thenReturn(Optional.of(juego));
        doNothing().when(juegoRepository).deleteById(1L);

        assertDoesNotThrow(() -> juegoService.deleteById(1L));

        verify(juegoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_DeberiaLanzarExcepcionCuandoNoExiste() {
        when(juegoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            juegoService.deleteById(999L);
        });
    }

    @Test
    void deleteById_DeberiaLanzarExcepcionCuandoIdEsNulo() {
        assertThrows(ValidationException.class, () -> {
            juegoService.deleteById(null);
        });
    }

    // ==================== Tests para búsquedas ====================

    @Test
    void findByTituloContainingIgnoreCase_DeberiaBuscarPorTitulo() {
        when(juegoRepository.findByTituloContainingIgnoreCase("Test"))
                .thenReturn(List.of(juego));

        List<Juego> resultado = juegoService.findByTituloContainingIgnoreCase("Test");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    void findByTituloContainingIgnoreCase_DeberiaLanzarExcepcionCuandoTextoEsVacio() {
        assertThrows(ValidationException.class, () -> {
            juegoService.findByTituloContainingIgnoreCase("");
        });
    }

    @Test
    void findByTituloContainingIgnoreCase_DeberiaLanzarExcepcionCuandoTextoEsNulo() {
        assertThrows(ValidationException.class, () -> {
            juegoService.findByTituloContainingIgnoreCase(null);
        });
    }

    // ==================== Tests con DTOs ====================

    @Test
    void createFromDTO_DeberiaCrearJuegoDesdeDTO() {
        JuegoDTO dto = new JuegoDTO();
        dto.setTitulo("Nuevo Juego");
        dto.setPrecio(39.99f);
        dto.setDescripcion("Nueva descripción");
        dto.setFechaLanzamiento(LocalDateTime.now());
        dto.setRequerimientos("Nuevos requisitos");
        dto.setImagen("nueva-imagen.jpg");
        dto.setDesarrolladorId(1L);
        dto.setEditorId(1L);
        dto.setTipo("JUEGO");

        when(desarrolladorService.findById(1L)).thenReturn(Optional.of(desarrollador));
        when(editorService.findById(1L)).thenReturn(Optional.of(editor));
        when(juegoRepository.save(any(Juego.class))).thenAnswer(invocation -> {
            Juego j = invocation.getArgument(0);
            j.setIdJuego(1L);
            return j;
        });

        Juego resultado = juegoService.createFromDTO(dto);

        assertNotNull(resultado);
        assertEquals("Nuevo Juego", resultado.getTitulo());
        assertEquals(39.99f, resultado.getPrecio());
        verify(juegoRepository, times(1)).save(any(Juego.class));
    }

    @Test
    void createFromDTO_DeberiaLanzarExcepcionCuandoDesarrolladorNoExiste() {
        JuegoDTO dto = new JuegoDTO();
        dto.setTitulo("Nuevo Juego");
        dto.setDesarrolladorId(999L);
        dto.setEditorId(1L);

        when(desarrolladorService.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            juegoService.createFromDTO(dto);
        });
    }

    @Test
    void createFromDTO_DeberiaLanzarExcepcionCuandoEditorNoExiste() {
        JuegoDTO dto = new JuegoDTO();
        dto.setTitulo("Nuevo Juego");
        dto.setDesarrolladorId(1L);
        dto.setEditorId(999L);

        when(desarrolladorService.findById(1L)).thenReturn(Optional.of(desarrollador));
        when(editorService.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            juegoService.createFromDTO(dto);
        });
    }

    // ==================== Tests para updateFromDTO ====================

    @Test
    void updateFromDTO_DeberiaActualizarJuegoExistente() {
        JuegoDTO dto = new JuegoDTO();
        dto.setTitulo("Título Actualizado");
        dto.setPrecio(49.99f);

        when(juegoRepository.findById(1L)).thenReturn(Optional.of(juego));
        when(juegoRepository.save(any(Juego.class))).thenReturn(juego);

        Juego resultado = juegoService.updateFromDTO(1L, dto);

        assertNotNull(resultado);
        assertEquals("Título Actualizado", resultado.getTitulo());
    }

    @Test
    void updateFromDTO_DeberiaLanzarExcepcionCuandoJuegoNoExiste() {
        JuegoDTO dto = new JuegoDTO();
        dto.setTitulo("Título");

        when(juegoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            juegoService.updateFromDTO(999L, dto);
        });
    }

    @Test
    void updateFromDTO_DeberiaLanzarExcepcionCuandoPrecioEsNegativo() {
        JuegoDTO dto = new JuegoDTO();
        dto.setTitulo("Título");
        dto.setPrecio(-10.0f);

        when(juegoRepository.findById(1L)).thenReturn(Optional.of(juego));

        assertThrows(ValidationException.class, () -> {
            juegoService.updateFromDTO(1L, dto);
        });
    }
}

