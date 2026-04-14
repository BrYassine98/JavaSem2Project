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
import dao.SerieDAO;
import models.Genre;
import models.Pays;
import models.Serie;

public class SeriePanel extends JPanel {
    private final SerieDAO serieDAO = new SerieDAO();
    private final DefaultTableModel tableModel;
    private final JTable table;

    private final JTextField idField = new JTextField();
    private final JTextField titreField = new JTextField();
    private final JTextField saisonsField = new JTextField();
    private final JTextField genreIdField = new JTextField();
    private final JTextField paysIdField = new JTextField();

    public SeriePanel() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Titre", "Saisons", "Genre", "Pays"}, 0) {
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
        formPanel.add(new javax.swing.JLabel("Saisons"));
        formPanel.add(saisonsField);
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
                saisonsField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
            }
        });

        addButton.addActionListener(e -> addSerie());
        updateButton.addActionListener(e -> updateSerie());
        deleteButton.addActionListener(e -> deleteSerie());
        refreshButton.addActionListener(e -> loadSeries());
        clearButton.addActionListener(e -> clearForm());

        loadSeries();
    }

    private void loadSeries() {
        tableModel.setRowCount(0);
        List<Serie> series = serieDAO.findAll();
        for (Serie serie : series) {
            tableModel.addRow(new Object[]{
                serie.getId(),
                serie.getTitre(),
                serie.getSaisons(),
                serie.getGenre() != null ? serie.getGenre().getNom() : "",
                serie.getPays() != null ? serie.getPays().getNom() : ""
            });
        }
    }

    private void addSerie() {
        Serie serie = buildSerieFromForm(false);
        if (serie == null) {
            return;
        }

        serieDAO.add(serie);
        loadSeries();
        clearForm();
    }

    private void updateSerie() {
        Serie serie = buildSerieFromForm(true);
        if (serie == null) {
            return;
        }

        serieDAO.update(serie);
        loadSeries();
        clearForm();
    }

    private void deleteSerie() {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selectionnez une serie a supprimer.");
            return;
        }

        serieDAO.delete(Integer.parseInt(idField.getText()));
        loadSeries();
        clearForm();
    }

    private Serie buildSerieFromForm(boolean requireId) {
        try {
            String titre = titreField.getText().trim();
            if (titre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Le titre est obligatoire.");
                return null;
            }

            int saisons = Integer.parseInt(saisonsField.getText().trim());
            int genreId = Integer.parseInt(genreIdField.getText().trim());
            int paysId = Integer.parseInt(paysIdField.getText().trim());

            int id = 0;
            if (requireId) {
                if (idField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Selectionnez une serie a modifier.");
                    return null;
                }
                id = Integer.parseInt(idField.getText());
            }

            Genre genre = new Genre(genreId, "");
            Pays pays = new Pays(paysId, "");
            return new Serie(id, titre, saisons, genre, pays);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Saisons, Genre ID et Pays ID doivent etre numeriques.");
            return null;
        }
    }

    private void clearForm() {
        idField.setText("");
        titreField.setText("");
        saisonsField.setText("");
        genreIdField.setText("");
        paysIdField.setText("");
        table.clearSelection();
    }
}
