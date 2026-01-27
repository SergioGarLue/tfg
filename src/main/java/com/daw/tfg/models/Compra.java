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
    private Long id;

    @Column(nullable = false)
    private Date fecha_compra;

    @Column(nullable = false)
    private Double total;

    @Column(nullable = false)
    private EstadoCompra estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_metodo_pago", nullable = false)
    @JsonIgnore
    private MetodoPago metodoPago;
    
    public Compra(Date fecha_compra, Double total, EstadoCompra estado, Usuario usuario, MetodoPago metodoPago) {
        this.fecha_compra = fecha_compra;
        this.total = total;
        this.estado = estado;
        this.usuario = usuario;
        this.metodoPago = metodoPago;
    }
}
