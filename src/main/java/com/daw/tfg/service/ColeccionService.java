package com.daw.tfg.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daw.tfg.models.ColeccionFavoritos;
import com.daw.tfg.models.Juego;
import com.daw.tfg.models.Usuario;
import com.daw.tfg.repository.ColeccionFavoritosRepository;

@Service
public class ColeccionService {

    private final ColeccionFavoritosRepository coleccionFavoritosRepository;
    private final UsuarioService usuarioService;
    private final JuegoService juegoService;

    public ColeccionService(ColeccionFavoritosRepository coleccionFavoritosRepository,
            UsuarioService usuarioService,
            JuegoService juegoService) {
        this.coleccionFavoritosRepository = coleccionFavoritosRepository;
        this.usuarioService = usuarioService;
        this.juegoService = juegoService;
    }

    // CRUD

    public List<ColeccionFavoritos> findAll() {
        return coleccionFavoritosRepository.findAll();
    }

    public ColeccionFavoritos findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id inválido");
        }
        return coleccionFavoritosRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ítem de colección no encontrado con id: " + id));
    }

    public List<ColeccionFavoritos> findByUsuario(Long usuarioId) {
        Usuario usuario = usuarioService.findById(usuarioId);
        return coleccionFavoritosRepository.findByUsuario(usuario);
    }

    public List<ColeccionFavoritos> findFavoritosByUsuario(Long usuarioId) {
        Usuario usuario = usuarioService.findById(usuarioId);
        return coleccionFavoritosRepository.findByUsuarioAndEsFavorito(usuario, true);
    }

    public ColeccionFavoritos save(ColeccionFavoritos item) {
        if (item == null) {
            throw new IllegalArgumentException("Ítem de colección inválido");
        }
        if (item.getUsuario() == null) {
            throw new IllegalArgumentException("Usuario obligatorio");
        }
        if (item.getJuego() == null) {
            throw new IllegalArgumentException("Juego obligatorio");
        }
        if (item.getFechaAdquisicion() == null) {
            throw new IllegalArgumentException("Fecha de adquisición obligatoria");
        }
        return coleccionFavoritosRepository.save(item);
    }

    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id inválido");
        }
        if (coleccionFavoritosRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Ítem de colección no encontrado con id: " + id);
        }
        coleccionFavoritosRepository.deleteById(id);
    }

    // Lógica de negocio

    /**
     * Obtiene todos los juegos en la colección (comprados) del usuario.
     * 
     * @param usuarioId ID del usuario
     * @return Lista de juegos en la colección
     */
    public List<Juego> getAllGamesInCollection(Long usuarioId) {
        List<ColeccionFavoritos> items = findByUsuario(usuarioId);
        return items.stream()
                .map(ColeccionFavoritos::getJuego)
                .toList();
    }

    /**
     * Obtiene todos los juegos favoritos del usuario.
     * 
     * @param usuarioId ID del usuario
     * @return Lista de juegos marcados como favoritos
     */
    public List<Juego> getFavoritesFromCollection(Long usuarioId) {
        List<ColeccionFavoritos> items = findFavoritosByUsuario(usuarioId);
        return items.stream()
                .map(ColeccionFavoritos::getJuego)
                .toList();
    }

    public List<ColeccionFavoritos> findRecentByUsuario(Long usuarioId, int limit) {
        Usuario usuario = usuarioService.findById(usuarioId);
        List<ColeccionFavoritos> recientes = coleccionFavoritosRepository.findByUsuarioOrderByFechaAdquisicionDesc(usuario);
        if (recientes.size() > limit) {
            return recientes.subList(0, limit);
        }
        return recientes;
    }

    /**
     * Añade un juego a la colección del usuario con fecha actual.
     * 
     * @param usuarioId ID del usuario
     * @param juegoId   ID del juego a añadir
     * @throws IllegalArgumentException si el juego ya existe en la colección
     */
    @Transactional
    public void addJuegoToCollection(Long usuarioId, Long juegoId) {
        addJuegoToCollection(usuarioId, juegoId, new Date());
    }

    @Transactional
    public void addJuegoToCollectionIfAbsent(Long usuarioId, Long juegoId, Date fechaAdquisicion) {
        Usuario usuario = usuarioService.findById(usuarioId);
        Juego juego = juegoService.findById(juegoId);

        if (fechaAdquisicion == null) {
            fechaAdquisicion = new Date();
        }

        if (coleccionFavoritosRepository.findByUsuarioAndJuego(usuario, juego).isPresent()) {
            return;
        }

        ColeccionFavoritos item = new ColeccionFavoritos(usuario, juego, fechaAdquisicion);
        save(item);
    }

    public void añadirJuegoAColeccion(Long usuarioId, Long juegoId) {
        addJuegoToCollection(usuarioId, juegoId);
    }

    /**
     * Añade un juego a la colección del usuario con fecha específica.
     * 
     * @param usuarioId        ID del usuario
     * @param juegoId          ID del juego a añadir
     * @param fechaAdquisicion Fecha de adquisición del juego
     * @throws IllegalArgumentException si el juego ya existe en la colección
     */
    @Transactional
    public void addJuegoToCollection(Long usuarioId, Long juegoId, Date fechaAdquisicion) {
        Usuario usuario = usuarioService.findById(usuarioId);
        Juego juego = juegoService.findById(juegoId);

        if (fechaAdquisicion == null) {
            fechaAdquisicion = new Date();
        }

        // Evitar duplicados
        Optional<ColeccionFavoritos> existente = coleccionFavoritosRepository.findByUsuarioAndJuego(usuario, juego);
        if (existente.isPresent()) {
            throw new IllegalArgumentException("El juego ya está en la colección");
        }

        ColeccionFavoritos item = new ColeccionFavoritos(usuario, juego, fechaAdquisicion);
        save(item);
    }

    /**
     * Elimina un juego de la colección del usuario.
     * 
     * @param usuarioId ID del usuario
     * @param juegoId   ID del juego a eliminar
     * @throws IllegalArgumentException si el juego no está en la colección
     */
    @Transactional
    public void removeJuegoFromCollection(Long usuarioId, Long juegoId) {
        Usuario usuario = usuarioService.findById(usuarioId);
        Juego juego = juegoService.findById(juegoId);

        Optional<ColeccionFavoritos> existe = coleccionFavoritosRepository.findByUsuarioAndJuego(usuario, juego);
        if (existe.isEmpty()) {
            throw new IllegalArgumentException("El juego no está en la colección");
        }

        deleteById(existe.get().getIdColeccionJuego());
    }

    /**
     * Marca un juego como favorito en la colección del usuario.
     * 
     * @param usuarioId ID del usuario
     * @param juegoId   ID del juego a marcar como favorito
     */
    @Transactional
    public void markAsFavorite(Long usuarioId, Long juegoId) {
        Usuario usuario = usuarioService.findById(usuarioId);
        Juego juego = juegoService.findById(juegoId);

        ColeccionFavoritos item = coleccionFavoritosRepository.findByUsuarioAndJuego(usuario, juego)
                .orElseThrow(() -> new IllegalArgumentException("El juego no está en la colección"));

        item.setEsFavorito(true);
        save(item);
    }

    /**
     * Desmarca un juego como favorito en la colección del usuario.
     * 
     * @param usuarioId ID del usuario
     * @param juegoId   ID del juego a desmarcar
     */
    @Transactional
    public void unmarkAsFavorite(Long usuarioId, Long juegoId) {
        Usuario usuario = usuarioService.findById(usuarioId);
        Juego juego = juegoService.findById(juegoId);

        ColeccionFavoritos item = coleccionFavoritosRepository.findByUsuarioAndJuego(usuario, juego)
                .orElseThrow(() -> new IllegalArgumentException("El juego no está en la colección"));

        item.setEsFavorito(false);
        save(item);
    }
}

