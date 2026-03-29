package com.daw.tfg.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import com.daw.tfg.enums.EstadoCompra;
import com.daw.tfg.models.Carrito;
import com.daw.tfg.models.Compra;
import com.daw.tfg.service.CompraServiceImpl;
import com.daw.tfg.models.Juego;
import com.daw.tfg.models.MetodoPago;
import com.daw.tfg.models.Usuario;
import com.daw.tfg.repository.CarritoRepository;

@Service
public class CarritoService {

    private final CompraServiceImpl compraServiceImpl;
    private final CarritoRepository carritoRepository;
    private final UsuarioService usuarioService;
    private final JuegoService juegoService;
    private final MetodoPagoService metodoPagoService;

    public CarritoService(CarritoRepository carritoRepository, UsuarioService usuarioService, JuegoService juegoService,
            MetodoPagoService metodoPagoService, CompraServiceImpl compraServiceImpl) {
        this.carritoRepository = carritoRepository;
        this.usuarioService = usuarioService;
        this.juegoService = juegoService;
        this.metodoPagoService = metodoPagoService;
        this.compraServiceImpl = compraServiceImpl;
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
     * Obtiene todos los juegos en el carrito de un usuario.
     * 
     * @param usuarioId ID del usuario
     * @return Lista de juegos en el carrito
     * @throws IllegalArgumentException si el usuario no tiene carrito
     */
    public List<Juego> getAllGamesInCart(Long usuarioId) {
        Usuario usuario = usuarioService.findById(usuarioId);
        Carrito carrito = findByUsuario(usuario);

        if (carrito.getJuegos() == null || carrito.getJuegos().isEmpty()) {
            return List.of();
        }

        return List.copyOf(carrito.getJuegos());
    }

    /**
     * Añade el juego al carrito del usuario comprobando que el usuario existe.
     * Si el carrito no existe, crea uno nuevo asociado a ese usuario.
     * 
     * @param usuarioId ID del usuario
     * @param juegoId   ID del juego a añadir
     * @throws IllegalArgumentException si el usuario o juego no existen, o si el
     *                                  juego ya está en el carrito
     */
    @Transactional
    public void addJuegoToCarrito(Long usuarioId, Long juegoId) {
        Usuario usuario = usuarioService.findById(usuarioId);
        Juego juego = juegoService.findById(juegoId);

        // 1. Intentamos buscar el carrito existente o creamos uno nuevo si no existe
        Carrito carrito = carritoRepository.findByUsuario(usuario)
                .orElseGet(() -> {
                    Carrito nuevoCarrito = new Carrito();
                    nuevoCarrito.setUsuario(usuario);
                    nuevoCarrito.setJuegos(new HashSet<>()); // Inicializar el Set para evitar NullPointerException
                    return nuevoCarrito;
                });

        // Asegurar que el carrito tiene el usuario asignado (por si se recuperó de BD)
        if (carrito.getUsuario() == null) {
            carrito.setUsuario(usuario);
        }

        // Inicializar juegos si es null (por si el carrito venía de BD sin inicializar)
        if (carrito.getJuegos() == null) {
            carrito.setJuegos(new HashSet<>());
        }

        carrito.setCompra(null);

        // 2. Verificamos si el juego ya está en el carrito
        if (carrito.getJuegos().contains(juego)) {
            throw new IllegalArgumentException("El juego ya está en el carrito");
        }

        // 3. Añadimos el juego a la colección
        carrito.getJuegos().add(juego);

        // 4. Guardamos el carrito
        save(carrito);
    }

    /**
     * Elimina un juego del carrito del usuario.
     * Verifica que el juego exista en el carrito antes de eliminarlo.
     * 
     * @param usuarioId ID del usuario
     * @param juegoId   ID del juego a eliminar
     * @throws IllegalArgumentException si el usuario no tiene carrito o el juego no
     *                                  está en el carrito
     */
    @Transactional
    public void removeJuegoFromCarrito(Long usuarioId, Long juegoId) {
        Usuario usuario = usuarioService.findById(usuarioId);
        Juego juego = juegoService.findById(juegoId);

        Carrito carrito = findByUsuario(usuario);

        // Verificar que el carrito tenga juegos inicializados
        if (carrito.getJuegos() == null) {
            throw new IllegalStateException("Carrito vacio");
        }

        // Verificar que el juego esté en el carrito antes de intentar eliminarlo
        if (!carrito.getJuegos().contains(juego)) {
            throw new IllegalArgumentException("El juego no está en el carrito");
        }

        carrito.getJuegos().remove(juego);
        save(carrito);
    }

    /**
     * Calcula el precio total del carrito de un usuario.
     * 
     * @param usuarioId ID del usuario
     * @return Precio total del carrito
     * @throws IllegalArgumentException si el usuario no tiene carrito
     */
    public Float getTotalPrice(Long usuarioId) {
        Usuario usuario = usuarioService.findById(usuarioId);
        Carrito carrito = findByUsuario(usuario);

        // Si el carrito no tiene juegos o está vacío, retornar 0
        if (carrito.getJuegos() == null || carrito.getJuegos().isEmpty()) {
            return 0.0f;
        }

        // Calcular total filtrando juegos sin precio para evitar NullPointerException
        return (float) carrito.getJuegos().stream()
                .filter(juego -> juego.getPrecio() != null)
                .mapToDouble(Juego::getPrecio)
                .sum();
    }

    /**
     * Procesa el checkout del carrito: crea una compra, vacía el carrito y asocia
     * el método de pago.
     * 
     * @param usuarioId    ID del usuario
     * @param metodoPagoId ID del método de pago (puede ser null)
     * @throws IllegalArgumentException si el usuario no existe o no tiene carrito
     * @throws IllegalStateException    si el carrito está vacío o no tiene juegos
     *                                  inicializados
     */
    @Transactional
    public void checkout(Long usuarioId, Long metodoPagoId) {
        Usuario usuario = usuarioService.findById(usuarioId);
        Carrito carrito = findByUsuario(usuario);

        // Verificar que el carrito tenga juegos inicializados
        if (carrito.getJuegos() == null) {
            throw new IllegalStateException("El carrito no está correctamente inicializado");
        }

        if (carrito.getJuegos().isEmpty()) {
            throw new IllegalStateException("No se puede finalizar la compra con un carrito vacío");
        }

        // Buscar el método de pago si se proporcionó
        MetodoPago metodoPago = null;
        if (metodoPagoId != null) {
            metodoPago = metodoPagoService.findById(metodoPagoId);
        }

        Double total = getTotalPrice(usuarioId).doubleValue();

        // 1. Creamos la compra
        Compra compra = new Compra();
        compra.setTotal(total);
        compra.setEstado(EstadoCompra.PENDIENTE);
        compra.setUsuario(usuario);
        compra.setMetodoPago(metodoPago);

        // 2. Guardamos la compra primero
        compra = compraServiceImpl.save(compra);

        // 3. Limpiamos el carrito y asociamos la compra
        carrito.getJuegos().clear();
        carrito.setCompra(compra);

        save(carrito);
    }

}
