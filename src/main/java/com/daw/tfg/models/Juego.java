package com.daw.tfg.models;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
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
    private Long idJuego;

    @Column(nullable = false, unique = true, name = "titulo")
    private String titulo;

    @Column(nullable = false, name = "precio")
    private Float precio;

    @Column(length = 1000, name = "descripcion")
    private String descripcion;

    @Column(nullable = false, name = "fechaLanzamiento")
    private LocalDateTime fechaLanzamiento;

    @Column(nullable = false, name = "requerimientos")
    private String requerimientos;

    @Column(nullable = false, name = "imagen")
    private String imagen;

    @OneToMany(mappedBy = "juego")
    @JsonIgnore
    private Set<Resena> resenas;

    @OneToMany(mappedBy = "juego")
    @JsonIgnore
    private Set<ContenidoAdicional> contenidosAdicionales;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "juego_genero",
        joinColumns = @JoinColumn(name = "id_juego"),
        inverseJoinColumns = @JoinColumn(name = "id_genero")
    )
    @JsonIgnore
    private Set<Genero> generos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_desarrollador", nullable = false)
    @JsonIgnore
    private Desarrollador desarrollador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_editor", nullable = false)
    @JsonIgnore
    private Editor editor;

    public Juego(String titulo, Float precio, String descripcion, LocalDateTime fechaLanzamiento, String requerimientos, String imagen,
        Set<Resena> resenas, Set<ContenidoAdicional> contenidosAdicionales,
            Set<Genero> generos, Desarrollador desarrollador, Editor editor) {
        this.titulo = titulo;
        this.precio = precio;
        this.descripcion = descripcion;
        this.fechaLanzamiento = fechaLanzamiento;
        this.requerimientos = requerimientos;
        this.imagen = imagen;
        this.resenas = resenas;
        this.contenidosAdicionales = contenidosAdicionales;
        this.generos = generos;
        this.desarrollador = desarrollador;
        this.editor = editor;
    }
}
