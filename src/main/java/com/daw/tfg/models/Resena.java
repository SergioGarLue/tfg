package com.daw.tfg.models;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "resenas")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DecimalMin(value = "1.00", message = "La nota mínima es 1")
    @DecimalMax(value = "10.00", message = "La nota máxima es 10")
    @Digits(integer = 2, fraction = 2, message = "La valoración debe tener máximo 2 decimales")
    @Column(nullable = false, name = "valoracion", precision = 4, scale = 2)
    private BigDecimal valoracion; 

    @ManyToOne
    @JoinColumn(name = "id_juego", nullable = false)
    @JsonIgnore
    private Juego juego;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    public Resena(BigDecimal valoracion, Juego juego, Usuario usuario) {
        this.valoracion = valoracion;
        this.juego = juego;
        this.usuario = usuario;
    }
}
