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

    // Seguramente sean innecesarios se puede acceder a ellos desde el juego al que
    // pertenece el DLC
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "id_desarrollador", nullable = false)
    // @JsonIgnore
    // private Desarrollador desarrollador;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "id_editor", nullable = false)
    // @JsonIgnore
    // private Editor editor;

    // Constructor
    public Contenido_Adicional(String titulo, Float precio, String descripcion, LocalDateTime fechaLanzamiento,
            Float pesoGb,
            String imagen, String requisitos, Juego juego
    // ,Desarrollador desarrollador, Editor editor
    ) {
        this.titulo = titulo;
        this.precio = precio;
        this.descripcion = descripcion;
        this.fechaLanzamiento = fechaLanzamiento;
        this.pesoGb = pesoGb;
        this.imagen = imagen;
        this.requisitos = requisitos;
        this.juego = juego;
        // this.desarrollador = desarrollador;
        // this.editor = editor;
    }
}
// Posible cuestionamiento conceptual:
// ⚠️ Las relaciones directo a Desarrollador y Editor pueden ser redundantes.
// Normalmente:

// Un Contenido_Adicional pertenece a un Juego
// El Desarrollador y Editor ya están asociados al Juego
// Pregunta: ¿Un contenido adicional puede tener un Desarrollador/Editor
// diferente al del Juego al que pertenece? Si la respuesta es no, sería más
// limpio acceder a través de juego.desarrollador y juego.editor.

// ¿Cuál es la intención del diseño? ¿Estos atributos son necesarios?