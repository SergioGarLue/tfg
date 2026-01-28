package com.daw.tfg.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "resena")
@Getter @Setter @NoArgsConstructor @ToString
public class Resena {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_resena;

    @Column(nullable = false, name = "valoracion")
    private Integer valoracion;

    @ManyToOne
    @JoinColumn(name = "id_juego", nullable = false)
    @JsonIgnore
    private Juego juego;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    public Resena(Integer valoracion, Juego juego, Usuario usuario) {
        this.valoracion = valoracion;
        this.juego = juego;
        this.usuario = usuario;
    }
}
