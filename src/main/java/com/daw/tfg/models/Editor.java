package com.daw.tfg.models;

import java.util.Set;

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
    private Long idEditor;

    @Column(nullable = false, unique = true, name = "nombre")
    private String nombre;

    @Column(nullable = false, unique = true, name = "imagen")
    private String imagen;

    // Relaciones
    // @OneToMany(mappedBy = "editor")
    // @JsonIgnore
    // private List<Contenido_Adicional> contenidosAdicionales;

    @OneToMany(mappedBy = "editor")
    @JsonIgnore
    private Set<Juego> juego;

    public Editor(Long idEditor, String nombre, String imagen, 
        // List<Contenido_Adicional> contenidosAdicionales,
            Set<com.daw.tfg.models.Juego> juego) {
        this.idEditor = idEditor;
        this.nombre = nombre;
        this.imagen = imagen;
        // this.contenidosAdicionales = contenidosAdicionales;
        this.juego = juego;
    }

    
}