package com.daw.tfg.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class LoginDTO {
    @NotBlank
    private String username;
    
    @NotBlank
    private String password;
}
