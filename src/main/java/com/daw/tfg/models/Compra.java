package com.daw.tfg.models;

import java.util.Date;

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

@Entity
@Getter @Setter @NoArgsConstructor
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
}
