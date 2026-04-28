package com.daw.tfg.enums;

public enum RolesUsuarios {
    // los anonymous podran acceder a ver los juegos sin iniciar sesion
    USER, //usuario que podra ver su perfil amigos y zonas privadas
    DEVELOPER, //podra acceder a informacion sobre sus juegos publicados (B2B)
    DESARROLLADOR, //legacy alias
    EDITOR, //podra acceder a informacion sobre sus juegos publicados
    ADMIN //acceso total a la pagina 
}
