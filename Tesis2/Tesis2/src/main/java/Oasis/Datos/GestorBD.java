package Oasis.Datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GestorBD {
    private static String url = "jdbc:mysql://localhost:3306/wandy";
    private static String user = "Wandy";
    private static String password = "123";

    private static Connection conn = null;

    public static Connection getConn() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = conectar();
        }
        return conn;
    }

    public static void cerrar() {
        try {conn.close();} catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Connection conectar() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}