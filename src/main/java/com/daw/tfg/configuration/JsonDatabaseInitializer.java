package com.daw.tfg.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.daw.tfg.dtos.JuegoDTO;
import com.daw.tfg.enums.EstadoUsuario;
import com.daw.tfg.enums.RolesUsuarios;
import com.daw.tfg.mappers.DtoMapper;
import com.daw.tfg.models.Desarrollador;
import com.daw.tfg.models.Editor;
import com.daw.tfg.models.Genero;
import com.daw.tfg.models.Juego;
import com.daw.tfg.models.PerfilUsuario;
import com.daw.tfg.models.Usuario;
import com.daw.tfg.service.DesarrolladorService;
import com.daw.tfg.service.EditorService;
import com.daw.tfg.service.GeneroService;
import com.daw.tfg.service.JuegoService;
import com.daw.tfg.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class JsonDatabaseInitializer {

    private static final Logger logger = LoggerFactory.getLogger(JsonDatabaseInitializer.class);

    private final JuegoService juegoService;
    private final DesarrolladorService desarrolladorService;
    private final EditorService editorService;
    private final GeneroService generoService;
    private final UsuarioService usuarioService;

    public JsonDatabaseInitializer(
            JuegoService juegoService,
            DesarrolladorService desarrolladorService,
            EditorService editorService,
            GeneroService generoService,
            UsuarioService usuarioService) {
        this.juegoService = juegoService;
        this.desarrolladorService = desarrolladorService;
        this.editorService = editorService;
        this.generoService = generoService;
        this.usuarioService = usuarioService;
    }

    @Bean
    public ApplicationRunner initDatabaseFromJson() {
        return args -> {
            Resource resource = new ClassPathResource("static/JSON/steam_top_1000_sellers.json");

            if (!resource.exists()) {
                logger.warn("No se encontró steam_top_1000_sellers.json en static/JSON");
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            List<JuegoDTO> juegosDto;

            try {
                juegosDto = mapper.readValue(resource.getInputStream(), new TypeReference<List<JuegoDTO>>() {
                });
            } catch (IOException e) {
                logger.error("Error leyendo JSON de juegos", e);
                return;
            }

            List<Desarrollador> desarrolladoresCreados = new ArrayList<>();

            for (JuegoDTO dto : juegosDto) {
                try {
                    if (dto.getIdJuego() == null) {
                        logger.warn("Juego JSON omitido porque appid es nulo: {}", dto);
                        continue;
                    }

                    if (juegoService.existsById(dto.getIdJuego())) {
                        continue;
                    }

                    String titulo = dto.getTitulo();
                    if (titulo == null || titulo.isBlank()) {
                        logger.warn("Juego con id {} omitido porque título vacío", dto.getIdJuego());
                        continue;
                    }

                    Desarrollador desarrollador = null;
                    if (dto.getDeveloper() != null && !dto.getDeveloper().isBlank()) {
                        desarrollador = desarrolladorService.findByNombre(dto.getDeveloper())
                                .orElseGet(() -> {
                                    Desarrollador nuevo = new Desarrollador();
                                    nuevo.setNombre(dto.getDeveloper());
                                    return desarrolladorService.save(nuevo);
                                });
                        if (!desarrolladoresCreados.contains(desarrollador)) {
                            desarrolladoresCreados.add(desarrollador);
                        }
                    }

                    Editor editor = null;
                    if (dto.getPublisher() != null && !dto.getPublisher().isBlank()) {
                        String editorNombre = dto.getPublisher();
                        editor = editorService.findByNombre(editorNombre)
                                .orElseGet(() -> {
                                    Editor nuevo = new Editor();
                                    nuevo.setNombre(editorNombre);
                                    return editorService.save(nuevo);
                                });
                    }

                    Set<Genero> generos = new HashSet<>();
                    if (dto.getGenres() != null) {
                        dto.getGenres().forEach(genreName -> {
                            if (genreName == null || genreName.isBlank())
                                return;
                            generoService.findByNombre(genreName)
                                    .ifPresentOrElse(generos::add, () -> {
                                        Genero nuevoGenero = new Genero();
                                        nuevoGenero.setNombre(genreName);
                                        generos.add(generoService.save(nuevoGenero));
                                    });
                        });
                    }

                    List<String> plataformas = new ArrayList<>();
                    if (dto.getPlatforms() != null) {
                        if (Boolean.TRUE.equals(dto.getPlatforms().getWindows())) {
                            plataformas.add("Windows");
                        }
                        if (Boolean.TRUE.equals(dto.getPlatforms().getMac())) {
                            plataformas.add("Mac");
                        }
                        if (Boolean.TRUE.equals(dto.getPlatforms().getLinux())) {
                            plataformas.add("Linux");
                        }
                    }

                    Juego juego = DtoMapper.fromJuegoDTO(dto);
                    juego.setDesarrollador(desarrollador);
                    juego.setEditor(editor);
                    juego.setGeneros(generos);
                    juego.setPlataformas(plataformas);

                    juegoService.save(juego);
                    logger.info("Juego importado desde JSON: {} ({})", titulo, dto.getIdJuego());
                } catch (Exception e) {
                    logger.error("Fallo al importar juego JSON id={} nombre={}", dto.getIdJuego(), dto.getTitulo(), e);
                }
            }

            // Crear 2+ usuarios DEVELOPER vinculados a desarrolladores existentes
            if (desarrolladoresCreados.size() >= 2) {
                crearUsuarioDeveloperSiNoExiste("devuser", "jsondev@platform.com", desarrolladoresCreados.get(0));
                crearUsuarioDeveloperSiNoExiste("devuser2", "jsondev2@platform.com", desarrolladoresCreados.get(1));

            } else if (desarrolladoresCreados.size() == 1) {
                crearUsuarioDeveloperSiNoExiste("devuser", "jsondev@platform.com", desarrolladoresCreados.get(0));
            }

        };
    }

    private void crearUsuarioDeveloperSiNoExiste(String username, String email, Desarrollador desarrollador) {
        if (usuarioService.findAll().stream().anyMatch(u -> u.getNombreUsuario().equals(username))) {
            Usuario existente = usuarioService.findByNombreUsuario(username);
            if (existente.getRol() != RolesUsuarios.DEVELOPER || existente.getDesarrollador() == null) {
                existente.setRol(RolesUsuarios.DEVELOPER);
                existente.setDesarrollador(desarrollador);
                usuarioService.save(existente);
                logger.info("Usuario {} actualizado a DEVELOPER vinculado a {}", username, desarrollador.getNombre());
            }
            return;
        }

        try {
            PerfilUsuario perfil = new PerfilUsuario(
                "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/Default_pfp.svg/500px-Default_pfp.svg.png",
                "https://via.placeholder.com/1200x400",
                "España",
                "Desarrollador de " + desarrollador.getNombre(),
                true
            );

            Usuario usuario = new Usuario(
                username,
                "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjXdOeKjQwBW9jDkEJHGKjgxKjLwJxS",
                email,
                EstadoUsuario.DESCONECTADO,
                RolesUsuarios.DEVELOPER,
                perfil
            );
            usuario.setDesarrollador(desarrollador);
            usuarioService.save(usuario);
            logger.info("Usuario DEVELOPER creado: {} / Dev@123! vinculado a {}", username, desarrollador.getNombre());
        } catch (Exception e) {
            logger.error("Error creando usuario developer: {}", e.getMessage());
        }
    }
}
