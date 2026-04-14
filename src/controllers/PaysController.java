package controllers;

import dao.PaysDAO;
import java.util.List;
import models.Pays;

public class PaysController {
    private final PaysDAO paysDAO = new PaysDAO();

    public List<Pays> getPays() {
        return paysDAO.findAll();
    }
}
