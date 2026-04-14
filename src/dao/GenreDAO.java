package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import models.Genre;

public class GenreDAO {
    public Genre add(Genre genre) {
        String sql = "INSERT INTO genre(nom) VALUES (?)";
        try {
            Connection connection = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, genre.getNom());
                stmt.executeUpdate();

                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        genre.setId(keys.getInt(1));
                    }
                }
                return genre;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout d'un genre.", e);
        }
    }

    public boolean update(Genre genre) {
        String sql = "UPDATE genre SET nom = ? WHERE id = ?";
        try {
            Connection connection = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, genre.getNom());
                stmt.setInt(2, genre.getId());
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise a jour d'un genre.", e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM genre WHERE id = ?";
        try {
            Connection connection = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, id);
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression d'un genre.", e);
        }
    }

    public Optional<Genre> findById(int id) {
        String sql = "SELECT id, nom FROM genre WHERE id = ?";
        try {
            Connection connection = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(new Genre(rs.getInt("id"), rs.getString("nom")));
                    }
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche d'un genre par ID.", e);
        }
    }

    public List<Genre> findAll() {
        String sql = "SELECT id, nom FROM genre ORDER BY nom";
        List<Genre> genres = new ArrayList<>();

        try {
            Connection connection = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    genres.add(new Genre(rs.getInt("id"), rs.getString("nom")));
                }
            }
            return genres;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du chargement des genres.", e);
        }
    }

    public Optional<Genre> findByFilmId(int filmId) {
        String sql = "SELECT g.id, g.nom FROM genre g JOIN film f ON f.genre_id = g.id WHERE f.id = ?";
        return findNested(sql, filmId);
    }

    public Optional<Genre> findBySerieId(int serieId) {
        String sql = "SELECT g.id, g.nom FROM genre g JOIN serie s ON s.genre_id = g.id WHERE s.id = ?";
        return findNested(sql, serieId);
    }

    private Optional<Genre> findNested(String sql, int parentId) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, parentId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(new Genre(rs.getInt("id"), rs.getString("nom")));
                    }
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche imbriquee d'un genre.", e);
        }
    }
}
