package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import models.Acteur;
import models.Pays;

public class ActeurDAO {
    public Acteur add(Acteur acteur) {
        String sql = "INSERT INTO acteur(nom, prenom, date_naissance, pays_id) VALUES (?, ?, ?, ?)";
        try {
            Connection connection = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, acteur.getNom());
                stmt.setString(2, acteur.getPrenom());
                if (acteur.getDateNaissance() == null) {
                    stmt.setDate(3, null);
                } else {
                    stmt.setDate(3, Date.valueOf(acteur.getDateNaissance()));
                }
                if (acteur.getPays() == null) {
                    stmt.setObject(4, null);
                } else {
                    stmt.setInt(4, acteur.getPays().getId());
                }
                stmt.executeUpdate();

                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        acteur.setId(keys.getInt(1));
                    }
                }
                return acteur;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout d'un acteur.", e);
        }
    }

    public boolean update(Acteur acteur) {
        String sql = "UPDATE acteur SET nom = ?, prenom = ?, date_naissance = ?, pays_id = ? WHERE id = ?";
        try {
            Connection connection = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, acteur.getNom());
                stmt.setString(2, acteur.getPrenom());
                if (acteur.getDateNaissance() == null) {
                    stmt.setDate(3, null);
                } else {
                    stmt.setDate(3, Date.valueOf(acteur.getDateNaissance()));
                }
                if (acteur.getPays() == null) {
                    stmt.setObject(4, null);
                } else {
                    stmt.setInt(4, acteur.getPays().getId());
                }
                stmt.setInt(5, acteur.getId());
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise a jour d'un acteur.", e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM acteur WHERE id = ?";
        try {
            Connection connection = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, id);
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression d'un acteur.", e);
        }
    }

    public Optional<Acteur> findById(int id) {
        String sql = baseSelect() + " WHERE a.id = ?";
        try {
            Connection connection = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(mapActeur(rs));
                    }
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche d'un acteur par ID.", e);
        }
    }

    public List<Acteur> findAll() {
        String sql = baseSelect() + " ORDER BY a.nom, a.prenom";
        return queryActeurs(sql, null);
    }

    public List<Acteur> findByFilm(int filmId) {
        String sql = baseSelect()
            + " JOIN film_acteur fa ON fa.acteur_id = a.id"
            + " WHERE fa.film_id = ? ORDER BY a.nom, a.prenom";
        return queryActeurs(sql, filmId);
    }

    public List<Acteur> findBySerie(int serieId) {
        String sql = baseSelect()
            + " JOIN serie_acteur sa ON sa.acteur_id = a.id"
            + " WHERE sa.serie_id = ? ORDER BY a.nom, a.prenom";
        return queryActeurs(sql, serieId);
    }

    public List<Acteur> findByPays(int paysId) {
        String sql = baseSelect() + " WHERE a.pays_id = ? ORDER BY a.nom, a.prenom";
        return queryActeurs(sql, paysId);
    }

    private String baseSelect() {
        return "SELECT a.id AS a_id, a.nom AS a_nom, a.prenom AS a_prenom, a.date_naissance,"
            + " p.id AS p_id, p.nom AS p_nom"
            + " FROM acteur a"
            + " LEFT JOIN pays p ON p.id = a.pays_id";
    }

    private List<Acteur> queryActeurs(String sql, Integer parameter) {
        List<Acteur> acteurs = new ArrayList<>();

        try {
            Connection connection = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                if (parameter != null) {
                    stmt.setInt(1, parameter);
                }

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        acteurs.add(mapActeur(rs));
                    }
                }
            }
            return acteurs;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du chargement des acteurs.", e);
        }
    }

    private Acteur mapActeur(ResultSet rs) throws SQLException {
        Integer paysId = (Integer) rs.getObject("p_id");
        Pays pays = paysId == null ? null : new Pays(paysId, rs.getString("p_nom"));
        Date dateSql = rs.getDate("date_naissance");
        LocalDate date = dateSql == null ? null : dateSql.toLocalDate();

        return new Acteur(
            rs.getInt("a_id"),
            rs.getString("a_nom"),
            rs.getString("a_prenom"),
            date,
            pays
        );
    }
}
