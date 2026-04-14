package views;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GenrePanel extends JPanel {
    public GenrePanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Gestion des genres", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
