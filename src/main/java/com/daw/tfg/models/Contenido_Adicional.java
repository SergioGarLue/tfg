package com.daw.tfg.models;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "contenido_adicional")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Contenido_Adicional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContenidoAdicional;

    @Column(nullable = false, unique = true, name = "titulo")
    private String titulo;

    @Column(nullable = false, name = "precio")
    private Float precio;

    @Column(length = 1000, name = "descripcion")
    private String descripcion;

    @Column(nullable = false, name = "fechaLanzamiento")
    private LocalDateTime fechaLanzamiento;

    @Column(nullable = false, name = "pesoGb")
    private Float pesoGb;

    @Column(nullable = false, name = "imagen")
    private String imagen;

    @Column(nullable = false, name = "requisitos")
    private String requisitos;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_juego", nullable = false)
    @JsonIgnore
    private Juego juego;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carrito")
    @JsonIgnore
    private Carrito carrito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_coleccion")
    @JsonIgnore
    private Coleccion coleccion;

    // Constructor
    public Contenido_Adicional(String titulo, Float precio, String descripcion, LocalDateTime fechaLanzamiento,
            Float pesoGb,
            String imagen, String requisitos, Juego juego, Carrito carrito) {
        this.titulo = titulo;
        this.precio = precio;
        this.descripcion = descripcion;
        this.fechaLanzamiento = fechaLanzamiento;
        this.pesoGb = pesoGb;
        this.imagen = imagen;
        this.requisitos = requisitos;
        this.juego = juego;
        this.carrito = carrito;
    }
}
