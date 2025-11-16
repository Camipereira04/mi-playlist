package uy.um.miplaylist.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import uy.um.miplaylist.model.Video;

@Service
public class PlaylistService {

    private static final String PLAYLIST_FILE = "playlist.json";

    private final List<Video> videos = new ArrayList<>();
    private long nextId = 1L;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Flag para saber si usamos persistencia (en la app sí, en los tests no)
    private final boolean habilitarPersistencia;

    // Constructor usado por Spring (persistencia activada)
    public PlaylistService() {
        this(true);
    }

    // Constructor extra para tests (podemos desactivar persistencia)
    public PlaylistService(boolean habilitarPersistencia) {
        this.habilitarPersistencia = habilitarPersistencia;

        if (habilitarPersistencia) {
            cargarDesdeArchivo();

            if (videos.isEmpty()) {
                agregarVideoSinGuardar("Video demo 1", "https://www.youtube.com/watch?v=dQw4w9WgXcQ");
                agregarVideoSinGuardar("Video demo 2", "https://www.youtube.com/watch?v=o-YBDTqX_ZU");
                guardarEnArchivo();
            }
        }
    }

    // --------- API pública usada por el controlador ---------

    public List<Video> listarVideos() {
        return Collections.unmodifiableList(videos);
    }

    public void agregarVideo(String titulo, String url) {
        agregarVideoSinGuardar(titulo, url);
        guardarEnArchivo();
    }

    public void eliminarVideo(long id) {
        videos.removeIf(v -> v.getId() == id);
        guardarEnArchivo();
    }

    public void darLike(long id) {
        Video v = buscarPorId(id);
        if (v != null) {
            v.setLikes(v.getLikes() + 1);
        }
        guardarEnArchivo();
    }


    public void toggleFavorito(long id) {
        Video v = buscarPorId(id);
        if (v != null) {
            v.setFavorito(!v.isFavorito());
        }
        guardarEnArchivo();
    }


    // --------- Métodos privados de apoyo ---------

    private void agregarVideoSinGuardar(String titulo, String url) {
        Video v = new Video(nextId++, titulo, url);
        videos.add(v);
    }

    private Video buscarPorId(long id) {
        return videos.stream()
                .filter(v -> v.getId() == id)
                .findFirst()
                .orElse(null);
    }


    private void cargarDesdeArchivo() {
        if (!habilitarPersistencia) return;

        Path path = Paths.get(PLAYLIST_FILE);
        if (!Files.exists(path)) {
            return;
        }

        try {
            File file = path.toFile();
            if (file.length() == 0) {
                return;
            }

            Video[] array = objectMapper.readValue(file, Video[].class);
            videos.clear();
            videos.addAll(Arrays.asList(array));

            long maxId = videos.stream()
                    .mapToLong(Video::getId)
                    .max()
                    .orElse(0L);
            nextId = maxId + 1;

        } catch (IOException e) {
            System.err.println("Error cargando playlist desde " + PLAYLIST_FILE);
            e.printStackTrace();
        }
    }

    private void guardarEnArchivo() {
        if (!habilitarPersistencia) return;

        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(PLAYLIST_FILE), videos);
        } catch (IOException e) {
            System.err.println("Error guardando playlist en " + PLAYLIST_FILE);
            e.printStackTrace();
        }
    }
}
