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
    private Long id_coleccion;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    @JsonIgnore
    private Usuario usuario;

    @OneToMany(mappedBy = "coleccion")
    @JsonIgnore
    private List<Coleccion_favoritos> juegos;

    @OneToMany(mappedBy = "coleccion")
    @JsonIgnore
    private List<Contenido_Adicional> contenidosAdicionales;

    @Column(nullable = false)
    private Date fecha_adquisicion;

    public Coleccion(Usuario usuario, List<Coleccion_favoritos> juegos, List<Contenido_Adicional> contenidosAdicionales, Date fecha_adquisicion) {
        this.usuario = usuario;
        this.juegos = juegos;
        this.contenidosAdicionales = contenidosAdicionales;
        this.fecha_adquisicion = fecha_adquisicion;
    }

    

}
