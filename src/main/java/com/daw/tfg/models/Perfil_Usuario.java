package com.daw.tfg.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Getter @Setter @NoArgsConstructor @ToString
@Table(name = "Perfil_Usuario")
public class Perfil_Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_perfil;

    @Column(unique = true, nullable = false)
    private String imagen_usuario;

    @Column(unique = true, nullable = false)
    private String imagen_fondo_perfil;

    @Column(nullable = false)
    private String pais;

    @Column(nullable = false)
    private String biografia;

    @Column(nullable = false)
    private Boolean estado;

    @OneToOne(mappedBy = "perfil_usuario")
    @JsonIgnore
    private Usuario id_Usuario;

    public Perfil_Usuario(String biografia, Boolean estado, Usuario id_Usuario, Long id_perfil, String imagen_fondo_perfil, String imagen_usuario, String pais) {
        this.biografia = biografia;
        this.estado = estado;
        this.id_Usuario = id_Usuario;
        this.id_perfil = id_perfil;
        this.imagen_fondo_perfil = imagen_fondo_perfil;
        this.imagen_usuario = imagen_usuario;
        this.pais = pais;
    }
}
