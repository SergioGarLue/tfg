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


@Entity
@Getter @Setter @NoArgsConstructor
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

    @OneToOne(mappedBy = "Usuario")
    @JsonIgnore
    private Usuario id_Usuario;

}
