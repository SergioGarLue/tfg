package com.daw.tfg.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Getter @Setter @NoArgsConstructor @ToString
@Table(name = "perfil_usuario")
public class Perfil_Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario_perfil;

    @Column(nullable = false)
    private String imagen_usuario;

    @Column(nullable = false)
    private String imagen_fondo_perfil;

    @Column(nullable = false)
    private String pais;

    @Column(nullable = false, length = 500)
    private String biografia;

    @Column(nullable = false)
    private Boolean estado;


    public Perfil_Usuario(String imagen_usuario, String imagen_fondo_perfil, String pais, 
            String biografia, Boolean estado) {
        this.imagen_usuario = imagen_usuario;
        this.imagen_fondo_perfil = imagen_fondo_perfil;
        this.pais = pais;
        this.biografia = biografia;
        this.estado = estado;
    }
}
