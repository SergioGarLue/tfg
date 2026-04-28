package com.daw.tfg.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daw.tfg.enums.TipoMetodoPago;
import com.daw.tfg.models.MetodoPago;
import com.daw.tfg.models.Usuario;
import com.daw.tfg.service.MetodoPagoService;
import com.daw.tfg.service.UsuarioService;

/**
 * Controladora REST para la gestión de métodos de pago.
 * Proporciona endpoints para operaciones CRUD sobre los métodos de pago de los usuarios.
 */
@RestController
@RequestMapping("/api/metodopago")
public class ControladoraMetodoPago {

    private final MetodoPagoService metodoPagoService;
    private final UsuarioService usuarioService;

    public ControladoraMetodoPago(MetodoPagoService metodoPagoService, UsuarioService usuarioService) {
        this.metodoPagoService = metodoPagoService;
        this.usuarioService = usuarioService;
    }

    /**
     * Obtiene todos los métodos de pago disponibles.
     * 
     * @return Lista de todos los métodos de pago
     */
    @GetMapping
    public List<MetodoPago> getAll() {
        return metodoPagoService.findAll();
    }

    /**
     * Obtiene un método de pago específico por su ID.
     * 
     * @param id Identificador del método de pago
     * @return El método encontrado o error 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<MetodoPago> getById(@PathVariable Long id) {
        try {
            MetodoPago metodoPago = metodoPagoService.findById(id);
            return ResponseEntity.ok(metodoPago);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene todos los métodos de pago de un usuario específico.
     * 
     * @param usuarioId Identificador del usuario
     * @return Lista de métodos de pago del usuario o 204 si no tiene ninguno
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<MetodoPago>> getMetodosPagoByUsuario(@PathVariable Long usuarioId) {
        try {
            Usuario usuario = usuarioService.findById(usuarioId);
            List<MetodoPago> metodos = metodoPagoService.findByUsuario(usuario);
            if (metodos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(metodos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene los métodos de pago de un tipo específico.
     * 
     * @param tipo Tipo de método de pago (TARJETA_CREDITO, TARJETA_DEBITO, PAYPAL, etc.)
     * @return Lista de métodos de pago del tipo especificado o 204 si no hay
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<MetodoPago>> getMetodosPagoByTipo(@PathVariable String tipo) {
        try {
            TipoMetodoPago tipoEnum = TipoMetodoPago.valueOf(tipo.toUpperCase());
            List<MetodoPago> metodos = metodoPagoService.findByTipo(tipoEnum);
            if (metodos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(metodos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Obtiene los métodos de pago activos o inactivos según el parámetro.
     * 
     * @param activo True para obtener métodos activos, false para inactivos
     * @return Lista de métodos de pago o 204 si no hay
     */
    @GetMapping("/activos/{activo}")
    public ResponseEntity<List<MetodoPago>> getMetodosPagoByActivo(@PathVariable Boolean activo) {
        try {
            List<MetodoPago> metodos = metodoPagoService.findByActivo(activo);
            if (metodos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(metodos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Crea un nuevo método de pago.
     * 
     * @param metodoPago Datos del método de pago a crear
     * @return Método de pago creado con status 201 o error 400 si hay validación
     */
    @PostMapping
    public ResponseEntity<MetodoPago> create(@RequestBody MetodoPago metodoPago) {
        try {
            MetodoPago created = metodoPagoService.save(metodoPago);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualiza un método de pago existente.
     * 
     * @param id Identificador del método de pago a actualizar
     * @param metodoPago Nuevos datos del método de pago
     * @return Método de pago actualizado o error 404 si no existe
     */
    @PostMapping("/{id}")
    public ResponseEntity<MetodoPago> update(@PathVariable Long id, @RequestBody MetodoPago metodoPago) {
        try {
            metodoPagoService.findById(id); // Verificar que existe
            metodoPago.setIdMetodoPago(id);
            MetodoPago updated = metodoPagoService.save(metodoPago);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Elimina un método de pago.
     * 
     * @param id Identificador del método de pago a eliminar
     * @return Respuesta 200 si se elimina correctamente, 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            metodoPagoService.deleteById(id);
            return ResponseEntity.ok("Método de pago eliminado correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
