package controllers;

import dao.FilmDAO;
import java.util.List;
import models.Film;

public class FilmController {
    private final FilmDAO filmDAO = new FilmDAO();

    public List<Film> getFilms() {
        return filmDAO.findAll();
    }
}
