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
@Getter @Setter @NoArgsConstructor @ToString
@Table(name = "usuario_lista_deseados")
public class Lista_Deseados {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idListaDeseados;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "juego")
    @JsonIgnore
    private List<Juego> juegos;

    public Lista_Deseados(Usuario usuario, List<Juego> juegos) {
        this.usuario = usuario;
        this.juegos = juegos;
    }
}
