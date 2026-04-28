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

        validarDTO(dto, juego);

        if (dto.getPrecio() != null) {
            juego.setPrecio(dto.getPrecio());
        }
        if (dto.getPorcentaje() != null) {
            juego.setPorcentaje(dto.getPorcentaje());
        }
        if (dto.getPrecioRebajado() != null) {
            juego.setPrecioRebajado(dto.getPrecioRebajado());
        } else if (dto.getPrecio() != null && dto.getPrecio() == 0.0) {
            // Si el precio pasa a 0 (gratis), limpiar precio rebajado
            juego.setPrecioRebajado(null);
        }
        if (dto.getDisponible() != null) {
            juego.setDisponible(dto.getDisponible());
        }

        // Si el juego es gratis (precio = 0), asegurar que no hay descuento ni precio rebajado
        if (juego.getPrecio() != null && juego.getPrecio() == 0.0) {
            juego.setPorcentaje(null);
            juego.setPrecioRebajado(null);
        }

        return juegoRepository.save(juego);
    }

    /**
     * Valida los datos del DTO antes de aplicar cambios.
     */
    private void validarDTO(ActualizarJuegoDesarrolladorDTO dto, Juego juegoActual) {
        Double precio = dto.getPrecio() != null ? dto.getPrecio() : juegoActual.getPrecio();
        Integer porcentaje = dto.getPorcentaje() != null ? dto.getPorcentaje() : juegoActual.getPorcentaje();
        Double precioRebajado = dto.getPrecioRebajado() != null ? dto.getPrecioRebajado() : juegoActual.getPrecioRebajado();

        if (precio != null && precio < 0) {
            throw new IllegalArgumentException("El precio base no puede ser negativo");
        }

        if (porcentaje != null && (porcentaje < 0 || porcentaje > 100)) {
            throw new IllegalArgumentException("El descuento debe estar entre 0 y 100");
        }

        if (precioRebajado != null && precio != null && precioRebajado > precio) {
            throw new IllegalArgumentException("El precio rebajado no puede ser mayor que el precio base");
        }

        if (precio != null && precio == 0.0 && porcentaje != null && porcentaje > 0) {
            throw new IllegalArgumentException("Un juego gratis no puede tener descuento");
        }
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
