package views;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ActeurPanel extends JPanel {
    public ActeurPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Gestion des acteurs", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
