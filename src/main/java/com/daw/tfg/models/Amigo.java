package com.daw.tfg.models;

import java.time.LocalDateTime;

import com.daw.tfg.Enums.EstadoAmigos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "amistad")
public class Amigo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAmistad;
    // id del usuario que recibe la solicitud de amistad
    @ManyToOne
    @JoinColumn(name = "id_solicitante", nullable = false)
    private Usuario solicitante;
    // id del usuario que manda la solicitud de amistad
    @ManyToOne
    @JoinColumn(name = "id_destinatario", nullable = false)
    private Usuario destinatario;

    // Un enum que se pasa como String a la BD con el rol del usuario
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAmigos estado;

    //fecha en la que se realiza la peticion de amistad
    @Column(name="fecha_peticion")
    private LocalDateTime fechaPeticion;

    public Amigo(Usuario solicitante, Usuario destinatario, EstadoAmigos estado) {
        this.solicitante = solicitante;
        this.destinatario = destinatario;
        this.estado = estado;
        this.fechaPeticion = LocalDateTime.now();
    }
}
