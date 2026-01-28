package com.daw.tfg.models;

import com.daw.tfg.Enums.EstadoUsuario;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Getter @Setter @NoArgsConstructor @ToString
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario;

    @Column(unique = true, nullable = false)
    private String nombre_usuario;

    @Column(nullable = false)
    private String contraseña_cifrada;

    @Column(unique = true, nullable = false)
    private String correo_electronico;

    @Column(nullable = false)
    private EstadoUsuario estado;
    
    @Column(nullable = false)
    private Boolean contenido_Explicito;
    //falta relacion con perfil_usuario

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_perfil", nullable = false, unique = true)
    @JsonIgnore
    private Perfil_Usuario id_usuario_perfil;

    public Usuario(String contraseña_cifrada, String correo_electronico, EstadoUsuario estado, String nombre_usuario) {
        this.contraseña_cifrada = contraseña_cifrada;
        this.correo_electronico = correo_electronico;
        this.estado = estado;
        this.nombre_usuario = nombre_usuario;
    }
    
}