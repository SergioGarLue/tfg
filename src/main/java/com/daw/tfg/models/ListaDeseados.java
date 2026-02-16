package com.daw.tfg.models;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @ToString
@Table(name = "usuario_lista_deseados")
public class ListaDeseados {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idListaDeseados;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToMany
    @JoinTable(name = "lista_deseados_juegos",
        joinColumns = @JoinColumn(name = "id_lista_deseados"),
        inverseJoinColumns = @JoinColumn(name = "id_juego"))
    @JsonIgnore
    private Set<Juego> juegos;

    public ListaDeseados(Usuario usuario, Set<Juego> juegos) {
        this.usuario = usuario;
        this.juegos = juegos;
    }
}
