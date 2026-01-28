package com.daw.tfg.models;

import java.util.Date;

import com.daw.tfg.Enums.EstadoCompra;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @ToString
@Table(name = "compra")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCompra;

    @Column(nullable = false)
    private Date fechaCompra;

    @Column(nullable = false)
    private Double total;

    @Column(nullable = false)
    private EstadoCompra estado;
    
    //Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_metodo_pago", nullable = false)
    @JsonIgnore
    private MetodoPago metodoPago;
    
    @OneToOne
    @JoinColumn(name = "id_carrito", nullable = false)
    private Carrito carrito;

    public Compra(Date fechaCompra, Double total, EstadoCompra estado, Usuario usuario, MetodoPago metodoPago, Carrito carrito) {
        this.fechaCompra = fechaCompra;
        this.total = total;
        this.estado = estado;
        this.usuario = usuario;
        this.metodoPago = metodoPago;
        this.carrito = carrito;
    }
}
