package com.daw.tfg.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Perfil_UsuarioDTO {
    @NotBlank(message = "Imagen de usuario requerida")
    private String imagenUsuario;

    @NotBlank(message = "Imagen de fondo requerida")
    private String imagenFondoPerfil;

    @NotBlank(message = "País requerido")
    private String pais;

    @NotBlank(message = "Biografía requerida")
    @Size(max = 500, message = "Biografía no puede exceder 500 caracteres")
    private String biografia;

    @NotNull(message = "Estado requerido")
    private Boolean estado;
}
