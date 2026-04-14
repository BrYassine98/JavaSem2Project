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
import dao.GenreDAO;
import models.Genre;

public class GenrePanel extends JPanel {
    private final GenreDAO genreDAO = new GenreDAO();
    private final DefaultTableModel tableModel;
    private final JTable table;

    private final JTextField idField = new JTextField();
    private final JTextField nomField = new JTextField();

    public GenrePanel() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nom"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        idField.setEditable(false);
        formPanel.add(new javax.swing.JLabel("ID"));
        formPanel.add(idField);
        formPanel.add(new javax.swing.JLabel("Nom"));
        formPanel.add(nomField);

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
                nomField.setText(String.valueOf(tableModel.getValueAt(row, 1)));
            }
        });

        addButton.addActionListener(e -> addGenre());
        updateButton.addActionListener(e -> updateGenre());
        deleteButton.addActionListener(e -> deleteGenre());
        refreshButton.addActionListener(e -> loadGenres());
        clearButton.addActionListener(e -> clearForm());

        loadGenres();
    }

    private void loadGenres() {
        tableModel.setRowCount(0);
        List<Genre> genres = genreDAO.findAll();
        for (Genre genre : genres) {
            tableModel.addRow(new Object[]{genre.getId(), genre.getNom()});
        }
    }

    private void addGenre() {
        String nom = nomField.getText().trim();
        if (nom.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le nom est obligatoire.");
            return;
        }

        genreDAO.add(new Genre(0, nom));
        loadGenres();
        clearForm();
    }

    private void updateGenre() {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selectionnez un genre a modifier.");
            return;
        }

        int id = Integer.parseInt(idField.getText());
        String nom = nomField.getText().trim();
        if (nom.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le nom est obligatoire.");
            return;
        }

        genreDAO.update(new Genre(id, nom));
        loadGenres();
        clearForm();
    }

    private void deleteGenre() {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selectionnez un genre a supprimer.");
            return;
        }

        int id = Integer.parseInt(idField.getText());
        genreDAO.delete(id);
        loadGenres();
        clearForm();
    }

    private void clearForm() {
        idField.setText("");
        nomField.setText("");
        table.clearSelection();
    }
}
