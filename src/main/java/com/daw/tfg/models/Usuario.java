package com.daw.tfg.models;

import java.util.Set;

import com.daw.tfg.Enums.EstadoUsuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

    public Usuario(String contraseña_cifrada, String correo_electronico, EstadoUsuario estado, Long id, String nombre_usuario) {
        this.contraseña_cifrada = contraseña_cifrada;
        this.correo_electronico = correo_electronico;
        this.estado = estado;
        this.id_usuario = id;
        this.nombre_usuario = nombre_usuario;
    }
}