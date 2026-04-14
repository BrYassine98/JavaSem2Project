package views;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class FilmPanel extends JPanel {
    public FilmPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Gestion des films", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
