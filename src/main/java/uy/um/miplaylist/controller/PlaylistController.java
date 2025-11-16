package uy.um.miplaylist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import uy.um.miplaylist.service.PlaylistService;

@Controller
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("tituloPagina", "Mi Playlist 🎵");
        model.addAttribute("mensaje", "Bienvenid@ a tu playlist personalizada");
        model.addAttribute("videos", playlistService.listarVideos());
        return "index";
    }

    @PostMapping("/videos/agregar")
    public String agregarVideo(
            @RequestParam String titulo,
            @RequestParam String url
    ) {
        playlistService.agregarVideo(titulo, url);
        return "redirect:/";
    }

    @GetMapping("/videos/eliminar")
    public String eliminarVideo(@RequestParam long id) {
        playlistService.eliminarVideo(id);
        return "redirect:/";
    }

    // 👍 Like
    @PostMapping("/videos/like")
    public String darLike(@RequestParam long id) {
        playlistService.darLike(id);
        return "redirect:/";
    }

    // ⭐ Favorito
    @PostMapping("/videos/favorito")
    public String marcarFavorito(@RequestParam long id) {
        playlistService.toggleFavorito(id);
        return "redirect:/";
    }
}
