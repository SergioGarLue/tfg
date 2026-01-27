package com.daw.tfg.models;

import com.daw.tfg.Enums.EstadoAmigos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "amigo")
public class Amigo {


    @ManyToOne
    @JoinColumn(name = "id_usuario1", nullable = false)
    private Usuario usuario1;

    @ManyToOne
    @JoinColumn(name = "id_usuario2", nullable = false)
    private Usuario usuario2;

    @Column(nullable = false)
    private EstadoAmigos estado;

    public Amigo(Usuario usuario1, Usuario usuario2, EstadoAmigos estado) {
        this.usuario1 = usuario1;
        this.usuario2 = usuario2;
        this.estado = estado;
    }
}
