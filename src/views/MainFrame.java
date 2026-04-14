package views;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("CinemaApp");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Films", new FilmPanel());
        tabs.addTab("Acteurs", new ActeurPanel());
        tabs.addTab("Series", new SeriePanel());
        tabs.addTab("Genres", new GenrePanel());
        tabs.addTab("Pays", new PaysPanel());

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
    }
}
