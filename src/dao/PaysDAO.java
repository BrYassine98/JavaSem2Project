package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import models.Pays;

public class PaysDAO {
    public Pays add(Pays pays) {
        String sql = "INSERT INTO pays(nom) VALUES (?)";
        try {
            Connection connection = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, pays.getNom());
                stmt.executeUpdate();

                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        pays.setId(keys.getInt(1));
                    }
                }
                return pays;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout d'un pays.", e);
        }
    }

    public boolean update(Pays pays) {
        String sql = "UPDATE pays SET nom = ? WHERE id = ?";
        try {
            Connection connection = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, pays.getNom());
                stmt.setInt(2, pays.getId());
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise a jour d'un pays.", e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM pays WHERE id = ?";
        try {
            Connection connection = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, id);
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression d'un pays.", e);
        }
    }

    public Optional<Pays> findById(int id) {
        String sql = "SELECT id, nom FROM pays WHERE id = ?";
        try {
            Connection connection = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(new Pays(rs.getInt("id"), rs.getString("nom")));
                    }
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche d'un pays par ID.", e);
        }
    }

    public List<Pays> findAll() {
        String sql = "SELECT id, nom FROM pays ORDER BY nom";
        List<Pays> paysList = new ArrayList<>();

        try {
            Connection connection = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    paysList.add(new Pays(rs.getInt("id"), rs.getString("nom")));
                }
            }
            return paysList;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du chargement des pays.", e);
        }
    }

    public Optional<Pays> findByFilmId(int filmId) {
        String sql = "SELECT p.id, p.nom FROM pays p JOIN film f ON f.pays_id = p.id WHERE f.id = ?";
        return findNested(sql, filmId);
    }

    public Optional<Pays> findBySerieId(int serieId) {
        String sql = "SELECT p.id, p.nom FROM pays p JOIN serie s ON s.pays_id = p.id WHERE s.id = ?";
        return findNested(sql, serieId);
    }

    public Optional<Pays> findByActeurId(int acteurId) {
        String sql = "SELECT p.id, p.nom FROM pays p JOIN acteur a ON a.pays_id = p.id WHERE a.id = ?";
        return findNested(sql, acteurId);
    }

    private Optional<Pays> findNested(String sql, int parentId) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, parentId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(new Pays(rs.getInt("id"), rs.getString("nom")));
                    }
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche imbriquee d'un pays.", e);
        }
    }
}
