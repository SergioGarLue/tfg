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
        Optional<Carrito> carr = carritoRepository.findByUsuario(usuario);
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
        Usuario usuario = usuarioService.findById(usuarioId);

        Juego juego = juegoService.findById(juegoId)
                .orElseThrow(() -> new IllegalArgumentException("Juego no encontrado"));

        // 1. Intentamos buscar el carrito existente o creamos uno nuevo si no existe
        Carrito carrito = carritoRepository.findByUsuario(usuario)
                .orElseGet(() -> new Carrito());
        carrito.setUsuario(usuario);
        carrito.setCompra(null);

        // 2. Verificamos si el juego ya está (importante que Juego tenga
        // equals/hashCode)
        if (carrito.getJuegos().contains(juego)) {
            throw new IllegalArgumentException("El juego ya está en el carrito");
        }

        // 3. Añadimos el juego a la colección
        carrito.getJuegos().add(juego);

        // 4. Guardamos (gracias a @Transactional, a veces el save no es obligatorio,
        // pero es buena práctica para asegurar la persistencia si el carrito es nuevo)
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
        return (float) carrito.getJuegos().stream().mapToDouble(juego -> juego.getPrecio()
                                                    .doubleValue()) // Pasamos a DoubleStream
                                                    .sum();
    }

    /**
     * prceso que crea la compra vacia el carrito y recoge el metodo de pago
     */
    @Transactional
    public void checkout(Long usuarioId, Long metodoPagoId) {
        Usuario usuario = usuarioService.findById(usuarioId);
        Carrito carrito = findByUsuario(usuario);

        if (carrito.getJuegos().isEmpty()) {
            throw new IllegalStateException("No se puede finalizar la compra con un carrito vacío");
        }

        // Aquí deberías buscar el método de pago real desde su Service/Repository
        // MetodoPago metodoPago = metodoPagoService.findById(metodoPagoId);

        Double total = getTotalPrice(usuarioId).doubleValue();

        // 1. Creamos la compra
        Compra compra = new Compra();
        compra.setTotal(total);
        compra.setEstado(EstadoCompra.PENDIENTE);
        compra.setUsuario(usuario);
        // comp.setMetodoPago(metodoPago); 

        // 2. Guardamos la compra primero
        compra = compraService.save(compra);

        // 3. Limpiamos el carrito y lo desvinculamos o vinculamos a la compra si es necesario
        carrito.getJuegos().clear();
        //seguramente lo tengamos que eliminar si cambiamos contenido adicional a Juego
        carrito.getContenidosAdicionales().clear(); 
        
        // Si el carrito debe guardar la última compra realizada:
        carrito.setCompra(compra); 

        save(carrito);
    }
}
