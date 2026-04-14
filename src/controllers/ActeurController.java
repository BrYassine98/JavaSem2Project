package controllers;

import dao.ActeurDAO;
import java.util.List;
import models.Acteur;

public class ActeurController {
    private final ActeurDAO acteurDAO = new ActeurDAO();

    public List<Acteur> getActeurs() {
        return acteurDAO.findAll();
    }
}
