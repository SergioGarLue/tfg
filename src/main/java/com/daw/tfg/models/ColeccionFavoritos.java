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
@Getter @Setter @NoArgsConstructor @ToString
@Table(name = "coleccion_favoritos")
public class ColeccionFavoritos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_coleccion")
    private Long idColeccionJuego;

    @ManyToOne
    @JoinColumn(name = "id_coleccion", nullable = false, insertable = false, updatable = false)
    @JsonIgnore
    private Coleccion coleccion;

    @ManyToOne
    @JoinColumn(name = "id_juego", nullable = false)
    @JsonIgnore
    private Juego juego;

    @Column(nullable = false)
    private Boolean esFavorito = false;

    public ColeccionFavoritos(Coleccion coleccion, Juego juego, Boolean esFavorito) {
        this.coleccion = coleccion;
        this.juego = juego;
        this.esFavorito = esFavorito;
    }
}
