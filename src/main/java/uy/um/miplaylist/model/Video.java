package uy.um.miplaylist.model;

public class Video {

    private long id;
    private String titulo;
    private String url;
    private int likes;
    private boolean favorito;

    public Video() {
    }

    public Video(long id, String titulo, String url) {
        this.id = id;
        this.titulo = titulo;
        this.url = url;
        this.likes = 0;
        this.favorito = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    // --- NUEVO: URL para embeber el video ---
    public String getEmbedUrl() {
        if (url == null) {
            return null;
        }

        // youtube.com/watch?v=VIDEO_ID
        if (url.contains("youtube.com/watch")) {
            int idx = url.indexOf("v=");
            if (idx != -1) {
                String id = url.substring(idx + 2);
                int ampIndex = id.indexOf('&');
                if (ampIndex != -1) {
                    id = id.substring(0, ampIndex);
                }
                return "https://www.youtube.com/embed/" + id;
            }
        }

        // youtu.be/VIDEO_ID
        if (url.contains("youtu.be/")) {
            int idx = url.indexOf("youtu.be/") + "youtu.be/".length();
            String id = url.substring(idx);
            int ampIndex = id.indexOf('?');
            if (ampIndex != -1) {
                id = id.substring(0, ampIndex);
            }
            return "https://www.youtube.com/embed/" + id;
        }

        // Si no es YouTube, devolvemos la url original (por si es otro tipo de video)
        return url;
    }
}
