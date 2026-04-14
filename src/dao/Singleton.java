package dao;

import java.sql.Connection;
import java.sql.SQLException;

public final class Singleton {
    private static volatile Connection instance;

    private Singleton() {
    }

    public static Connection getConnection() throws SQLException {
        if (instance == null || instance.isClosed()) {
            synchronized (Singleton.class) {
                if (instance == null || instance.isClosed()) {
                    instance = DatabaseConnection.createConnection();
                }
            }
        }
        return instance;
    }

    public static void closeConnection() {
        if (instance == null) {
            return;
        }

        try {
            if (!instance.isClosed()) {
                instance.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la fermeture de la connexion.", e);
        }
    }
}
