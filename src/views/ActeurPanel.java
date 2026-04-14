package views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.table.DefaultTableModel;
import dao.ActeurDAO;
import models.Acteur;
import models.Pays;

public class ActeurPanel extends JPanel {
    private final ActeurDAO acteurDAO = new ActeurDAO();
    private final DefaultTableModel tableModel;
    private final JTable table;

    private final JTextField idField = new JTextField();
    private final JTextField nomField = new JTextField();
    private final JTextField prenomField = new JTextField();
    private final JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
    private final JTextField paysIdField = new JTextField();

    public ActeurPanel() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nom", "Prenom", "Date naissance", "Pays"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 8, 8));
        idField.setEditable(false);
        formPanel.add(new javax.swing.JLabel("ID"));
        formPanel.add(idField);
        formPanel.add(new javax.swing.JLabel("Nom"));
        formPanel.add(nomField);
        formPanel.add(new javax.swing.JLabel("Prenom"));
        formPanel.add(prenomField);
        formPanel.add(new javax.swing.JLabel("Date naissance"));
        formPanel.add(dateSpinner);
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
                nomField.setText(String.valueOf(tableModel.getValueAt(row, 1)));
                prenomField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
            }
        });

        addButton.addActionListener(e -> addActeur());
        updateButton.addActionListener(e -> updateActeur());
        deleteButton.addActionListener(e -> deleteActeur());
        refreshButton.addActionListener(e -> loadActeurs());
        clearButton.addActionListener(e -> clearForm());

        loadActeurs();
    }

    private void loadActeurs() {
        tableModel.setRowCount(0);
        List<Acteur> acteurs = acteurDAO.findAll();
        for (Acteur acteur : acteurs) {
            tableModel.addRow(new Object[]{
                acteur.getId(),
                acteur.getNom(),
                acteur.getPrenom(),
                acteur.getDateNaissance() != null ? acteur.getDateNaissance() : "",
                acteur.getPays() != null ? acteur.getPays().getNom() : ""
            });
        }
    }

    private void addActeur() {
        Acteur acteur = buildActeurFromForm(false);
        if (acteur == null) {
            return;
        }

        acteurDAO.add(acteur);
        loadActeurs();
        clearForm();
    }

    private void updateActeur() {
        Acteur acteur = buildActeurFromForm(true);
        if (acteur == null) {
            return;
        }

        acteurDAO.update(acteur);
        loadActeurs();
        clearForm();
    }

    private void deleteActeur() {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selectionnez un acteur a supprimer.");
            return;
        }

        acteurDAO.delete(Integer.parseInt(idField.getText()));
        loadActeurs();
        clearForm();
    }

    private Acteur buildActeurFromForm(boolean requireId) {
        try {
            String nom = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            if (nom.isEmpty() || prenom.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nom et prenom sont obligatoires.");
                return null;
            }

            int id = 0;
            if (requireId) {
                if (idField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Selectionnez un acteur a modifier.");
                    return null;
                }
                id = Integer.parseInt(idField.getText());
            }

            java.util.Date selectedDate = (java.util.Date) dateSpinner.getValue();
            LocalDate dateNaissance = new Date(selectedDate.getTime()).toLocalDate();

            String paysValue = paysIdField.getText().trim();
            Pays pays = null;
            if (!paysValue.isEmpty()) {
                pays = new Pays(Integer.parseInt(paysValue), "");
            }

            return new Acteur(id, nom, prenom, dateNaissance, pays);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Pays ID doit etre numerique.");
            return null;
        }
    }

    private void clearForm() {
        idField.setText("");
        nomField.setText("");
        prenomField.setText("");
        paysIdField.setText("");
        table.clearSelection();
    }
}
