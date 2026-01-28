package com.daw.tfg.models;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

    @ManyToMany
    @JoinTable(name = "juegos_carrito",
        joinColumns = @JoinColumn(name = "id_juego"),
        inverseJoinColumns = @JoinColumn(name = "id_carrito"))
    @JsonIgnore
    private Set<Juego> juegos;

    @OneToMany(mappedBy = "contenido_adicional")
    @JsonIgnore
    private List<Contenido_Adicional> id_Contenido_Adicional;

    @OneToOne
    @JoinColumn(name = "id_compra", nullable = false)
    private Compra id_Compra;

    public Carrito(Usuario id_Usuario, Set<Juego> juegos, List<Contenido_Adicional> id_Contenido_Adicional, Compra id_Compra) {
        this.id_Usuario = id_Usuario;
        this.juegos = juegos;
        this.id_Contenido_Adicional = id_Contenido_Adicional;
        this.id_Compra = id_Compra;
    }
}
