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
@Table(name = "Desarrollador")
public class Desarrollador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_desarrollador;

    @Column(nullable = false, unique = true, name = "nombre")
    private String nombre;

    @Column(nullable = false, unique = true, name = "imagen")
    private String imagen;
    
    // Relaciones
    @OneToMany(mappedBy = "Desarrollador")
    @JsonIgnore
    private List<Contenido_Adicional> contenidosAdicionales;

    @OneToMany(mappedBy = "Desarrollador")
    @JsonIgnore
    private List<Juego> Juego;

    public Desarrollador(String nombre, String imagen, List<Contenido_Adicional> contenidosAdicionales,
            List<com.daw.tfg.models.Juego> juego) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.contenidosAdicionales = contenidosAdicionales;
        Juego = juego;
    }

}
