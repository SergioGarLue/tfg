package com.daw.tfg.models;

import java.util.Set;

import com.daw.tfg.Enums.RolesUsuarios;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
    private String contraseñaCifrada;

    //almacena el correoELectronico del usuario
    @Column(unique = true, nullable = false)
    private String correoElectronico;

    // Un enum que se pasa como String a la BD con el rol del usuario
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "rol")
    private RolesUsuarios rol;


    /*  Relacion para las amistades, Usuario con Usurio 
        pudiendo rechazar, aceptar, estar pendiente la peticion de amistad
        al aceptarla se guardara como Amistad donde aparecera  
        fecha de peticion, un id unico de cada amistad,  y los dos usuarios que la componen
    */
    @ManyToMany
    @JoinTable(
        name="amistad",
        joinColumns= @JoinColumn(name = "id_usuario"),
        inverseJoinColumns = @JoinColumn(name = "id_amigo")
    )
    private Set<Amigo> amigos;

    /*
        Relacion uno a uno con el perfil del usuario enlazando la columna
        con su perfil para poder acceder a el posteriormente
    */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_perfil", nullable = false, unique = true)
    @JsonIgnore
    private Perfil_Usuario perfilUsuario;

    public Usuario(String contraseñaCifrada, String correoElectronico, RolesUsuarios rol, String nombreUsuario) {
        this.contraseñaCifrada = contraseñaCifrada;
        this.correoElectronico = correoElectronico;
        this.rol = rol;
        this.nombreUsuario = nombreUsuario;
    }
    
}