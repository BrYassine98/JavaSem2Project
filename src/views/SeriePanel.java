package views;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class SeriePanel extends JPanel {
    public SeriePanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Gestion des series", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
