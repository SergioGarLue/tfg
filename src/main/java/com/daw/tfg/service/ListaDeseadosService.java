package com.daw.tfg.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daw.tfg.models.Juego;
import com.daw.tfg.models.ListaDeseados;
import com.daw.tfg.models.Usuario;
import com.daw.tfg.repository.ListaDeseadosRepository;

@Service
public class ListaDeseadosService {

    private final ListaDeseadosRepository listaRepository;
    private final UsuarioService usuarioService;
    private final JuegoService juegoService;

    public ListaDeseadosService(ListaDeseadosRepository listaRepository, UsuarioService usuarioService, JuegoService juegoService) {
        this.listaRepository = listaRepository;
        this.usuarioService = usuarioService;
        this.juegoService = juegoService;
    }

    // CRUD

    public List<ListaDeseados> findAll() {
        return listaRepository.findAll();
    }

    public ListaDeseados findById(Long id) {
        Optional<ListaDeseados> lista = listaRepository.findById(id);
        if (lista.isEmpty()) {
            throw new IllegalArgumentException("Lista de deseados no encontrada");
        }
        return lista.get();
    }

    public ListaDeseados findByUsuario(Usuario usuario) {
        Optional<ListaDeseados> lista = listaRepository.findByUsuario(usuario);
        if (lista.isEmpty()) {
            throw new IllegalArgumentException("Lista de deseados no encontrada");
        }
        return lista.get();
    }

    public ListaDeseados save(ListaDeseados lista) {
        return listaRepository.save(lista);
    }

    public void deleteById(Long id) {
        if (listaRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Lista de deseados no encontrada");
        }
        listaRepository.deleteById(id);
    }

    // Lógica de negocio

    /**
     * Obtiene todos los juegos en la lista de deseados de un usuario.
     * 
     * @param usuarioId ID del usuario
     * @return Lista de juegos en la lista de deseados
     * @throws IllegalArgumentException si el usuario no tiene lista de deseados
     */
    public List<Juego> getAllGamesInLista(Long usuarioId) {
        Usuario usuario = usuarioService.findById(usuarioId);
        ListaDeseados lista = findByUsuario(usuario);

        if (lista.getJuegos() == null || lista.getJuegos().isEmpty()) {
            return List.of();
        }

        return List.copyOf(lista.getJuegos());
    }

    /**
     * Añade el juego a la lista de deseados del usuario comprobando que el usuario existe.
     * Si la lista no existe, crea una nueva asociada a ese usuario.
     * 
     * @param usuarioId ID del usuario
     * @param juegoId   ID del juego a añadir
     * @throws IllegalArgumentException si el usuario o juego no existen, o si el
     *                                  juego ya está en la lista
     */
    @Transactional
    public void addJuegoToLista(Long usuarioId, Long juegoId) {
        Usuario usuario = usuarioService.findById(usuarioId);
        Juego juego = juegoService.findById(juegoId);

        // 1. Intentamos buscar la lista existente o creamos una nueva si no existe
        ListaDeseados lista = listaRepository.findByUsuario(usuario)
                .orElseGet(() -> {
                    ListaDeseados nuevaLista = new ListaDeseados();
                    nuevaLista.setUsuario(usuario);
                    nuevaLista.setJuegos(new HashSet<>()); // Inicializar el Set para evitar NullPointerException
                    return nuevaLista;
                });

        // Asegurar que la lista tiene el usuario asignado (por si se recuperó de BD)
        if (lista.getUsuario() == null) {
            lista.setUsuario(usuario);
        }

        // Inicializar juegos si es null (por si la lista venía de BD sin inicializar)
        if (lista.getJuegos() == null) {
            lista.setJuegos(new HashSet<>());
        }

        // 2. Verificamos si el juego ya está en la lista
        if (lista.getJuegos().contains(juego)) {
            throw new IllegalArgumentException("El juego ya está en la lista de deseados");
        }

        // 3. Añadimos el juego a la colección
        lista.getJuegos().add(juego);

        // 4. Guardamos la lista
        save(lista);
    }

    /**
     * Elimina un juego de la lista de deseados del usuario.
     * Verifica que el juego exista en la lista antes de eliminarlo.
     * 
     * @param usuarioId ID del usuario
     * @param juegoId   ID del juego a eliminar
     * @throws IllegalArgumentException si el usuario no tiene lista o el juego no
     *                                  está en la lista
     */
    @Transactional
    public void removeJuegoFromLista(Long usuarioId, Long juegoId) {
        Usuario usuario = usuarioService.findById(usuarioId);
        Juego juego = juegoService.findById(juegoId);

        ListaDeseados lista = findByUsuario(usuario);

        // Verificar que la lista tenga juegos inicializados
        if (lista.getJuegos() == null) {
            throw new IllegalStateException("Lista vacía");
        }

        // Verificar que el juego esté en la lista antes de intentar eliminarlo
        if (!lista.getJuegos().contains(juego)) {
            throw new IllegalArgumentException("El juego no está en la lista de deseados");
        }

        lista.getJuegos().remove(juego);
        save(lista);
    }

    /**
     * Obtiene la cantidad de juegos en la lista de deseados de un usuario.
     * 
     * @param usuarioId ID del usuario
     * @return Cantidad de juegos en la lista
     * @throws IllegalArgumentException si el usuario no tiene lista
     */
    public int getQuantityGames(Long usuarioId) {
        Usuario usuario = usuarioService.findById(usuarioId);
        ListaDeseados lista = findByUsuario(usuario);

        if (lista.getJuegos() == null) {
            return 0;
        }

        return lista.getJuegos().size();
    }
}

