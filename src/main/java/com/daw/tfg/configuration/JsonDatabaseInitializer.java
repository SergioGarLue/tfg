package com.daw.tfg.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.daw.tfg.dtos.JuegoDTO;
import com.daw.tfg.models.Desarrollador;
import com.daw.tfg.models.Editor;
import com.daw.tfg.models.Genero;
import com.daw.tfg.models.Juego;
import com.daw.tfg.service.DesarrolladorService;
import com.daw.tfg.service.EditorService;
import com.daw.tfg.service.GeneroService;
import com.daw.tfg.service.JuegoService;
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

    public JsonDatabaseInitializer(
            JuegoService juegoService,
            DesarrolladorService desarrolladorService,
            EditorService editorService,
            GeneroService generoService) {
        this.juegoService = juegoService;
        this.desarrolladorService = desarrolladorService;
        this.editorService = editorService;
        this.generoService = generoService;
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
                juegosDto = mapper.readValue(resource.getInputStream(), new TypeReference<List<JuegoDTO>>() {});
            } catch (IOException e) {
                logger.error("Error leyendo JSON de juegos", e);
                return;
            }

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

                    Double precio = 0.0;
                    Integer porcentaje = 0;
                    if (dto.getPrice() != null) {
                        if (dto.getPrice().getFinalPrice() != null) {
                            precio = dto.getPrice().getFinalPrice();
                        }
                        if (dto.getPrice().getPorcentaje() != null) {
                            porcentaje = dto.getPrice().getPorcentaje();
                        }
                    }

                    Desarrollador desarrollador = null;
                    if (dto.getDeveloper() != null && !dto.getDeveloper().isBlank()) {
                        desarrollador = desarrolladorService.findByNombre(dto.getDeveloper())
                                .orElseGet(() -> {
                                    Desarrollador nuevo = new Desarrollador();
                                    nuevo.setNombre(dto.getDeveloper());
                                    return desarrolladorService.save(nuevo);
                                });
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
                            if (genreName == null || genreName.isBlank()) return;
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

                    Juego juego = new Juego();
                    juego.setIdJuego(dto.getIdJuego());
                    juego.setTitulo(titulo);
                    juego.setDescripcion(dto.getDescription());
                    juego.setFechaLanzamiento(dto.getFechaLanzamiento());
                    juego.setImagen(dto.getImagen());
                    juego.setPesoJuego(dto.getPesoJuego());
                    juego.setPrecio(precio);
                    juego.setPorcentaje(porcentaje);
                    juego.setTipo(dto.getType());
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
        };
    }
}
