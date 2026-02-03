package com.daw.tfg.Dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter @NoArgsConstructor
public class UsuarioDTO {
    @NotBlank
    @Size(min=4, max=8)
    private String username;

    @NotBlank
    @Size(min=8)
    private String passwd;

}
