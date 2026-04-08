package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/cafeteria1POS";
    private static final String USUARIO = "gerente1";
    private static final String CLAVE = "contraseña1#"; // Modifica si tienes contraseña

    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}
