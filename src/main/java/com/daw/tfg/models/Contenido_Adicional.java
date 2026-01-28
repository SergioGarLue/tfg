package com.daw.tfg.models;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "contenido_adicional")
@Getter @Setter @NoArgsConstructor @ToString
public class Contenido_Adicional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_contenido_adicional;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_juego", nullable = false)
    @JsonIgnore
    private Juego juego;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_desarrollador", nullable = false)
    @JsonIgnore
    private Desarrollador desarrollador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_editor", nullable = false)
    @JsonIgnore
    private Editor editor;

    public Contenido_Adicional(String titulo, Float precio, String descripcion, Date fechaLanzamiento, Float pesoGb,
            String imagen, String requisitos, Juego juego, Desarrollador desarrollador, Editor editor) {
        this.titulo = titulo;
        this.precio = precio;
        this.descripcion = descripcion;
        this.fechaLanzamiento = fechaLanzamiento;
        this.pesoGb = pesoGb;
        this.imagen = imagen;
        this.requisitos = requisitos;
        this.juego = juego;
        this.desarrollador = desarrollador;
        this.editor = editor;
    }
}
// Posible cuestionamiento conceptual:
// ⚠️ Las relaciones directo a Desarrollador y Editor pueden ser redundantes. Normalmente:

// Un Contenido_Adicional pertenece a un Juego
// El Desarrollador y Editor ya están asociados al Juego
// Pregunta: ¿Un contenido adicional puede tener un Desarrollador/Editor diferente al del Juego al que pertenece? Si la respuesta es no, sería más limpio acceder a través de juego.desarrollador y juego.editor.

// ¿Cuál es la intención del diseño? ¿Estos atributos son necesarios?