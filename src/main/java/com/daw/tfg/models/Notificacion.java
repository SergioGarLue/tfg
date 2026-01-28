package com.daw.tfg.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "notificacion")
@Getter @Setter @NoArgsConstructor @ToString
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_notificacion;

    @OneToOne
    @JoinColumn(name = "id_compra", nullable = false)
    private Compra id_Compra;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    // RARETE LA RELACIÓN PARA LAS NOTIS DE AMISTADES
    // @ManyToOne
    // @JoinColumn(name = "id_usuario2", nullable = false)
    // private Usuario usuario2;

    public Notificacion(Compra id_Compra, Usuario usuario) {
        this.id_Compra = id_Compra;
        this.usuario = usuario;
    }
}
