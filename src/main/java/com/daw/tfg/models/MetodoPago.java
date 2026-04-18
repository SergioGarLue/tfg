package com.daw.tfg.models;

import java.util.Date;
import java.util.Set;

import com.daw.tfg.enums.ProveedorMetodoPago;
import com.daw.tfg.enums.TipoMetodoPago;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "metodo_pago")
@Getter @Setter @NoArgsConstructor @ToString
public class MetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMetodoPago;

    // Proveedor de la pasarela de pago
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "proveedor")
    private ProveedorMetodoPago proveedor;

    // Tipo de método de pago
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "tipo")
    private TipoMetodoPago tipo;

    /*
        Referencia externa segura proporcionada por la pasarela (ej. paymentMethodId de Stripe)
        No almacena datos sensibles como tokens o números de tarjeta.
    */
    @Column(nullable = true, name = "referencia_externa")
    private String referenciaExterna;

    // Descripción opcional para identificar el método (ej. "**** 1234")
    @Column(nullable = true, name = "descripcion")
    private String descripcion;

    @Column(nullable = false, name = "activo")
    private Boolean activo;

    // Fecha de creación o última actualización
    @Column(nullable = false, name = "fecha_creacion")
    private Date fechaCreacion;

    /*
        Relacion que enlaza los metodos de pago con un usuario
        dado que un Usuario puede tener multiples metodos diferentes
    */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    /*
        Relacion que enlaza los metodos de pago con las compras
        dado que los metodos de pago pueden ser utilizados en multiples compras
    */
    @OneToMany(mappedBy = "metodoPago", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Compra> compras;

    public MetodoPago(ProveedorMetodoPago proveedor, TipoMetodoPago tipo, String referenciaExterna,
                      String descripcion, Boolean activo, Usuario usuario) {
        this.proveedor = proveedor;
        this.tipo = tipo;
        this.referenciaExterna = referenciaExterna;
        this.descripcion = descripcion;
        this.activo = activo;
        this.fechaCreacion = new Date();
        this.usuario = usuario;
    }
}
