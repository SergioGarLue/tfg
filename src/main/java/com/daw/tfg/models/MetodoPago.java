package com.daw.tfg.models;

import java.util.Date;

import com.daw.tfg.Enums.TipoMetodoPago;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "metodo_pago")
public class MetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_metodo_pago;

    @Column(unique = true, nullable = false)
    private String proveedor;

    @Column(nullable = false)
    private TipoMetodoPago tipo;

    @Column(unique = true, nullable = false)
    private String token;

    @Column(nullable = false, length = 4)
    private String ultimos_digitos;

    @Column(nullable = false)
    private Date fecha_expiracion;

    @Column(nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "Usuario")
    @JsonIgnore
    private Usuario id_Usuario;

    public MetodoPago(String proveedor, TipoMetodoPago tipo, String token, String ultimos_digitos, Date fecha_expiracion, Boolean activo, Usuario id_Usuario, Long id_metodo_pago) {
        this.proveedor = proveedor;
        this.tipo = tipo;
        this.token = token;
        this.ultimos_digitos = ultimos_digitos;
        this.fecha_expiracion = fecha_expiracion;
        this.activo = activo;
        this.id_Usuario = id_Usuario;
        this.id_metodo_pago = id_metodo_pago;
    }
}
