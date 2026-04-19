package com.daw.tfg.service;

import com.daw.tfg.dtos.CompraDTO;
import com.daw.tfg.enums.EstadoCompra;
import com.daw.tfg.exceptions.BadRequestException;
import com.daw.tfg.exceptions.ResourceNotFoundException;
import com.daw.tfg.mappers.DtoMapper;
import com.daw.tfg.models.Carrito;
import com.daw.tfg.models.Compra;
import com.daw.tfg.models.Juego;
import com.daw.tfg.models.Usuario;
import com.daw.tfg.repository.CarritoRepository;
import com.daw.tfg.repository.CompraRepository;
import com.daw.tfg.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompraServiceImpl implements ICompraService {

    private final CompraRepository compraRepository;
    private final UsuarioRepository usuarioRepository;
    private final CarritoRepository carritoRepository;
    private final ColeccionService coleccionService;

    /**
     * @deprecated Este método procesa la compra inmediatamente y entrega juegos.
     *             Para pagos con pasarelas como Stripe, usar checkout y esperar confirmación vía webhook.
     */
    @Deprecated
    @Override
    @Transactional
    public CompraDTO procesarCompra(Long usuarioId) {
        // Buscar usuario
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + usuarioId));

        // Obtener carrito y juegos
        Carrito carrito = carritoRepository.findByUsuario(usuario)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado para usuario: " + usuarioId));
        List<Juego> juegos = carrito.getJuegos().stream().toList();

        // Validar carrito no vacío
        if (juegos.isEmpty()) {
            throw new BadRequestException("El carrito está vacío. No se puede procesar la compra.");
        }

        // Calcular total
        Double total = juegos.stream()
                .mapToDouble(Juego::getPrecio)
                .sum();

        // Crear compra
        Compra compra = new Compra();
        compra.setFechaCompra(LocalDateTime.now());
        compra.setTotal(total);
        compra.setEstado(EstadoCompra.COMPLETADA);
        compra.setUsuario(usuario);

        // Añadir cada juego a la colección (falla -> rollback)
        for (Juego juego : juegos) {
            coleccionService.addJuegoToCollection(usuarioId, juego.getIdJuego());
        }

        // Guardar compra
        compra = compraRepository.save(compra);

        // Establecer relación mutual con carrito para historial verídico
        compra.setCarrito(carrito);
        carrito.setCompra(compra);
        carritoRepository.save(carrito);

        // Vaciar juegos del carrito
        carrito.getJuegos().clear();
        carritoRepository.save(carrito);

        return DtoMapper.toCompraDTO(compra);
    }

    @Override
    public List<CompraDTO> getHistorialPorUsuario(Long usuarioId) {
        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        List<Compra> compras = compraRepository.findByUsuarioIdUsuario(usuarioId);
        return DtoMapper.toCompraDTOList(compras);
    }

    @Override
    public CompraDTO getDetalleCompra(Long compraId) {
        Compra compra = compraRepository.findById(compraId)
                .orElseThrow(() -> new ResourceNotFoundException("Compra no encontrada con ID: " + compraId));
        return DtoMapper.toCompraDTO(compra);
    }

    // CRUD básico mantenido opcional
    public Compra save(Compra compra) {
        return compraRepository.save(compra);
    }

    @Transactional
    public Compra actualizarEstadoCompraPorPaymentIntent(String paymentIntentId, EstadoCompra estado) {
        if (paymentIntentId == null || paymentIntentId.isBlank()) {
            throw new BadRequestException("paymentIntentId es obligatorio para actualizar el estado de la compra");
        }

        Compra compra = compraRepository.findByPaymentIntentId(paymentIntentId)
                .orElseThrow(() -> new ResourceNotFoundException("Compra no encontrada para paymentIntentId: " + paymentIntentId));

        if (compra.getEstado() == EstadoCompra.COMPLETADA) {
            return compra;
        }

        if (estado == EstadoCompra.COMPLETADA) {
            entregarJuegosDeCompra(compra);
        }

        compra.setEstado(estado);
        return compraRepository.save(compra);
    }

    private void entregarJuegosDeCompra(Compra compra) {
        if (compra == null || compra.getUsuario() == null || compra.getCarrito() == null) {
            throw new BadRequestException("Compra inválida para entregar juegos");
        }

        List<Juego> juegos = compra.getCarrito().getJuegos() == null
                ? List.of()
                : compra.getCarrito().getJuegos().stream().toList();

        for (Juego juego : juegos) {
            coleccionService.addJuegoToCollectionIfAbsent(compra.getUsuario().getIdUsuario(), juego.getIdJuego(), null);
        }
    }
}

