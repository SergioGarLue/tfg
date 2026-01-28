package com.daw.tfg.models;

import java.util.Set;

import com.daw.tfg.Enums.EstadoUsuario;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
    private Long idUsuario;

    @Column(unique = true, nullable = false)
    private String nombreUsuario;

    @Column(nullable = false)
    private String contraseñaCifrada;

    @Column(unique = true, nullable = false)
    private String correoElectronico;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_perfil", nullable = false, unique = true)
    @JsonIgnore
    private Perfil_Usuario perfilUsuario;

    public Usuario(String contraseñaCifrada, String correoElectronico, EstadoUsuario estado, String nombreUsuario) {
        this.contraseñaCifrada = contraseñaCifrada;
        this.correoElectronico = correoElectronico;
        this.estado = estado;
        this.nombreUsuario = nombreUsuario;
    }
    
}