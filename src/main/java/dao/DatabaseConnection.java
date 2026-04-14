import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private final String url = "jdbc:postgresql://localhost:5432/cinema_db";
    private final String user = "cinema_user";
    private final String password = "your_password"; // Replace with your actual password

    public Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connection to PostgreSQL database established!");
        } catch (SQLException e) {
            System.out.println("Connection failure!");
            e.printStackTrace();
        }
        return connection;
    }
}