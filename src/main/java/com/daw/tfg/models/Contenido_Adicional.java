package com.daw.tfg.models;

import java.sql.Date;
import java.util.List;

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

@Entity
@Table(name = "contenido_adicional")
@Getter @Setter @NoArgsConstructor
public class Contenido_Adicional {
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
    private List<Resena> reviews;

    @ManyToOne()
    @JoinColumn(name = "id_juego")
    @Column(name = "contenidos_adicionales")
    private List<Contenido_Adicional> contenidos_adicionales;

    @ManyToMany(mappedBy = "generos")
    @JoinColumn(name = "id_genero")
    private List<Genero> genero;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(nullable = false, name = "desarrollador")
    private String desarrollador;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(nullable = false, name = "editor")
    private String editor;

    public Contenido_Adicional(String titulo, Float precio, String descripcion, Date fechaLanzamiento, Float pesoGb,
            String imagen, String requistos, List<Resena> reviews, List<Contenido_Adicional> contenidos_adicionales,
            List<Genero> genero, String desarrollador, String editor) {
        this.titulo = titulo;
        this.precio = precio;
        this.descripcion = descripcion;
        this.fechaLanzamiento = fechaLanzamiento;
        this.pesoGb = pesoGb;
        this.imagen = imagen;
        this.requistos = requistos;
        this.reviews = reviews;
        this.contenidos_adicionales = contenidos_adicionales;
        this.genero = genero;
        this.desarrollador = desarrollador;
        this.editor = editor;
    }
}
