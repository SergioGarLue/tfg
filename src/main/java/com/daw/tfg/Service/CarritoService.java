package com.daw.tfg.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daw.tfg.Repository.CarritoRepository;
import com.daw.tfg.models.Carrito;
import com.daw.tfg.models.Compra;
import com.daw.tfg.models.Juego;
import com.daw.tfg.models.MetodoPago;
import com.daw.tfg.models.Usuario;
import com.daw.tfg.Enums.EstadoCompra;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final UsuarioService usuarioService;
    private final JuegoService juegoService;
    private final CompraService compraService;

    public CarritoService(CarritoRepository carritoRepository, UsuarioService usuarioService, JuegoService juegoService,
            CompraService compraService) {
        this.carritoRepository = carritoRepository;
        this.usuarioService = usuarioService;
        this.juegoService = juegoService;
        this.compraService = compraService;
    }

    // CRUD

    public List<Carrito> findAll() {
        return carritoRepository.findAll();
    }

    public Carrito findById(Long id) {
        Optional<Carrito> carr = carritoRepository.findById(id);
        if (carr.isEmpty()) {
            throw new IllegalArgumentException("Carrito no encontrado");
        }
        return carr.get();
    }

    public Carrito findByUsuario(Usuario usuario) {
        Optional<Carrito> carr = carritoRepository.findByIdUsuario(usuario);
        if (carr.isEmpty()) {
            throw new IllegalArgumentException("Carrito no encontrado");
        }
        return carr.get();
    }

    public List<Carrito> findByJuegoContains(Juego juego) {
        return carritoRepository.findByJuegosContains(juego);
    }

    public Carrito save(Carrito carrito) {
        return carritoRepository.save(carrito);
    }

    public void deleteById(Long id) {
        if (carritoRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Carrito no encontrado");
        }
        carritoRepository.deleteById(id);
    }

    // Logica de negocio

    /**
     * Añade el jeugo al carrito del usuario comprobadno que el usuario existe
     * si carrito no existe -> crea el carrito asociado a ese user
     */
    @Transactional
    public void addJuegoToCarrito(Long usuarioId, Long juegoId) {
        Usuario usuario  = usuarioService.findById(usuarioId);

        Optional<Juego> juegoOpt = juegoService.findById(juegoId);
        if (juegoOpt.isEmpty()) {
            throw new IllegalArgumentException("Juego no encontrado");
        }
        Juego juego = juegoOpt.get();

        Carrito carrito = findByUsuario(usuario);

        if (carrito.getJuegos().contains(juego)) {
            throw new IllegalArgumentException("El juego ya está en el carrito");
        }
        carrito.getJuegos().add(juego);

        // crea un nuevo carrito si no existe ninguno
        carrito = new Carrito(usuario, Set.of(juego), Set.of(), null); // asumiendo que la compra es nula

        save(carrito);
    }

    /**
     * Eliminar juegos del carrito comprobando que el juego esta en ese carrito
     */
    @Transactional
    public void removeJuegoFromCarrito(Long usuarioId, Long juegoId) {
        Usuario usuario = usuarioService.findById(usuarioId);

        Optional<Juego> juegoOpt = juegoService.findById(juegoId);
        if (juegoOpt.isEmpty()) {
            throw new IllegalArgumentException("Juego no encontrado");
        }
        Juego juego = juegoOpt.get();

        Carrito carrito = findByUsuario(usuario);
        carrito.getJuegos().remove(juego);
        save(carrito);
    }

    /**
     * calcula el precio total del carrito
     */
    public Float getTotalPrice(Long usuarioId) {
        Usuario usuario = usuarioService.findById(usuarioId);

        Carrito carrito = findByUsuario(usuario);
        //cambio antes era un .reduce que podia dar nullpointerexception
        return (float) carrito.getJuegos().stream()
                .filter(juego -> juego.getPrecio() != null) // evitar errores con nulos
                .mapToDouble(Juego::getPrecio)
                .sum();
    }

    /**
     * prceso que crea la compra vacia el carrito y recoge el metodo de pago
     */
    @Transactional
    public void checkout(Long usuarioId, Long metodoPagoId) {
        Usuario usuario = usuarioService.findById(usuarioId);

        Carrito carrito = findByUsuario(usuario);

        // asumimos que el metodo de pago ya existe (habira que modificarlo para que se
        // compruebe)
        MetodoPago metodoPago = new MetodoPago();
        metodoPago.setIdMetodoPago(metodoPagoId);

        Float total = getTotalPrice(usuarioId);

        // crea la compra
        Compra compra = new Compra(total.doubleValue(), EstadoCompra.PENDIENTE, usuario, metodoPago, carrito);
        compraService.save(compra);

        // vacia el carrito
        carrito.getJuegos().clear();
        carrito.getContenidosAdicionales().clear();
        save(carrito);
    }
}
