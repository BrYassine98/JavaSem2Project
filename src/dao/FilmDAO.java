package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import models.Film;
import models.Genre;
import models.Pays;

public class FilmDAO {
    public Film add(Film film) {
        String sql = "INSERT INTO film(titre, annee, genre_id, pays_id) VALUES (?, ?, ?, ?)";
        try {
            Connection connection = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, film.getTitre());
                stmt.setInt(2, film.getAnnee());
                stmt.setInt(3, film.getGenre().getId());
                stmt.setInt(4, film.getPays().getId());
                stmt.executeUpdate();

                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        film.setId(keys.getInt(1));
                    }
                }
                return film;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout d'un film.", e);
        }
    }

    public boolean update(Film film) {
        String sql = "UPDATE film SET titre = ?, annee = ?, genre_id = ?, pays_id = ? WHERE id = ?";
        try {
            Connection connection = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, film.getTitre());
                stmt.setInt(2, film.getAnnee());
                stmt.setInt(3, film.getGenre().getId());
                stmt.setInt(4, film.getPays().getId());
                stmt.setInt(5, film.getId());
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise a jour d'un film.", e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM film WHERE id = ?";
        try {
            Connection connection = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, id);
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression d'un film.", e);
        }
    }

    public Optional<Film> findById(int id) {
        String sql = baseSelect() + " WHERE f.id = ?";
        try {
            Connection connection = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(mapFilm(rs));
                    }
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche d'un film par ID.", e);
        }
    }

    public List<Film> findAll() {
        String sql = baseSelect() + " ORDER BY f.titre";
        return queryFilms(sql, null);
    }

    public List<Film> findByGenre(int genreId) {
        String sql = baseSelect() + " WHERE f.genre_id = ? ORDER BY f.titre";
        return queryFilms(sql, genreId);
    }

    public List<Film> findByPays(int paysId) {
        String sql = baseSelect() + " WHERE f.pays_id = ? ORDER BY f.titre";
        return queryFilms(sql, paysId);
    }

    public List<Film> findByActeur(int acteurId) {
        String sql = baseSelect()
            + " JOIN film_acteur fa ON fa.film_id = f.id"
            + " WHERE fa.acteur_id = ? ORDER BY f.titre";
        return queryFilms(sql, acteurId);
    }

    private String baseSelect() {
        return "SELECT f.id AS f_id, f.titre, f.annee,"
            + " g.id AS g_id, g.nom AS g_nom,"
            + " p.id AS p_id, p.nom AS p_nom"
            + " FROM film f"
            + " JOIN genre g ON g.id = f.genre_id"
            + " JOIN pays p ON p.id = f.pays_id";
    }

    private List<Film> queryFilms(String sql, Integer parameter) {
        List<Film> films = new ArrayList<>();

        try {
            Connection connection = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                if (parameter != null) {
                    stmt.setInt(1, parameter);
                }

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        films.add(mapFilm(rs));
                    }
                }
            }
            return films;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du chargement des films.", e);
        }
    }

    private Film mapFilm(ResultSet rs) throws SQLException {
        Genre genre = new Genre(rs.getInt("g_id"), rs.getString("g_nom"));
        Pays pays = new Pays(rs.getInt("p_id"), rs.getString("p_nom"));
        return new Film(rs.getInt("f_id"), rs.getString("titre"), rs.getInt("annee"), genre, pays);
    }
}
