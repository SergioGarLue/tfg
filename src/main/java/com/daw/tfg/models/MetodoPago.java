package com.daw.tfg.models;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.daw.tfg.Enums.TipoMetodoPago;
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

    
    @Column(unique = true, nullable = false, name = "proveedor")
    private String proveedor;

    //Pasa el metodo de pago recogido de un enum como string a la BD
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "tipo")
    private TipoMetodoPago tipo;

    /*
        Almacena el token del metodo de pago para no almacenar datos sensibles como el CVV
        o la contraseña en PayPal, haciendo seguras las compras mediante una pasarela de pago
    */
    @Column(unique = true, nullable = false, name = "token")
    private String token;

    //solo para tarjetas de credito almacena los 4 ultimos digitos de esta 
    @Column(nullable = true, length = 4, name = "ultimos_digitos")
    private String ultimosDigitos;

    //solo para tarjetas almacena la fecha en la que expira la tarjeta 
    @Column(nullable = false, name = "fecha_expiracion")
    private Date fechaExpiracion;

    @Column(nullable = false, name = "activo")
    private Boolean activo;

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

    public MetodoPago(String proveedor, TipoMetodoPago tipo, String token, String ultimosDigitos, Date fechaExpiracion,
            Boolean activo, Usuario usuario, Set<Compra> compras) {
        this.proveedor = proveedor;
        this.tipo = tipo;
        this.token = token;
        this.ultimosDigitos = ultimosDigitos;
        this.fechaExpiracion = fechaExpiracion;
        this.activo = activo;
        this.usuario = usuario;
        this.compras = compras;
    }

    
}
