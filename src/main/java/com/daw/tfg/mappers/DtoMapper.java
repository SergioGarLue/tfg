package com.daw.tfg.mappers;

import com.daw.tfg.dtos.*;
import com.daw.tfg.models.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class DtoMapper {

    // Juego mappings
    public static JuegoDTO toJuegoDTO(Juego juego) {
        if (juego == null) return null;

        JuegoDTO dto = new JuegoDTO();
        dto.setIdJuego(juego.getIdJuego());
        dto.setTitulo(juego.getTitulo());
        dto.setType(juego.getTipo());
        dto.setDeveloper(Objects.requireNonNullElse(juego.getDesarrollador(), new Desarrollador()).getNombre()); // assume getNombre()
        dto.setPublisher(Objects.requireNonNullElse(juego.getEditor(), new Editor()).getNombre());
        dto.setFechaLanzamiento(juego.getFechaLanzamiento());
        dto.setDescription(juego.getDescripcion());
        dto.setImagen(juego.getImagen());
        dto.setScreenshots(juego.getScreenshots());
        dto.setPesoJuego(juego.getPesoJuego());
        dto.setPrice(new JuegoDTO.PriceDTO(juego.getPrecio(), juego.getPorcentaje()));

        // Platforms from plataformas List<String>
        JuegoDTO.PlatformsDTO platforms = new JuegoDTO.PlatformsDTO();
        List<String> plats = juego.getPlataformas();
        platforms.setWindows(plats != null && plats.contains("windows"));
        platforms.setMac(plats != null && plats.contains("mac"));
        platforms.setLinux(plats != null && plats.contains("linux"));
        dto.setPlatforms(platforms);

        // genres placeholder (from relation, null for simple)
        dto.setGenres(null);

        return dto;
    }

    public static Juego fromJuegoDTO(JuegoDTO dto) {
        if (dto == null) return null;
        Juego juego = new Juego();
        juego.setIdJuego(dto.getIdJuego());
        juego.setTitulo(dto.getTitulo());
        juego.setPrecio(dto.getPrice() != null ? dto.getPrice().getFinalPrice() : 0.0);
        juego.setPorcentaje(dto.getPrice() != null ? dto.getPrice().getPorcentaje() : 0);
        juego.setDescripcion(dto.getDescription());
        juego.setFechaLanzamiento(dto.getFechaLanzamiento());
        juego.setPesoJuego(dto.getPesoJuego());
        juego.setImagen(dto.getImagen());
        juego.setScreenshots(dto.getScreenshots());
        juego.setTipo(dto.getType());
        // relaciones null
        return juego;
    }

    // Usuario mappings
    public static UsuarioDTO toUsuarioDTO(Usuario usuario) {
        if (usuario == null) return null;
        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsername(usuario.getNombreUsuario());
        dto.setCorreoElectronico(usuario.getCorreoElectronico());
        dto.setPasswd(usuario.getContraseñaCifrada()); // plain for DTO? or hash
        return dto;
    }

    public static Usuario fromUsuarioDTO(UsuarioDTO dto) {
        if (dto == null) return null;
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(dto.getUsername());
        usuario.setCorreoElectronico(dto.getCorreoElectronico());
        usuario.setContraseñaCifrada(dto.getPasswd()); // hash later
        // enums null
        return usuario;
    }

    // Compra mappings
    public static CompraDTO toCompraDTO(Compra compra) {
        if (compra == null) return null;
        CompraDTO dto = new CompraDTO();
        dto.setIdCompra(compra.getIdCompra());
        dto.setFechaCompra(compra.getFechaCompra());
        dto.setTotal(compra.getTotal());
        dto.setEstado(compra.getEstado().name());
        dto.setPaymentIntentId(compra.getPaymentIntentId());
        return dto;
    }

    public static Compra fromCompraDTO(CompraDTO dto) {
        if (dto == null) return null;
        Compra compra = new Compra(0.0, null, null, null, null); // placeholder
        compra.setIdCompra(dto.getIdCompra());
        compra.setFechaCompra(dto.getFechaCompra());
        compra.setTotal(dto.getTotal());
        compra.setPaymentIntentId(dto.getPaymentIntentId());
        // estado from string
        return compra;
    }

    // PerfilUsuario mappings
    public static PerfilUsuario fromPerfilUsuarioDTO(Perfil_UsuarioDTO dto) {
        if (dto == null) return null;
        // use setters or constructor
        PerfilUsuario perfil = new PerfilUsuario();
        perfil.setImagenUsuario(dto.getImagenUsuario());
        perfil.setImagenFondoPerfil(dto.getImagenFondoPerfil());
        perfil.setPais(dto.getPais());
        perfil.setBiografia(dto.getBiografia());
        perfil.setEstado(dto.getEstado());
        return perfil;
    }

    public static Perfil_UsuarioDTO toPerfilUsuarioDTO(PerfilUsuario perfil) {
        if (perfil == null) return null;
        Perfil_UsuarioDTO dto = new Perfil_UsuarioDTO();
        dto.setImagenUsuario(perfil.getImagenUsuario());
        dto.setImagenFondoPerfil(perfil.getImagenFondoPerfil());
        dto.setPais(perfil.getPais());
        dto.setBiografia(perfil.getBiografia());
        dto.setEstado(perfil.getEstado());
        return dto;
    }

    // List variants
    public static List<JuegoDTO> toJuegoDTOList(List<Juego> juegos) {
        return juegos.stream().map(DtoMapper::toJuegoDTO).collect(Collectors.toList());
    }

    public static List<UsuarioDTO> toUsuarioDTOList(List<Usuario> usuarios) {
        return usuarios.stream().map(DtoMapper::toUsuarioDTO).collect(Collectors.toList());
    }

    public static List<CompraDTO> toCompraDTOList(List<Compra> compras) {
        return compras.stream().map(DtoMapper::toCompraDTO).collect(Collectors.toList());
    }

    // Add more as needed (Amigo, ListaDeseados, etc.)
}
