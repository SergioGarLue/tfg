package com.daw.tfg.models;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    
}
