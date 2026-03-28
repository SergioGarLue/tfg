package com.daw.tfg.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter @NoArgsConstructor
public class UsuarioDTO {
    @NotBlank
    @Size(min=4, max=16)
    private String username;

    @NotBlank @Email
    private String correoElectronico;

    @NotBlank
    @Size(min=8)
    private String passwd;
}
