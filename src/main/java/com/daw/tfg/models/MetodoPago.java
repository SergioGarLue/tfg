package com.daw.tfg.models;

import java.util.Date;
import java.util.List;

import com.daw.tfg.Enums.TipoMetodoPago;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "metodo_pago")
@Getter @Setter @NoArgsConstructor @ToString
public class MetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_metodo_pago;

    @Column(unique = true, nullable = false, name = "proveedor")
    private String proveedor;

    @Column(nullable = false, name = "tipo")
    private TipoMetodoPago tipo;

    @Column(unique = true, nullable = false, name = "token")
    private String token;

    @Column(nullable = false, length = 4, name = "ultimos_digitos")
    private String ultimos_digitos;

    @Column(nullable = false, name = "fecha_expiracion")
    private Date fecha_expiracion;

    @Column(nullable = false, name = "activo")
    private Boolean activo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    @OneToMany(mappedBy = "metodoPago", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Compra> compras;

    public MetodoPago(String proveedor, TipoMetodoPago tipo, String token, String ultimos_digitos,
            Date fecha_expiracion, Boolean activo, Usuario usuario) {
        this.proveedor = proveedor;
        this.tipo = tipo;
        this.token = token;
        this.ultimos_digitos = ultimos_digitos;
        this.fecha_expiracion = fecha_expiracion;
        this.activo = activo;
        this.usuario = usuario;
    }
}
