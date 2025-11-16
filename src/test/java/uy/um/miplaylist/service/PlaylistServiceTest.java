package uy.um.miplaylist.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uy.um.miplaylist.model.Video;

class PlaylistServiceTest {

    private PlaylistService playlistService;

    @BeforeEach
    void setUp() {
        // Usamos el constructor con persistencia desactivada para los tests
        playlistService = new PlaylistService(false);
    }

    @Test
    void agregarVideo_deberiaAgregarUnVideoALaLista() {
        // Act
        playlistService.agregarVideo("Título test", "https://youtu.be/test");

        // Assert
        List<Video> videos = playlistService.listarVideos();
        assertEquals(1, videos.size(), "Debe haber exactamente 1 video");
        Video v = videos.get(0);
        assertEquals("Título test", v.getTitulo());
        assertEquals("https://youtu.be/test", v.getUrl());
        assertEquals(0, v.getLikes());
        assertFalse(v.isFavorito());
    }

    @Test
    void eliminarVideo_deberiaEliminarElVideoConEseId() {
        // Arrange
        playlistService.agregarVideo("Video 1", "url1");
        playlistService.agregarVideo("Video 2", "url2");
        long idPrimero = playlistService.listarVideos().get(0).getId();

        // Act
        playlistService.eliminarVideo(idPrimero);

        // Assert
        List<Video> videos = playlistService.listarVideos();
        assertEquals(1, videos.size(), "Debe quedar solo 1 video después de eliminar");
        assertNotEquals(idPrimero, videos.get(0).getId(), "El video restante no debe tener el ID eliminado");
    }

    @Test
    void darLike_deberiaIncrementarElContadorDeLikes() {
        // Arrange
        playlistService.agregarVideo("Video", "url");
        Video v = playlistService.listarVideos().get(0);
        long id = v.getId();

        // Act
        playlistService.darLike(id);
        playlistService.darLike(id);

        // Assert
        Video actualizado = playlistService.listarVideos().get(0);
        assertEquals(2, actualizado.getLikes(), "El video debería tener 2 likes");
    }

    @Test
    void toggleFavorito_deberiaAlternarElEstadoDeFavorito() {
        // Arrange
        playlistService.agregarVideo("Video", "url");
        Video v = playlistService.listarVideos().get(0);
        long id = v.getId();

        // Estado inicial
        assertFalse(v.isFavorito(), "Al inicio no debería ser favorito");

        // Act 1: marcar como favorito
        playlistService.toggleFavorito(id);
        Video despuesPrimerToggle = playlistService.listarVideos().get(0);
        assertTrue(despuesPrimerToggle.isFavorito(), "Después del primer toggle debería ser favorito");

        // Act 2: desmarcar favorito
        playlistService.toggleFavorito(id);
        Video despuesSegundoToggle = playlistService.listarVideos().get(0);
        assertFalse(despuesSegundoToggle.isFavorito(), "Después del segundo toggle no debería ser favorito");
    }
}
