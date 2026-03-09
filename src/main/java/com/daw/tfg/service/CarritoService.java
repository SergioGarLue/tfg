package com.daw.tfg.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daw.tfg.enums.EstadoCompra;
import com.daw.tfg.exception.ResourceNotFoundException;
import com.daw.tfg.exception.ValidationException;
import com.daw.tfg.models.Carrito;
import com.daw.tfg.models.Compra;
import com.daw.tfg.models.Juego;
import com.daw.tfg.models.MetodoPago;
import com.daw.tfg.models.Usuario;
import com.daw.tfg.repository.CarritoRepository;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final UsuarioService usuarioService;
    private final JuegoService juegoService;
    private final CompraService compraService;
    private final MetodoPagoService metodoPagoService;

    public CarritoService(CarritoRepository carritoRepository, UsuarioService usuarioService, JuegoService juegoService,
            CompraService compraService, MetodoPagoService metodoPagoService) {
        this.carritoRepository = carritoRepository;
        this.usuarioService = usuarioService;
        this.juegoService = juegoService;
        this.compraService = compraService;
        this.metodoPagoService = metodoPagoService;
    }

    // ==================== CRUD ====================

    public List<Carrito> findAll() {
        return carritoRepository.findAll();
    }

    public Carrito findById(Long id) {
        return carritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito", "id", id));
    }

    public Carrito findByUsuario(Usuario usuario) {
        return carritoRepository.findByUsuario(usuario)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito", "usuario", usuario.getIdUsuario()));
    }

    public Carrito findByUsuarioId(Long usuarioId) {
        Usuario usuario = usuarioService.findById(usuarioId);
        return findByUsuario(usuario);
    }

    public List<Carrito> findByJuegoContains(Juego juego) {
        return carritoRepository.findByJuegosContains(juego);
    }

    public Carrito save(Carrito carrito) {
        return carritoRepository.save(carrito);
    }

    public void deleteById(Long id) {
        if (carritoRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Carrito", "id", id);
        }
        carritoRepository.deleteById(id);
    }

    // ==================== Lógica de negocio ====================

    /**
     * Obtiene todos los juegos en el carrito de un usuario.
     */
    public List<Juego> getAllGamesInCart(Long usuarioId) {
        Carrito carrito = findByUsuarioId(usuarioId);
        
        if (carrito.getJuegos() == null || carrito.getJuegos().isEmpty()) {
            return List.of();
        }
        
        return List.copyOf(carrito.getJuegos());
    }

    /**
     * Añade el juego al carrito del usuario.
     * Si el carrito no existe, crea uno nuevo.
     */
    @Transactional
    public void addJuegoToCarrito(Long usuarioId, Long juegoId) {
        Usuario usuario = usuarioService.findById(usuarioId);
        Juego juego = juegoService.findById(juegoId);

        // Buscar carrito existente o crear uno nuevo
        Carrito carrito = carritoRepository.findByUsuario(usuario)
                .orElseGet(() -> {
                    Carrito nuevoCarrito = new Carrito();
                    nuevoCarrito.setUsuario(usuario);
                    nuevoCarrito.setJuegos(new HashSet<>());
                    return nuevoCarrito;
                });
        
        // Asegurar que el carrito tiene el usuario asignado
        if (carrito.getUsuario() == null) {
            carrito.setUsuario(usuario);
        }
        
        // Inicializar juegos si es null
        if (carrito.getJuegos() == null) {
            carrito.setJuegos(new HashSet<>());
        }
        
        carrito.setCompra(null);

        // Verificar si el juego ya está en el carrito
        if (carrito.getJuegos().contains(juego)) {
            throw new ValidationException("El juego ya está en el carrito");
        }

        // Añadir el juego
        carrito.getJuegos().add(juego);
        save(carrito);
    }

    /**
     * Elimina un juego del carrito del usuario.
     */
    @Transactional
    public void removeJuegoFromCarrito(Long usuarioId, Long juegoId) {
        Usuario usuario = usuarioService.findById(usuarioId);
        Juego juego = juegoService.findById(juegoId);

        Carrito carrito = findByUsuario(usuario);
        
        if (carrito.getJuegos() == null) {
            throw new ValidationException("Carrito vacío");
        }
        
        if (!carrito.getJuegos().contains(juego)) {
            throw new ValidationException("El juego no está en el carrito");
        }
        
        carrito.getJuegos().remove(juego);
        save(carrito);
    }

    /**
     * Calcula el precio total del carrito de un usuario.
     */
    public Float getTotalPrice(Long usuarioId) {
        Carrito carrito = findByUsuarioId(usuarioId);
        
        if (carrito.getJuegos() == null || carrito.getJuegos().isEmpty()) {
            return 0.0f;
        }
        
        return (float) carrito.getJuegos().stream()
                .filter(juego -> juego.getPrecio() != null)
                .mapToDouble(Juego::getPrecio)
                .sum();
    }

    /**
     * Procesa el checkout del carrito.
     */
    @Transactional
    public void checkout(Long usuarioId, Long metodoPagoId) {
        Carrito carrito = findByUsuarioId(usuarioId);

        if (carrito.getJuegos() == null) {
            throw new ValidationException("El carrito no está correctamente inicializado");
        }

        if (carrito.getJuegos().isEmpty()) {
            throw new ValidationException("No se puede finalizar la compra con un carrito vacío");
        }

        // Buscar el método de pago si se proporcionó
        MetodoPago metodoPago = null;
        if (metodoPagoId != null) {
            metodoPago = metodoPagoService.findById(metodoPagoId);
        }

        Double total = getTotalPrice(usuarioId).doubleValue();

        // Crear la compra
        Compra compra = new Compra();
        compra.setTotal(total);
        compra.setEstado(EstadoCompra.PENDIENTE);
        compra.setUsuario(carrito.getUsuario());
        compra.setMetodoPago(metodoPago);

        // Guardar la compra
        compra = compraService.save(compra);

        // Limpiar el carrito y asociar la compra
        carrito.getJuegos().clear();
        carrito.setCompra(compra); 
        save(carrito);
    }
}
