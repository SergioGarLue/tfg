package com.daw.tfg.models;

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
    private Long idCarrito;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToMany
    @JoinTable(name = "juegos_carrito",
        joinColumns = @JoinColumn(name = "id_juego"),
        inverseJoinColumns = @JoinColumn(name = "id_carrito"))
    @JsonIgnore
    private Set<Juego> juegos;

    @OneToMany(mappedBy = "carrito")
    @JsonIgnore
    private Set<Contenido_Adicional> contenidosAdicionales;

    @OneToOne
    @JoinColumn(name = "id_compra", nullable = false)
    private Compra compra;

    public Carrito(Usuario usuario, Set<Juego> juegos, Set<Contenido_Adicional> contenidosAdicionales, Compra compra) {
        this.usuario = usuario;
        this.juegos = juegos;
        this.contenidosAdicionales = contenidosAdicionales;
        this.compra = compra;
    }
}
