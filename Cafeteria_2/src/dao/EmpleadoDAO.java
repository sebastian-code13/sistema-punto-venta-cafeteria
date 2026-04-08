package dao;

import conexion.Conexion;
import java.sql.*;
import modelo.Empleado;

public class EmpleadoDAO {

    // Agregar empleado
    public boolean agregarEmpleado(String nombre, String usuario, 
            String contrasena, Date fechaEntrada) {
        String sql = "INSERT INTO empleados "
                + "(nombre, usuario, contraseña, fecha_ingreso) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, usuario);
            ps.setString(3, contrasena);
            ps.setDate(4, fechaEntrada);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al agregar empleado: " + e.getMessage());
            return false;
        }
    }

    // Actualizar empleado
    public boolean actualizarEmpleado(int id, String nombre, String usuario, 
            String contrasena, Date fechaEntrada) {
        String sql = "UPDATE empleados SET "
                + "nombre = ?, usuario = ?, contraseña = ?, "
                + "fecha_ingreso = ? WHERE id = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, usuario);
            ps.setString(3, contrasena);
            ps.setDate(4, fechaEntrada);
            ps.setInt(5, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar empleado: " 
                    + e.getMessage());
            return false;
        }
    }

    // Eliminar empleado
    public boolean eliminarEmpleado(int id) {
        String sql = "DELETE FROM empleados WHERE id = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar empleado: " + e.getMessage());
            return false;
        }
    }

    // Obtener todos los empleados
    public ResultSet obtenerEmpleados() {
        String sql = "SELECT id, nombre, usuario, contraseña, "
                + "fecha_ingreso FROM empleados";
        try {
            Connection conn = Conexion.getConexion();
            Statement st = conn.createStatement();
            return st.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("Error al obtener empleados: " + e.getMessage());
            return null;
        }
    }

    // Autenticación de login
    public boolean autenticarEmpleado(String usuario, String contrasena) {
        String sql = "SELECT id FROM empleados "
                + "WHERE usuario = ? AND contraseña = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, contrasena);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error al autenticar: " + e.getMessage());
            return false;
        }
    }
    
      public boolean validarCredenciales(String usuario, String contraseña) {
    String sql = "SELECT * FROM empleados WHERE usuario = ? AND contraseña = ?";
    
    try (Connection conn = Conexion.getConexion();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, usuario);
        stmt.setString(2, contraseña);
        
        ResultSet rs = stmt.executeQuery();
        return rs.next(); // Si hay resultado, credenciales válidas
        
    } catch (SQLException e) {
        System.err.println("Error al validar credenciales: " + e.getMessage());
        return false;
    }
}
      
      public String obtenerRolPorUsuarioYClave(String usuario, String clave) {
    String sql = "SELECT rol FROM empleados WHERE usuario = ? AND contraseña = ?";
    try (Connection conn = Conexion.getConexion();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, usuario);
        stmt.setString(2, clave);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getString("rol"); // Puede ser "gerente" o "cajero"
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null; // Si no se encuentra
}
      
    public Empleado obtenerEmpleadoPorCredenciales(String usuario, String clave) {
        String sql = "SELECT id, rol FROM empleados WHERE usuario = ? AND contraseña = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario);
            stmt.setString(2, clave);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Empleado empleado = new Empleado();
                empleado.setId(rs.getInt("id"));
                empleado.setRol(rs.getString("rol"));
                return empleado;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
