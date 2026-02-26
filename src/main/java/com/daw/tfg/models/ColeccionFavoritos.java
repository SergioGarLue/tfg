package com.daw.tfg.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "idColeccionJuego")
@Table(name = "coleccion_favoritos")
public class ColeccionFavoritos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_coleccion_juego")
    private Long idColeccionJuego;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_juego", nullable = false)
    @JsonIgnore
    private Juego juego;

    @Column(nullable = false)
    private Boolean esFavorito = false;

    @Column(nullable = false)
    private Date fechaAdquisicion;

    public ColeccionFavoritos(Usuario usuario, Juego juego, Date fechaAdquisicion) {
        this.usuario = usuario;
        this.juego = juego;
        this.fechaAdquisicion = fechaAdquisicion;
        this.esFavorito = false;
    }

    public ColeccionFavoritos(Usuario usuario, Juego juego, Boolean esFavorito, Date fechaAdquisicion) {
        this.usuario = usuario;
        this.juego = juego;
        this.esFavorito = esFavorito;
        this.fechaAdquisicion = fechaAdquisicion;
    }
}
