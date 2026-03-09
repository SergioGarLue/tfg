package com.daw.tfg.models;

import java.time.LocalDateTime;
import java.util.Set;

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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "juego")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Juego {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "juego_genero", joinColumns = @JoinColumn(name = "id_juego"), inverseJoinColumns = @JoinColumn(name = "id_genero"))
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

    // determina si un juego es contenido adicional(bolean), ademas da la id del juego padre,
    // esta parte no es nullable = false ya que puede ser nulo.

    @Column(name = "tipo")
    private String tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_juego_padre")
    @JsonIgnore
    private Juego juegoPadre;

    public Juego(Desarrollador desarrollador, String descripcion,
         Editor editor, LocalDateTime fechaLanzamiento, Set<Genero> generos,
         Long idJuego, String imagen, Juego juegoPadre, Float precio, String requerimientos,
          Set<Resena> resenas, String tipo, String titulo) {
            
        this.desarrollador = desarrollador;
        this.descripcion = descripcion;
        this.editor = editor;
        this.fechaLanzamiento = fechaLanzamiento;
        this.generos = generos;
        this.idJuego = idJuego;
        this.imagen = imagen;
        this.juegoPadre = juegoPadre;
        this.precio = precio;
        this.requerimientos = requerimientos;
        this.titulo = titulo;
    }

    
}
