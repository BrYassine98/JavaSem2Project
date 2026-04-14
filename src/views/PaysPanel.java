package views;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PaysPanel extends JPanel {
    public PaysPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Gestion des pays", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
