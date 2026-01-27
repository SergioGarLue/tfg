package com.daw.tfg.models;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "Coleccion")
public class Coleccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_Coleccion;

    @OneToOne(mappedBy = "Usuario")
    @JsonIgnore
    private Usuario id_Usuario;

    @OneToMany(mappedBy = "Juego")
    @Column(name = "id_juego")
    private List<Juego> Juego;

    @OneToMany(mappedBy = "contenido_adicional")
    @Column(name = "id_dlc")
    private List<Contenido_Adicional> contenido_adicional;

    @Column(nullable = false)
    private Date fecha_adquision;

    // sujeto a cambios, en la tabla favoritos es un Boolean y no tiene sentido deberia de ser una lista

    @OneToMany(mappedBy = "Juego")
    @Column(name = "id_juego")
    private List<Juego> JuegoFavoritos;

    public Coleccion(Long id_Coleccion, Usuario id_Usuario, List<com.daw.tfg.models.Juego> juego,
            List<Contenido_Adicional> contenido_adicional, Date fecha_adquision,
            List<com.daw.tfg.models.Juego> juegoFavoritos) {
        this.id_Coleccion = id_Coleccion;
        this.id_Usuario = id_Usuario;
        Juego = juego;
        this.contenido_adicional = contenido_adicional;
        this.fecha_adquision = fecha_adquision;
        JuegoFavoritos = juegoFavoritos;
    }

    

}
