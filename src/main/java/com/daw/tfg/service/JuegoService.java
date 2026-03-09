package com.daw.tfg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.daw.tfg.dtos.JuegoDTO;
import com.daw.tfg.exception.ResourceNotFoundException;
import com.daw.tfg.exception.ValidationException;
import com.daw.tfg.models.Desarrollador;
import com.daw.tfg.models.Editor;
import com.daw.tfg.models.Juego;
import com.daw.tfg.repository.JuegoRepository;

@Service
public class JuegoService {

    private final JuegoRepository juegoRepository;
    private final DesarrolladorService desarrolladorService;
    private final EditorService editorService;

    public JuegoService(JuegoRepository juegoRepository, DesarrolladorService desarrolladorService, EditorService editorService) {
        this.juegoRepository = juegoRepository;
        this.desarrolladorService = desarrolladorService;
        this.editorService = editorService;
    }

    // ==================== Métodos helpers para obtener entidades ====================
    
    /**
     * Obtiene un desarrollador por ID o lanza excepción si no existe.
     */
    private Desarrollador getDesarrolladorById(Long id) {
        return desarrolladorService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Desarrollador", "id", id));
    }

    /**
     * Obtiene un editor por ID o lanza excepción si no existe.
     */
    private Editor getEditorById(Long id) {
        return editorService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Editor", "id", id));
    }

    /**
     * Obtiene un juego padre por ID o lanza excepción si no existe.
     */
    private Juego getJuegoPadreById(Long id) {
        if (id == null) {
            return null;
        }
        return findById(id);
    }

    // ==================== CRUD Básico ====================

    public List<Juego> findAll() {
        return juegoRepository.findAll();
    }

    public Juego findById(Long id) {
        if (id == null) {
            throw new ValidationException("Id de juego inválido");
        }
        return juegoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Juego", "id", id));
    }

    /**
     * Guarda un juego. La validación se delegate al DTO con @Valid.
     * Este método solo verifica la existencia de entidades relacionadas.
     */
    public Juego save(Juego juego) {
        if (juego == null) {
            throw new ValidationException("Juego inválido");
        }
        // Validar entidades relacionadas
        if (juego.getDesarrollador() == null) {
            throw new ValidationException("Desarrollador obligatorio");
        }
        if (juego.getEditor() == null) {
            throw new ValidationException("Editor obligatorio");
        }
        return juegoRepository.save(juego);
    }

    public void deleteById(Long id) {
        if (id == null) {
            throw new ValidationException("Id inválido");
        }
        if (juegoRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Juego", "id", id);
        }
        juegoRepository.deleteById(id);
    }

    // ==================== Métodos con DTOs ====================

    /**
     * Crea un nuevo juego a partir de un DTO.
     * La validación de campos se maneja vía @Valid en el controlador.
     */
    public Juego createFromDTO(JuegoDTO dto) {
        // Validar que existen desarrollador y editor
        Desarrollador desarrollador = getDesarrolladorById(dto.getDesarrolladorId());
        Editor editor = getEditorById(dto.getEditorId());
        
        // Obtener juego padre si se proporciona
        Juego juegoPadre = getJuegoPadreById(dto.getJuegoPadreId());

        Juego juego = new Juego();
        juego.setTitulo(dto.getTitulo());
        juego.setPrecio(dto.getPrecio());
        juego.setDescripcion(dto.getDescripcion());
        juego.setFechaLanzamiento(dto.getFechaLanzamiento());
        juego.setRequerimientos(dto.getRequerimientos());
        juego.setImagen(dto.getImagen());
        juego.setDesarrollador(desarrollador);
        juego.setEditor(editor);
        juego.setTipo(dto.getTipo());
        juego.setJuegoPadre(juegoPadre);

        return save(juego);
    }

    /**
     * Actualiza un juego existente a partir de un DTO.
     * Solo actualiza los campos presentes en el DTO (no sobrescribe con null).
     */
    public Juego updateFromDTO(Long id, JuegoDTO dto) {
        Juego existente = findById(id);

        // Actualizar título si se proporciona
        if (dto.getTitulo() != null && !dto.getTitulo().isBlank()) {
            existente.setTitulo(dto.getTitulo());
        }

        // Actualizar precio si se proporciona
        if (dto.getPrecio() != null) {
            if (dto.getPrecio() < 0) {
                throw new ValidationException("El precio no puede ser negativo");
            }
            existente.setPrecio(dto.getPrecio());
        }

        // Actualizar descripción si se proporciona
        if (dto.getDescripcion() != null) {
            existente.setDescripcion(dto.getDescripcion());
        }

        // Actualizar fecha de lanzamiento si se proporciona
        if (dto.getFechaLanzamiento() != null) {
            existente.setFechaLanzamiento(dto.getFechaLanzamiento());
        }

        // Actualizar requerimientos si se proporciona
        if (dto.getRequerimientos() != null) {
            existente.setRequerimientos(dto.getRequerimientos());
        }

        // Actualizar imagen si se proporciona
        if (dto.getImagen() != null) {
            existente.setImagen(dto.getImagen());
        }

        // Actualizar tipo si se proporciona
        if (dto.getTipo() != null) {
            existente.setTipo(dto.getTipo());
        }

        // Actualizar desarrollador si se proporciona
        if (dto.getDesarrolladorId() != null) {
            Desarrollador desarrollador = getDesarrolladorById(dto.getDesarrolladorId());
            existente.setDesarrollador(desarrollador);
        }

        // Actualizar editor si se proporciona
        if (dto.getEditorId() != null) {
            Editor editor = getEditorById(dto.getEditorId());
            existente.setEditor(editor);
        }
        
        // Actualizar juego padre si se proporciona
        if (dto.getJuegoPadreId() != null) {
            Juego juegoPadre = getJuegoPadreById(dto.getJuegoPadreId());
            existente.setJuegoPadre(juegoPadre);
        }

        return save(existente);
    }

    // ==================== Búsquedas y Filtros ====================
    // Los métodos de búsqueda devuelven lista vacía (200 OK) en lugar de excepciones
    // Spring Data JPA siempre devuelve una lista vacía, nunca null

    public List<Juego> findByTituloContainingIgnoreCase(String tituloParte) {
        if (tituloParte == null || tituloParte.isBlank()) {
            throw new ValidationException("Fragmento de título inválido");
        }
        return juegoRepository.findByTituloContainingIgnoreCase(tituloParte);
    }

    public List<Juego> findByTituloContaining(String fragmento) {
        return findByTituloContainingIgnoreCase(fragmento);
    }

    public List<Juego> findByGenerosNombre(String nombreGenero) {
        if (nombreGenero == null || nombreGenero.isBlank()) {
            throw new ValidationException("Nombre de género inválido");
        }
        return juegoRepository.findByGenerosNombre(nombreGenero);
    }

    public List<Juego> findByPrecioBetween(Float min, Float max) {
        if (min == null || max == null) {
            throw new ValidationException("Rango de precio inválido");
        }
        if (min > max) {
            throw new ValidationException("El precio mínimo no puede ser mayor que el máximo");
        }
        return juegoRepository.findByPrecioBetween(min, max);
    }

    public List<Juego> findByDesarrolladorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new ValidationException("Nombre de desarrollador inválido");
        }
        return juegoRepository.findByDesarrolladorNombreIgnoreCase(nombre);
    }

    public List<Juego> findByEditorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new ValidationException("Nombre de editor inválido");
        }
        return juegoRepository.findByEditorNombreIgnoreCase(nombre);
    }

    public Optional<Juego> findOptionalById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return juegoRepository.findById(id);
    }
}
