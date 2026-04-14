package views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import dao.FilmDAO;
import models.Film;
import models.Genre;
import models.Pays;

public class FilmPanel extends JPanel {
    private final FilmDAO filmDAO = new FilmDAO();
    private final DefaultTableModel tableModel;
    private final JTable table;

    private final JTextField idField = new JTextField();
    private final JTextField titreField = new JTextField();
    private final JTextField anneeField = new JTextField();
    private final JTextField genreIdField = new JTextField();
    private final JTextField paysIdField = new JTextField();

    public FilmPanel() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Titre", "Annee", "Genre", "Pays"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 8, 8));
        idField.setEditable(false);
        formPanel.add(new javax.swing.JLabel("ID"));
        formPanel.add(idField);
        formPanel.add(new javax.swing.JLabel("Titre"));
        formPanel.add(titreField);
        formPanel.add(new javax.swing.JLabel("Annee"));
        formPanel.add(anneeField);
        formPanel.add(new javax.swing.JLabel("Genre ID"));
        formPanel.add(genreIdField);
        formPanel.add(new javax.swing.JLabel("Pays ID"));
        formPanel.add(paysIdField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Ajouter");
        JButton updateButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");
        JButton refreshButton = new JButton("Rafraichir");
        JButton clearButton = new JButton("Vider");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(clearButton);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int row = table.getSelectedRow();
                idField.setText(String.valueOf(tableModel.getValueAt(row, 0)));
                titreField.setText(String.valueOf(tableModel.getValueAt(row, 1)));
                anneeField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
            }
        });

        addButton.addActionListener(e -> addFilm());
        updateButton.addActionListener(e -> updateFilm());
        deleteButton.addActionListener(e -> deleteFilm());
        refreshButton.addActionListener(e -> loadFilms());
        clearButton.addActionListener(e -> clearForm());

        loadFilms();
    }

    private void loadFilms() {
        tableModel.setRowCount(0);
        List<Film> films = filmDAO.findAll();
        for (Film film : films) {
            tableModel.addRow(new Object[]{
                film.getId(),
                film.getTitre(),
                film.getAnnee(),
                film.getGenre() != null ? film.getGenre().getNom() : "",
                film.getPays() != null ? film.getPays().getNom() : ""
            });
        }
    }

    private void addFilm() {
        Film film = buildFilmFromForm(false);
        if (film == null) {
            return;
        }

        filmDAO.add(film);
        loadFilms();
        clearForm();
    }

    private void updateFilm() {
        Film film = buildFilmFromForm(true);
        if (film == null) {
            return;
        }

        filmDAO.update(film);
        loadFilms();
        clearForm();
    }

    private void deleteFilm() {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selectionnez un film a supprimer.");
            return;
        }

        filmDAO.delete(Integer.parseInt(idField.getText()));
        loadFilms();
        clearForm();
    }

    private Film buildFilmFromForm(boolean requireId) {
        try {
            String titre = titreField.getText().trim();
            if (titre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Le titre est obligatoire.");
                return null;
            }

            int annee = Integer.parseInt(anneeField.getText().trim());
            int genreId = Integer.parseInt(genreIdField.getText().trim());
            int paysId = Integer.parseInt(paysIdField.getText().trim());

            int id = 0;
            if (requireId) {
                if (idField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Selectionnez un film a modifier.");
                    return null;
                }
                id = Integer.parseInt(idField.getText());
            }

            Genre genre = new Genre(genreId, "");
            Pays pays = new Pays(paysId, "");
            return new Film(id, titre, annee, genre, pays);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Annee, Genre ID et Pays ID doivent etre numeriques.");
            return null;
        }
    }

    private void clearForm() {
        idField.setText("");
        titreField.setText("");
        anneeField.setText("");
        genreIdField.setText("");
        paysIdField.setText("");
        table.clearSelection();
    }
}
