package com.daw.tfg.models;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "carrito")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCarrito;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToMany
    @JoinTable(name = "juegos_carrito",
        joinColumns = @JoinColumn(name = "id_carrito"),
        inverseJoinColumns = @JoinColumn(name = "id_juego")) 
    @JsonIgnore
    private Set<Juego> juegos;

    @OneToMany(mappedBy = "carrito")
    @JsonIgnore
    private Set<ContenidoAdicional> contenidosAdicionales;

    @OneToOne
    @JoinColumn(name = "id_compra", nullable = true)
    private Compra compra;

    public Carrito(Usuario usuario, Set<Juego> juegos, Set<ContenidoAdicional> contenidosAdicionales) {
        this.usuario = usuario;
        this.juegos = juegos;
        this.contenidosAdicionales = contenidosAdicionales;
        //no tiene compra porque puede ser null, los carritos empiezan sin compra
    }

    
}
