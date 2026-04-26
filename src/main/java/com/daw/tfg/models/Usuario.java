package com.daw.tfg.models;

import com.daw.tfg.enums.EstadoUsuario;
import com.daw.tfg.enums.RolesUsuarios;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
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
    private Long idUsuario;

    //almacena el nombre del usuario
    @Column(unique = true, nullable = false)
    private String nombreUsuario;

    //almacenara la contraseña cifrada 
    @Column(nullable = false)
    @JsonIgnore
    private String contraseñaCifrada;

    //almacena el correoELectronico del usuario
    @Column(unique = true, nullable = false)
    private String correoElectronico;

    // Un enum que se pasa como String a la BD con el estado de conexion del usuario
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoUsuario conexion;

    // Un enum que se pasa como String a la BD con el rol del usuario
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "rol")
    private RolesUsuarios rol;

    /*
        Relacion uno a uno con el perfil del usuario enlazando la columna
        con su perfil para poder acceder a el posteriormente
    */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_usuario_perfil", nullable = false, unique = true)
    @JsonIgnore
    private PerfilUsuario perfilUsuario;

    public Usuario(String nombreUsuario, String contraseñaCifrada, String correoElectronico, EstadoUsuario conexion,
            RolesUsuarios rol, PerfilUsuario perfilUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.contraseñaCifrada = contraseñaCifrada;
        this.correoElectronico = correoElectronico;
        this.conexion = conexion;
        this.rol = rol;
        this.perfilUsuario = perfilUsuario;
    }

    @JsonProperty("imagenUsuario")
    public String getImagenUsuario() {
        return perfilUsuario != null ? perfilUsuario.getImagenUsuario() : null;
    }
}
