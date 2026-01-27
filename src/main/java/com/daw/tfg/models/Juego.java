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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "juego")
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
    
    @Column(nullable = false, name = "requisitos")
    private String requisitos;

    @OneToMany(mappedBy = "juego")
    @JsonIgnore
    private List<Resena> resenas;

    @OneToMany(mappedBy = "juego")
    @JsonIgnore
    private List<Contenido_Adicional> contenidosAdicionales;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "juego_genero",
        joinColumns = @JoinColumn(name = "id_juego"),
        inverseJoinColumns = @JoinColumn(name = "id_genero")
    )
    @JsonIgnore
    private List<Genero> generos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_desarrollador", nullable = false)
    @JsonIgnore
    private Desarrollador desarrollador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_editor", nullable = false)
    @JsonIgnore
    private Editor editor;

    public Juego(String titulo, Float precio, String descripcion, Date fechaLanzamiento, Float pesoGb, String imagen,
            String requisitos, List<Resena> resenas, List<Contenido_Adicional> contenidosAdicionales,
            List<Genero> generos, Desarrollador desarrollador, Editor editor) {
        this.titulo = titulo;
        this.precio = precio;
        this.descripcion = descripcion;
        this.fechaLanzamiento = fechaLanzamiento;
        this.pesoGb = pesoGb;
        this.imagen = imagen;
        this.requisitos = requisitos;
        this.resenas = resenas;
        this.contenidosAdicionales = contenidosAdicionales;
        this.generos = generos;
        this.desarrollador = desarrollador;
        this.editor = editor;
    }
}
