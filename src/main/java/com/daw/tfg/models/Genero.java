package com.daw.tfg.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "genero")
@Getter @Setter @NoArgsConstructor @ToString
public class Genero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_genero;
    
    @Column(nullable = false, unique = true, name = "nombre")
    private String nombre;

    @ManyToMany(mappedBy = "genero", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Juego> juegos;

    public Genero(String nombre) {
        this.nombre = nombre;
    }

    public Genero(String nombre, List<Juego> juegos) {
        this.nombre = nombre;
        this.juegos = juegos;
    }
}


