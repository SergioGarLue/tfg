package com.daw.tfg.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daw.tfg.dtos.ActualizarJuegoDesarrolladorDTO;
import com.daw.tfg.models.Juego;
import com.daw.tfg.models.Usuario;
import com.daw.tfg.repository.JuegoRepository;
import com.daw.tfg.repository.UsuarioRepository;

@Service
public class DesarrolladorB2BService {

    private final JuegoRepository juegoRepository;
    private final UsuarioRepository usuarioRepository;

    public DesarrolladorB2BService(JuegoRepository juegoRepository, UsuarioRepository usuarioRepository) {
        this.juegoRepository = juegoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Obtiene los juegos asociados al desarrollador (empresa) del usuario autenticado.
     */
    @Transactional(readOnly = true)
    public List<Juego> obtenerMisJuegos(String nombreUsuario) {
        Usuario usuario = obtenerUsuarioDeveloperValidado(nombreUsuario);
        Long idDesarrollador = usuario.getDesarrollador().getIdDesarrollador();
        return juegoRepository.findByDesarrolladorIdDesarrollador(idDesarrollador);
    }

    /**
     * Actualiza precio, descuento y disponibilidad de un juego si pertenece al desarrollador del usuario.
     */
    @Transactional
    public Juego actualizarJuego(String nombreUsuario, Long idJuego, ActualizarJuegoDesarrolladorDTO dto) {
        Usuario usuario = obtenerUsuarioDeveloperValidado(nombreUsuario);

        Juego juego = juegoRepository.findById(idJuego)
                .orElseThrow(() -> new IllegalArgumentException("Juego no encontrado"));

        if (!juego.getDesarrollador().getIdDesarrollador().equals(usuario.getDesarrollador().getIdDesarrollador())) {
            throw new SecurityException("No tienes permiso para modificar este juego");
        }

        if (dto.getPrecio() != null) {
            juego.setPrecio(dto.getPrecio());
        }
        if (dto.getPorcentaje() != null) {
            juego.setPorcentaje(dto.getPorcentaje());
        }
        if (dto.getPrecioRebajado() != null) {
            juego.setPrecioRebajado(dto.getPrecioRebajado());
        }
        if (dto.getDisponible() != null) {
            juego.setDisponible(dto.getDisponible());
        }

        return juegoRepository.save(juego);
    }

    /**
     * Valida que el usuario tenga rol DEVELOPER y esté vinculado a una empresa.
     */
    private Usuario obtenerUsuarioDeveloperValidado(String nombreUsuario) {
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (usuario.getRol() == null ||
            (!usuario.getRol().name().equals("DEVELOPER") && !usuario.getRol().name().equals("DESARROLLADOR"))) {
            throw new SecurityException("El usuario no tiene rol DEVELOPER/DESARROLLADOR");
        }

        if (usuario.getDesarrollador() == null) {
            throw new IllegalStateException("El usuario DEVELOPER no está vinculado a ninguna empresa");
        }

        return usuario;
    }
}
