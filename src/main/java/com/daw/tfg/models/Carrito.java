package com.daw.tfg.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "carrito")
@Getter @Setter @NoArgsConstructor @ToString
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_carrito;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario id_Usuario;

    @OneToMany(mappedBy = "juego")
    @JsonIgnore
    private List<Juego> id_Juego;

    @OneToMany(mappedBy = "contenido_adicional")
    @JsonIgnore
    private List<Contenido_Adicional> id_Contenido_Adicional;

    @OneToOne
    @JoinColumn(name = "id_compra", nullable = false)
    private Compra id_Compra;

    public Carrito(Usuario id_Usuario, List<Juego> id_Juego, List<Contenido_Adicional> id_Contenido_Adicional, Compra id_Compra) {
        this.id_Usuario = id_Usuario;
        this.id_Juego = id_Juego;
        this.id_Contenido_Adicional = id_Contenido_Adicional;
        this.id_Compra = id_Compra;
    }
}
