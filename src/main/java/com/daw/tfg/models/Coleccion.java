package com.daw.tfg.models;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
    private Long idColeccion;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    @JsonIgnore
    private Usuario usuario;

    @OneToMany(mappedBy = "coleccion_favoritos")
    @JsonIgnore
    private List<Coleccion_favoritos> juegos;

    @OneToMany(mappedBy = "coleccion_dlc")
    @JsonIgnore
    private List<Contenido_Adicional> contenidosAdicionales;

    @Column(nullable = false)
    private Date fechaAdquisicion;

    public Coleccion(Usuario usuario, List<Coleccion_favoritos> juegos, List<Contenido_Adicional> contenidosAdicionales, Date fechaAdquisicion) {
        this.usuario = usuario;
        this.juegos = juegos;
        this.contenidosAdicionales = contenidosAdicionales;
        this.fechaAdquisicion = fechaAdquisicion;
    }

    

}
