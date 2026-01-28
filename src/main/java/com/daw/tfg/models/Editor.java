package com.daw.tfg.models;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "editor")
public class Editor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_editor;

    @Column(nullable = false, unique = true, name = "nombre")
    private String nombre;

    @Column(nullable = false, unique = true, name = "imagen")
    private String imagen;

    // Relaciones
    @OneToMany(mappedBy = "editor")
    @JsonIgnore
    private List<Contenido_Adicional> contenidosAdicionales;

    @OneToMany(mappedBy = "editor")
    @JsonIgnore
    private List<Juego> juego;

    public Editor(Long id_editor, String nombre, String imagen, List<Contenido_Adicional> contenidosAdicionales,
            List<com.daw.tfg.models.Juego> juego) {
        this.id_editor = id_editor;
        this.nombre = nombre;
        this.imagen = imagen;
        this.contenidosAdicionales = contenidosAdicionales;
        this.juego = juego;
    }

    
}