package com.daw.tfg.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "generos")
@Getter @Setter @NoArgsConstructor
public class Genero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, name = "nombre")
    private String nombre;

    @ManyToMany(mappedBy = "genero_juegos")
    @JoinColumn(name = "id_juego")
    private List<Juego> juegos;

    public Genero(String nombre, List<Juego> juegos) {
        this.nombre = nombre;
        this.juegos = juegos;
    }
}


