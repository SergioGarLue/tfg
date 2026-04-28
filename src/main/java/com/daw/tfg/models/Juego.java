package com.daw.tfg.models;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
public class Juego {
    @Id
    private Long idJuego;

    @Column(nullable = false, unique = true, name = "titulo")
    private String titulo;

    @Column(nullable = false, name = "precio")
    private Double precio;

    @Column(name = "porcentaje")
    private Integer porcentaje;

    @Column(name = "precio_rebajado")
    private Double precioRebajado;

    @Column(nullable = false, name = "disponible")
    private Boolean disponible = true;

    @Column(length = 1000, name = "descripcion")
    private String descripcion;

    @Column(nullable = false, name = "fechaLanzamiento")
    private String fechaLanzamiento;

    @Column(name = "peso_juego")
    private Float pesoJuego;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "juego_plataforma", joinColumns = @JoinColumn(name = "id_juego"))
    @Column(name = "plataforma")
    private List<String> plataformas;

    @Column(nullable = false, name = "imagen")
    private String imagen;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "juego_screenshot", joinColumns = @JoinColumn(name = "id_juego"))
    @Column(name = "screenshot")
    private List<String> screenshots;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "juego_genero", joinColumns = @JoinColumn(name = "id_juego"), inverseJoinColumns = @JoinColumn(name = "id_genero"))
    private Set<Genero> generos;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_desarrollador", nullable = false)
    @JsonIgnore
    private Desarrollador desarrollador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_editor", nullable = true)
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
         Editor editor, String fechaLanzamiento, Float pesoJuego, List<String> plataformas, Set<Genero> generos,
         Long idJuego, String imagen, Juego juegoPadre, Double precio, Integer porcentaje,
         String tipo, String titulo) {
            
        this.desarrollador = desarrollador;
        this.descripcion = descripcion;
        this.editor = editor;
        this.fechaLanzamiento = fechaLanzamiento;
        this.pesoJuego = pesoJuego;
        this.plataformas = plataformas;
        this.generos = generos;
        this.idJuego = idJuego;
        this.imagen = imagen;
        this.juegoPadre = juegoPadre;
        this.precio = precio;
        this.porcentaje = porcentaje;
        this.tipo = tipo;
        this.titulo = titulo;
        this.disponible = true;
    }

    
}
