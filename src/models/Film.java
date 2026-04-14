package models;

public class Film {
    private int id;
    private String titre;
    private int annee;
    private Genre genre;
    private Pays pays;

    public Film() {
    }

    public Film(int id, String titre, int annee, Genre genre, Pays pays) {
        this.id = id;
        this.titre = titre;
        this.annee = annee;
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

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
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
