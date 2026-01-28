package com.daw.tfg.models;

import java.time.LocalDate;

import com.daw.tfg.Enums.EstadoAmigos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

    @ManyToOne
    @JoinColumn(name = "id_solicitante", nullable = false)
    private Usuario solicitante;

    @ManyToOne
    @JoinColumn(name = "id_destinatario", nullable = false)
    private Usuario destinatario;

    @Column(nullable = false)
    private EstadoAmigos estado;

    @Column(name="fecha_peticion")
    private LocalDate fechaPeticion;

    public Amigo(Usuario solicitante, Usuario destinatario, EstadoAmigos estado) {
        this.solicitante = solicitante;
        this.destinatario = destinatario;
        this.estado = estado;
    }

    
}
