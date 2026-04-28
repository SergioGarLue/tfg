package com.daw.tfg.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daw.tfg.dtos.JuegoDTO;
import com.daw.tfg.models.Desarrollador;
import com.daw.tfg.models.Genero;
import com.daw.tfg.models.Juego;
import com.daw.tfg.repository.DesarrolladorRepository;
import com.daw.tfg.repository.GeneroRepository;
import com.daw.tfg.repository.JuegoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servicio para ejecutar el script Python de Steam y importar datos a la BD.
 * Solo debe ser accedido por usuarios con rol ADMIN.
 */
@Service
public class SteamImportService {

    private final JuegoRepository juegoRepository;
    private final DesarrolladorRepository desarrolladorRepository;
    private final GeneroRepository generoRepository;

    public SteamImportService(JuegoRepository juegoRepository,
            DesarrolladorRepository desarrolladorRepository,
            GeneroRepository generoRepository) {
        this.juegoRepository = juegoRepository;
        this.desarrolladorRepository = desarrolladorRepository;
        this.generoRepository = generoRepository;
    }

    /**
     * Ejecuta el script Python de Steam y luego importa los datos a la BD.
     * 
     * @return Mensaje indicando el progreso
     * @throws IOException Si hay error al ejecutar el script o leer el JSON
     * @throws InterruptedException Si se interrumpe la ejecución del script
     */
    @Transactional
    public String ejecutarSteamScraper() throws IOException, InterruptedException {
        // 1. Ejecutar el script Python
        String mensaje = ejecutarPython();
        System.out.println(mensaje);

        // 2. Importar datos del JSON a la BD
        String mensajeImport = importarDatosAlaBD();
        System.out.println(mensajeImport);

        return mensaje + "\n" + mensajeImport;
    }

    /**
     * Ejecuta el script Python siis.py
     * 
     * @return Mensaje con el resultado de la ejecución
     * @throws IOException Si hay error en la ejecución
     * @throws InterruptedException Si se interrumpe la ejecución
     */
    private static final String[] PYTHON_COMMANDS = {"python", "python3", "py"};

    private String ejecutarPython() throws IOException, InterruptedException {
        // Ruta al script Python
        String rutaScript = Paths.get("src", "main", "resources", "static", "JSON", "siis.py")
                .toAbsolutePath().toString();

        String comandoPython = encontrarComandoPython();
        if (comandoPython == null) {
            return "❌ No se encontró un intérprete Python en el sistema. Instale Python o configure el comando 'python', 'python3' o 'py'.";
        }

        // Crear el comando: python -u siis.py
        ProcessBuilder pb = new ProcessBuilder(comandoPython, "-u", rutaScript);

        // Establecer el directorio de trabajo (raíz del proyecto)
        pb.directory(new File("."));

        // Forzar UTF-8 en la salida del proceso Python en Windows
        pb.environment().put("PYTHONIOENCODING", "utf-8");
        pb.environment().put("PYTHONUTF8", "1");

        // Redirigir los flujos de salida
        pb.redirectErrorStream(true);

        // Iniciar el proceso
        Process proceso = pb.start();

        // Leer la salida del proceso
        StringBuilder output = new StringBuilder();
        try (var reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(proceso.getInputStream()))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                output.append(linea).append("\n");
            }
        }

        // Esperar a que termine el proceso
        int codigoSalida = proceso.waitFor();

        if (codigoSalida == 0) {
            return "✅ Script Python ejecutado exitosamente.\n" + output.toString();
        } else {
            return "❌ Error en la ejecución del script Python (código: " + codigoSalida + ")\n"
                    + output.toString();
        }
    }

    private String encontrarComandoPython() throws InterruptedException {
        for (String comando : PYTHON_COMMANDS) {
            try {
                ProcessBuilder pb = new ProcessBuilder(comando, "--version");
                pb.redirectErrorStream(true);
                Process proceso = pb.start();
                int codigoSalida = proceso.waitFor();
                if (codigoSalida == 0) {
                    return comando;
                }
            } catch (IOException e) {
                // Comando no disponible, probar el siguiente
            }
        }
        return null;
    }

    /**
     * Importa los datos del JSON generado por el script Python a la BD.
     * Lee el archivo steam_top_1000_sellers.json y guarda los juegos.
     * 
     * @return Mensaje con el resultado de la importación
     * @throws IOException Si hay error al leer el JSON
     */
    private String importarDatosAlaBD() throws IOException {
        String rutaJSON = "src/main/resources/static/JSON/steam_top_1000_sellers.json";
        File archivoJSON = new File(rutaJSON);

        if (!archivoJSON.exists()) {
            return "❌ Archivo JSON no encontrado: " + rutaJSON;
        }

        // Leer el contenido del JSON
        String contenidoJSON = new String(Files.readAllBytes(Paths.get(rutaJSON)));

        // Parsear el JSON a array de JuegoDTO
        ObjectMapper mapper = new ObjectMapper();
        JuegoDTO[] juegosDTO;

        try {
            juegosDTO = mapper.readValue(contenidoJSON, JuegoDTO[].class);
        } catch (Exception e) {
            return "❌ Error al parsear el JSON: " + e.getMessage();
        }

        int totalJuegos = juegosDTO.length;
        int juegosProcesados = 0;
        int juegosGuardados = 0;
        int errores = 0;

        // Procesar cada juego
        for (JuegoDTO juegoDTO : juegosDTO) {
            try {
                juegosProcesados++;

                // Verificar si el juego ya existe
                if (juegoRepository.existsById(juegoDTO.getIdJuego())) {
                    System.out.println("⏭️ Juego ya existe: " + juegoDTO.getTitulo());
                    continue;
                }

                // Mapear DTO a Juego
                Juego juego = new Juego();
                juego.setIdJuego(juegoDTO.getIdJuego());
                juego.setTitulo(juegoDTO.getTitulo());
                juego.setDescripcion(juegoDTO.getDescription());
                juego.setFechaLanzamiento(juegoDTO.getFechaLanzamiento());
                juego.setPesoJuego(juegoDTO.getPesoJuego());
                juego.setTipo(juegoDTO.getType());
                juego.setImagen(juegoDTO.getImagen());
                juego.setScreenshots(juegoDTO.getScreenshots());

                // Precio y descuento
                if (juegoDTO.getPrice() != null) {
                    juego.setPrecio(juegoDTO.getPrice().getFinalPrice() != null
                            ? juegoDTO.getPrice().getFinalPrice()
                            : 0.0);
                    juego.setPorcentaje(juegoDTO.getPrice().getPorcentaje() != null
                            ? juegoDTO.getPrice().getPorcentaje()
                            : 0);
                } else {
                    juego.setPrecio(0.0);
                    juego.setPorcentaje(0);
                }

                // Plataformas
                List<String> plataformas = convertirPlataformas(juegoDTO.getPlatforms());
                juego.setPlataformas(plataformas);

                // Crear o buscar desarrollo
                Desarrollador desarrollador = obtenerOCrearDesarrollador(juegoDTO.getDeveloper());
                juego.setDesarrollador(desarrollador);

                // Crear o buscar géneros
                Set<Genero> generos = obtenerOCrearGeneros(juegoDTO.getGenres());
                juego.setGeneros(generos);

                // Guardar el juego
                juegoRepository.save(juego);
                juegosGuardados++;

                System.out.println("[" + juegosProcesados + "/" + totalJuegos + "] ✅ Juego guardado: "
                        + juegoDTO.getTitulo());

            } catch (Exception e) {
                errores++;
                System.out.println("❌ Error procesando juego: " + e.getMessage());
                e.printStackTrace();
            }
        }

        String resumen = String.format(
                "📊 Importación completada:\n" +
                        "   - Total procesados: %d\n" +
                        "   - Juegos guardados: %d\n" +
                        "   - Errores: %d",
                juegosProcesados, juegosGuardados, errores);

        return resumen;
    }

    /**
     * Obtiene un desarrollador de la BD o lo crea si no existe.
     * 
     * @param nombreDesarrollador Nombre del desarrollador
     * @return Desarrollador encontrado o creado
     */
    private Desarrollador obtenerOCrearDesarrollador(String nombreDesarrollador) {
        if (nombreDesarrollador == null || nombreDesarrollador.isBlank()) {
            nombreDesarrollador = "Desconocido";
        }

        // Buscar en la BD
        var desarrolladorOpt = desarrolladorRepository.findByNombre(nombreDesarrollador);

        if (desarrolladorOpt.isPresent()) {
            return desarrolladorOpt.get();
        }

        // Crear nueva entrada
        Desarrollador nuevoDesarrollador = new Desarrollador();
        nuevoDesarrollador.setNombre(nombreDesarrollador);
        nuevoDesarrollador.setImagen(""); // Sin imagen de desarrollador

        return desarrolladorRepository.save(nuevoDesarrollador);
    }

    /**
     * Obtiene un conjunto de géneros de la BD o los crea si no existen.
     * 
     * @param listaGeneros Lista de nombres de géneros
     * @return Set de géneros encontrados o creados
     */
    private Set<Genero> obtenerOCrearGeneros(List<String> listaGeneros) {
        Set<Genero> generos = new HashSet<>();

        if (listaGeneros == null || listaGeneros.isEmpty()) {
            return generos;
        }

        for (String nombreGenero : listaGeneros) {
            if (nombreGenero == null || nombreGenero.isBlank()) {
                continue;
            }

            // Buscar género en la BD
            var generoOpt = generoRepository.findByNombre(nombreGenero);

            if (generoOpt.isPresent()) {
                generos.add(generoOpt.get());
            } else {
                // Crear nueva entrada
                Genero nuevoGenero = new Genero();
                nuevoGenero.setNombre(nombreGenero);

                generos.add(generoRepository.save(nuevoGenero));
            }
        }

        return generos;
    }

    /**
     * Convierte el DTO de plataformas a una lista de strings.
     * 
     * @param platformsDTO DTO con información de plataformas
     * @return Lista de nombres de plataformas
     */
    private List<String> convertirPlataformas(JuegoDTO.PlatformsDTO platformsDTO) {
        List<String> plataformas = new java.util.ArrayList<>();

        if (platformsDTO != null) {
            if (Boolean.TRUE.equals(platformsDTO.getWindows())) {
                plataformas.add("windows");
            }
            if (Boolean.TRUE.equals(platformsDTO.getMac())) {
                plataformas.add("mac");
            }
            if (Boolean.TRUE.equals(platformsDTO.getLinux())) {
                plataformas.add("linux");
            }
        }

        return plataformas;
    }
}
