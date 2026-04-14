package controllers;

import dao.GenreDAO;
import java.util.List;
import models.Genre;

public class GenreController {
    private final GenreDAO genreDAO = new GenreDAO();

    public List<Genre> getGenres() {
        return genreDAO.findAll();
    }
}
