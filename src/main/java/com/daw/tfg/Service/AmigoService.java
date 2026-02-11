package com.daw.tfg.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daw.tfg.Repository.AmigoRepository;
import com.daw.tfg.models.Amistad;
import com.daw.tfg.models.Usuario;
import com.daw.tfg.Enums.EstadoPeticion;

@Service
public class AmigoService {

    private final AmigoRepository amigoRepository;
    private final UsuarioService usuarioService;

    public AmigoService(AmigoRepository amigoRepository, UsuarioService usuarioService) {
        this.amigoRepository = amigoRepository;
        this.usuarioService = usuarioService;
    }

    // Metodos CRUD

    public List<Amistad> findAll() {
        return amigoRepository.findAll();
    }

    public Amistad findById(Long id) {
        Optional<Amistad> amigo = amigoRepository.findById(id);
        if (amigo.isEmpty()) {
            return null;
        }
        return amigo.get();
    }

    public List<Amistad> findByUsuarioSolicitante(Usuario u) {
        return amigoRepository.findBySolicitante(u);
    }

    public List<Amistad> findByUsuarioDestino(Usuario u) {
        return amigoRepository.findByDestinatario(u);
    }

    public Amistad save(Amistad a) {
        return amigoRepository.save(a);
    }

    public void deleteById(Long id) {
        amigoRepository.deleteById(id);
    }

    // Logica de negocio

    /**
     * Enviar una peticion de amistad
     * Valida que existan ambos usuarios y la peticion no se haya creado antes
     */
    @Transactional
    public void sendFriendRequest(Long solicitanteId, Long destinatarioId) {
        if (solicitanteId.equals(destinatarioId)) {
            throw new IllegalArgumentException("No puedes enviarte una solicitud a ti mismo");
        }

        Usuario solicitante = usuarioService.findById(solicitanteId);
        Usuario destinatario = usuarioService.findById(destinatarioId);

        // Comrpueba que el amigo existe y que la peticion existe
        List<Amistad> existing = amigoRepository.findBySolicitante(solicitante);
        existing.addAll(amigoRepository.findByDestinatario(solicitante));
        existing.addAll(amigoRepository.findBySolicitante(destinatario));
        existing.addAll(amigoRepository.findByDestinatario(destinatario));
        boolean hasRelation = existing.stream()
                .anyMatch(a -> (a.getSolicitante().equals(solicitante) && a.getDestinatario().equals(destinatario)) ||
                        (a.getSolicitante().equals(destinatario) && a.getDestinatario().equals(solicitante)));
        if (hasRelation) {
            throw new IllegalArgumentException("Ya existe una solicitud o amistad entre estos usuarios");
        }

        Amistad amistad = new Amistad(solicitante, destinatario, EstadoPeticion.PENDIENTE);
        save(amistad);
    }

    /**
     * Aceptar una peticion de amistas
     * comprueba que la peticion existe y esta pendiente
     */
    @Transactional
    public void acceptFriendRequest(Long amistadId, Long destinatarioId) {
        Amistad amistad = findById(amistadId);

        if (!amistad.getDestinatario().getIdUsuario().equals(destinatarioId)) {
            throw new IllegalArgumentException("No autorizado para aceptar esta solicitud");
        }

        if (amistad.getEstado() != EstadoPeticion.PENDIENTE) {
            throw new IllegalArgumentException("La solicitud no está pendiente");
        }

        amistad.setEstado(EstadoPeticion.ACEPTADO);
        save(amistad);
    }

    /**
     * Rechazar una peticion de amistas
     * comprueba que la peticion existe y esta pendiente
     */
    @Transactional
    public void rejectFriendRequest(Long amistadId, Long destinatarioId) {
        Amistad amistad = findById(amistadId);

        if (!amistad.getDestinatario().getIdUsuario().equals(destinatarioId)) {
            throw new IllegalArgumentException("No autorizado para rechazar esta solicitud");
        }

        if (amistad.getEstado() != EstadoPeticion.PENDIENTE) {
            throw new IllegalArgumentException("La solicitud no está pendiente");
        }

        amistad.setEstado(EstadoPeticion.RECHAZADA);
        save(amistad);
    }
}
