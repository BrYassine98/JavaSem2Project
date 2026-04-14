package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import models.Serie;
import models.Genre;
import models.Pays;

public class SerieDAO {
    public Serie add(Serie serie) {
        String sql = "INSERT INTO serie(titre, saisons, genre_id, pays_id) VALUES (?, ?, ?, ?)";
        try {
            Connection connection = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, serie.getTitre());
                stmt.setInt(2, serie.getSaisons());
                stmt.setInt(3, serie.getGenre().getId());
                stmt.setInt(4, serie.getPays().getId());
                stmt.executeUpdate();

                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        serie.setId(keys.getInt(1));
                    }
                }
                return serie;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout d'une serie.", e);
        }
    }

    public boolean update(Serie serie) {
        String sql = "UPDATE serie SET titre = ?, saisons = ?, genre_id = ?, pays_id = ? WHERE id = ?";
        try {
            Connection connection = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, serie.getTitre());
                stmt.setInt(2, serie.getSaisons());
                stmt.setInt(3, serie.getGenre().getId());
                stmt.setInt(4, serie.getPays().getId());
                stmt.setInt(5, serie.getId());
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise a jour d'une serie.", e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM serie WHERE id = ?";
        try {
            Connection connection = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, id);
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression d'une serie.", e);
        }
    }

    public Optional<Serie> findById(int id) {
        String sql = baseSelect() + " WHERE s.id = ?";
        try {
            Connection connection = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(mapSerie(rs));
                    }
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche d'une serie par ID.", e);
        }
    }

    public List<Serie> findAll() {
        String sql = baseSelect() + " ORDER BY s.titre";
        return querySeries(sql, null);
    }

    public List<Serie> findByGenre(int genreId) {
        String sql = baseSelect() + " WHERE s.genre_id = ? ORDER BY s.titre";
        return querySeries(sql, genreId);
    }

    public List<Serie> findByPays(int paysId) {
        String sql = baseSelect() + " WHERE s.pays_id = ? ORDER BY s.titre";
        return querySeries(sql, paysId);
    }

    public List<Serie> findByActeur(int acteurId) {
        String sql = baseSelect()
            + " JOIN serie_acteur sa ON sa.serie_id = s.id"
            + " WHERE sa.acteur_id = ? ORDER BY s.titre";
        return querySeries(sql, acteurId);
    }

    private String baseSelect() {
        return "SELECT s.id AS s_id, s.titre, s.saisons,"
            + " g.id AS g_id, g.nom AS g_nom,"
            + " p.id AS p_id, p.nom AS p_nom"
            + " FROM serie s"
            + " JOIN genre g ON g.id = s.genre_id"
            + " JOIN pays p ON p.id = s.pays_id";
    }

    private List<Serie> querySeries(String sql, Integer parameter) {
        List<Serie> series = new ArrayList<>();

        try {
            Connection connection = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                if (parameter != null) {
                    stmt.setInt(1, parameter);
                }

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        series.add(mapSerie(rs));
                    }
                }
            }
            return series;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du chargement des series.", e);
        }
    }

    private Serie mapSerie(ResultSet rs) throws SQLException {
        Genre genre = new Genre(rs.getInt("g_id"), rs.getString("g_nom"));
        Pays pays = new Pays(rs.getInt("p_id"), rs.getString("p_nom"));
        return new Serie(rs.getInt("s_id"), rs.getString("titre"), rs.getInt("saisons"), genre, pays);
    }
}
