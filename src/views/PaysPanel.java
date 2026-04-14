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
import dao.PaysDAO;
import models.Pays;

public class PaysPanel extends JPanel {
    private final PaysDAO paysDAO = new PaysDAO();
    private final DefaultTableModel tableModel;
    private final JTable table;

    private final JTextField idField = new JTextField();
    private final JTextField nomField = new JTextField();

    public PaysPanel() {
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

        addButton.addActionListener(e -> addPays());
        updateButton.addActionListener(e -> updatePays());
        deleteButton.addActionListener(e -> deletePays());
        refreshButton.addActionListener(e -> loadPays());
        clearButton.addActionListener(e -> clearForm());

        loadPays();
    }

    private void loadPays() {
        tableModel.setRowCount(0);
        List<Pays> paysList = paysDAO.findAll();
        for (Pays pays : paysList) {
            tableModel.addRow(new Object[]{pays.getId(), pays.getNom()});
        }
    }

    private void addPays() {
        String nom = nomField.getText().trim();
        if (nom.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le nom est obligatoire.");
            return;
        }

        paysDAO.add(new Pays(0, nom));
        loadPays();
        clearForm();
    }

    private void updatePays() {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selectionnez un pays a modifier.");
            return;
        }

        int id = Integer.parseInt(idField.getText());
        String nom = nomField.getText().trim();
        if (nom.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le nom est obligatoire.");
            return;
        }

        paysDAO.update(new Pays(id, nom));
        loadPays();
        clearForm();
    }

    private void deletePays() {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selectionnez un pays a supprimer.");
            return;
        }

        int id = Integer.parseInt(idField.getText());
        paysDAO.delete(id);
        loadPays();
        clearForm();
    }

    private void clearForm() {
        idField.setText("");
        nomField.setText("");
        table.clearSelection();
    }
}
