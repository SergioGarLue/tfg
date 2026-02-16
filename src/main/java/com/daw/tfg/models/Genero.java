package com.daw.tfg.models;

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
    private Long idGenero;
    
    @Column(nullable = false, unique = true, name = "nombre")
    private String nombre;

    @ManyToMany( fetch = FetchType.LAZY)
    @JoinTable(
        name = "juego_genero",
        joinColumns = @JoinColumn(name = "id_genero"),
        inverseJoinColumns = @JoinColumn(name = "id_juego")
    )
    @JsonIgnore
    private Set<Juego> juegos;

    public Genero(String nombre, Set<Juego> juegos) {
        this.nombre = nombre;
        this.juegos = juegos;
    }
}


