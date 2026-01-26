package com.daw.tfg.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "resenas")
@Getter @Setter @NoArgsConstructor @ToString
public class Resena {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "valoracion")
    private Integer valoracion;

    @ManyToOne()
    @JoinColumn(name = "id_juego")
    @Column(name ="juego")
    @JsonIgnore
    private Juego juego;

    @ManyToOne()
    @JoinColumn(name = "id_contenido_adicional")    
    @Column(name ="contenido_adicional")
    @JsonIgnore
    private Contenido_Adicional contenido_adicional;

    public Resena(Integer valoracion, Juego juego, Contenido_Adicional contenido_adicional) {
        this.valoracion = valoracion;
        this.juego = juego;
        this.contenido_adicional = contenido_adicional;
    }
}
