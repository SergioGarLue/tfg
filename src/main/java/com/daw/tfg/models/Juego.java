package com.daw.tfg.models;

import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "juegos")
@Getter @Setter @NoArgsConstructor @ToString
public class Juego {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, name = "titulo")
    private String titulo;

    @Column(nullable = false, name = "precio")
    private Float precio;

    @Column(length = 1000, name = "descripcion")
    private String descripcion;

    @Column(nullable = false, name = "fechaLanzamiento")
    private Date fechaLanzamiento;

    @Column(nullable = false, name = "pesoGb")
    private Float pesoGb;

    @Column(nullable = false, name = "imagen")
    private String imagen;
    
    @Column(nullable = false, name = "requistos")
    private String requistos;

    @OneToMany(mappedBy = "review")
    @Column(name = "review")
    @JsonIgnore
    private List<Resena> reviews;

    @OneToMany(mappedBy = "contenido_adicional")
    @Column(name = "contenido_adicional")
    @JsonIgnore
    private List<Contenido_Adicional> contenido_adicional;

    @ManyToMany(mappedBy = "genero_juegos")
    @JoinColumn(name = "id_genero")
    @JsonIgnore
    private List<Genero> genero;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(nullable = false, name = "desarrollador")
    @JsonIgnore
    private Desarrollador desarrollador;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(nullable = false, name = "editor")
    @JsonIgnore
    private Editor editor;

    public Juego(String titulo, Float precio, String descripcion, Date fechaLanzamiento, Float pesoGb, String imagen,
            String requistos, List<Resena> reviews, List<Contenido_Adicional> contenido_adicional,
            List<Genero> genero, Desarrollador desarrollador, Editor editor) {
        this.titulo = titulo;
        this.precio = precio;
        this.descripcion = descripcion;
        this.fechaLanzamiento = fechaLanzamiento;
        this.pesoGb = pesoGb;
        this.imagen = imagen;
        this.requistos = requistos;
        this.reviews = reviews;
        this.contenido_adicional = contenido_adicional;
        this.genero = genero;
        this.desarrollador = desarrollador;
        this.editor = editor;
    }

}
