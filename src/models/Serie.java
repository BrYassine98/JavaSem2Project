package models;

public class Serie {
    private int id;
    private String titre;
    private int saisons;
    private Genre genre;
    private Pays pays;

    public Serie() {
    }

    public Serie(int id, String titre, int saisons, Genre genre, Pays pays) {
        this.id = id;
        this.titre = titre;
        this.saisons = saisons;
        this.genre = genre;
        this.pays = pays;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public int getSaisons() {
        return saisons;
    }

    public void setSaisons(int saisons) {
        this.saisons = saisons;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Pays getPays() {
        return pays;
    }

    public void setPays(Pays pays) {
        this.pays = pays;
    }
}
