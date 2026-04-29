package com.daw.tfg.models;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
    private Set<Juego> juegos = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "id_compra", nullable = true)
    private Compra compra;

    public Carrito(Usuario usuario, Set<Juego> juegos) {
        this.usuario = usuario;
        this.juegos = juegos;
        //no tiene compra porque puede ser null, los carritos empiezan sin compra
    }

    
}
