package trimestrepractica;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase para establecer la conexión con la base de datos MySQL
 */
public class ConexionBD {

    // URL de la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/empresaparquesdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    // Usuario y contraseña de conexión
    private static final String USER = "root";
    private static final String PASSWORD = "Studium2025#";

    /**
     * Método estático para obtener la conexión
     * @return Connection
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}