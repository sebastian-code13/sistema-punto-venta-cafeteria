package dao;

import conexion.Conexion;
import java.sql.*;

public class ClienteDAO {
    
    public boolean agregarCliente(String nombre, String correo, 
            String telefono) {
        String sql = "INSERT INTO clientes "
                + "(nombre, correo, telefono) VALUES (?, ?, ?)";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, correo);
            stmt.setString(3, telefono);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al agregar cliente: " + e.getMessage());
            return false;
        }
    }

    public void agregarPuntosACliente(int clienteId, int puntos) {
    String sql = "UPDATE clientes SET puntos = puntos + ? WHERE id = ?";

    try (Connection conn = Conexion.getConexion();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, puntos);
        stmt.setInt(2, clienteId);
        stmt.executeUpdate();

    } catch (SQLException e) {
        System.err.println("Error al agregar puntos al cliente: " + e.getMessage());
    }
}

    public ResultSet obtenerClientes() {
        String sql = "SELECT * FROM clientes";
        try {
            Connection conn = Conexion.getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            return stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error al obtener clientes: " + e.getMessage());
            return null;
        }
    }

    public boolean actualizarCliente(int id, String nombre, 
            String correo, String telefono) {
        String sql = "UPDATE clientes SET "
                + "nombre=?, correo=?, telefono=? WHERE id=?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, correo);
            stmt.setString(3, telefono);
            stmt.setInt(4, id);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
           System.out.println("Error al actualizar cliente: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarCliente(int id) {
        String sql = "DELETE FROM clientes WHERE id=?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }
        public void actualizarPuntosCliente(int id, int puntosNuevos) 
                throws SQLException {
        String sql = "UPDATE clientes SET puntos = puntos + ? WHERE id = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, puntosNuevos);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }
        public boolean ClienteDAOPuntosCliente(int idCliente, int puntos) {
        String sql = "UPDATE clientes SET puntos = ? WHERE id = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, puntos);
            ps.setInt(2, idCliente);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
        
}
        public String obtenerNombreClientePorId(int clienteId) {
    String nombre = "";
    String sql = "SELECT nombre FROM clientes WHERE id = ?";
    try (Connection conn = Conexion.getConexion();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, clienteId);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                nombre = rs.getString("nombre");
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return nombre;
}

        public int obtenerPuntosCliente(int clienteId) {
    int puntos = 0;
    String sql = "SELECT puntos FROM clientes WHERE id = ?";
    try (Connection conn = Conexion.getConexion();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, clienteId);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                puntos = rs.getInt("puntos");
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return puntos;
}

}
