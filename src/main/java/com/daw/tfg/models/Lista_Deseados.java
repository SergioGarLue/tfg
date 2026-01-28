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
    private Long id_Lista_Deseados;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario id_Usuario;

    @OneToMany(mappedBy = "juego")
    @JsonIgnore
    private List<Juego> id_Juego;

    public Lista_Deseados(Long id, Usuario id_Usuario, List<Juego> id_Juego) {
        this.id_Lista_Deseados = id;
        this.id_Usuario = id_Usuario;
        this.id_Juego = id_Juego;
    }
}
