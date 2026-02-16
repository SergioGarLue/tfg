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
public class PerfilUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario_perfil;

    @Column(nullable = false)
    private String imagenUsuario;

    @Column(nullable = false)
    private String imagenFondoPerfil;

    @Column(nullable = false)
    private String pais;

    @Column(nullable = false, length = 500)
    private String biografia;

    @Column(nullable = false)
    private Boolean estado;


    public PerfilUsuario(String imagenUsuario, String imagenFondoPerfil, String pais, 
            String biografia, Boolean estado) {
        this.imagenUsuario = imagenUsuario;
        this.imagenFondoPerfil = imagenFondoPerfil;
        this.pais = pais;
        this.biografia = biografia;
        this.estado = estado;
    }
}
