package com.daw.tfg.models;

import java.util.Date;

import com.daw.tfg.Enums.EstadoCompra;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
    private Long id_compra;

    @Column(nullable = false)
    private Date fecha_compra;

    @Column(nullable = false)
    private Double total;

    @Column(nullable = false)
    private EstadoCompra estado;

    @OneToMany(mappedBy = "Usuario")
    @JsonIgnore
    private Usuario id_Usuario;

    @OneToMany(mappedBy = "metodo_pago")
    @JsonIgnore
    private MetodoPago id_metodo_pago;

    public Compra(Date fecha_compra, Double total, EstadoCompra estado, Usuario id_Usuario, MetodoPago id_metodo_pago, Long id_compra) {
        this.fecha_compra = fecha_compra;
        this.total = total;
        this.estado = estado;
        this.id_Usuario = id_Usuario;
        this.id_metodo_pago = id_metodo_pago;
        this.id_compra = id_compra;
    }
}
