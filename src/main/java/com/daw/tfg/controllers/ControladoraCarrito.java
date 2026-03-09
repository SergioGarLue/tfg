package com.daw.tfg.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daw.tfg.dtos.CheckoutDTO;
import com.daw.tfg.models.Carrito;
import com.daw.tfg.models.Juego;
import com.daw.tfg.service.CarritoService;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controladora REST para la gestión del carrito de compras.
 */
@RestController
@RequestMapping("/api/carrito")
public class ControladoraCarrito {

    private final CarritoService carritoService;

    public ControladoraCarrito(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping
    public List<Carrito> getAll() {
        return carritoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carrito> getById(@PathVariable Long id) {
        Carrito carrito = carritoService.findById(id);
        return ResponseEntity.ok(carrito);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Carrito> getByUsuario(@PathVariable Long usuarioId) {
        // El servicio se encarga internamente de buscar el usuario
        Carrito carrito = carritoService.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(carrito);
    }

    @GetMapping("/usuario/{usuarioId}/juegos")
    public ResponseEntity<List<Juego>> getGamesInCart(@PathVariable Long usuarioId) {
        List<Juego> juegos = carritoService.getAllGamesInCart(usuarioId);
        if (juegos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(juegos);
    }

    @PostMapping("/usuario/{usuarioId}/agregar/{juegoId}")
    public ResponseEntity<String> addJuego(@PathVariable Long usuarioId, @PathVariable Long juegoId) {
        carritoService.addJuegoToCarrito(usuarioId, juegoId);
        return ResponseEntity.ok("Juego añadido al carrito correctamente");
    }

    @DeleteMapping("/usuario/{usuarioId}/eliminar/{juegoId}")
    public ResponseEntity<String> removeJuego(@PathVariable Long usuarioId, @PathVariable Long juegoId) {
        carritoService.removeJuegoFromCarrito(usuarioId, juegoId);
        return ResponseEntity.ok("Juego eliminado con éxito");
    }

    @GetMapping("/total/{usuarioId}")
    public ResponseEntity<Float> getTotal(@PathVariable Long usuarioId) {
        Float total = carritoService.getTotalPrice(usuarioId);
        return ResponseEntity.ok(total);
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@Valid @RequestBody CheckoutDTO checkoutDTO) {
        carritoService.checkout(checkoutDTO.getUsuarioId(), checkoutDTO.getMetodoPagoId());
        return ResponseEntity.ok("Compra realizada correctamente");
    }
}
