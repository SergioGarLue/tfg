package com.daw.tfg.models;

import com.daw.tfg.Enums.EstadoUsuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre_usuario;

    @Column(nullable = false)
    private String contraseña_cifrada;

    @Column(unique = true, nullable = false)
    private String correo_electronico;

    @Column(nullable = false)
    private EstadoUsuario estado;

    public Usuario(String contraseña_cifrada, String correo_electronico, EstadoUsuario estado, Long id, String nombre_usuario) {
        this.contraseña_cifrada = contraseña_cifrada;
        this.correo_electronico = correo_electronico;
        this.estado = estado;
        this.id = id;
        this.nombre_usuario = nombre_usuario;
    }
}