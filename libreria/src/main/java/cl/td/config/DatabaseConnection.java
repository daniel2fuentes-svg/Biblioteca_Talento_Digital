package cl.td.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mariadb://localhost:3306/biblioteca_db";
    private static final String USER = "DB_USER";
    private static final String PASSWORD = "DB_PASS";

    public static Connection createConnection() {
        try {
            // 2. Cargamos el driver
            Class.forName("org.mariadb.jdbc.Driver");
            // 3. Devolvemos una conexión NUEVA directamente
            System.out.println("Solicitando nueva conexión a MariaDB...");
            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: No se encontró el Driver de MariaDB.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("ERROR: No se pudo conectar a " + URL);
            e.printStackTrace();
        }
        return null;
    }
}