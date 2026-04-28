package com.daw.tfg.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class UsuarioInfoDTO {
    private Long idUsuario;
    private String nombreUsuario;
    private String correoElectronico;
    private String rol;
    private String imagenUsuario;

    public UsuarioInfoDTO(Long idUsuario, String nombreUsuario, String correoElectronico, String rol, String imagenUsuario) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.correoElectronico = correoElectronico;
        this.rol = rol;
        this.imagenUsuario = imagenUsuario;
    }
}
