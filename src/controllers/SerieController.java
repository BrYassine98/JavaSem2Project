package controllers;

import dao.SerieDAO;
import java.util.List;
import models.Serie;

public class SerieController {
    private final SerieDAO serieDAO = new SerieDAO();

    public List<Serie> getSeries() {
        return serieDAO.findAll();
    }
}
