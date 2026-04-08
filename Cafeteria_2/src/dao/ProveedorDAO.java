package dao;

import conexion.Conexion;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class ProveedorDAO {

    public boolean agregarProveedor(String nombre, String contacto, 
            String telefono, String direccion) {
        String sql = "INSERT INTO proveedores "
                + "(nombre, contacto, telefono, direccion) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexion.getConexion(); 
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, contacto);
            ps.setString(3, telefono);
            ps.setString(4, direccion);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al agregar proveedor: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarProveedor(int id, String nombre, String contacto, 
            String telefono, String direccion) {
        String sql = "UPDATE proveedores SET nombre=?, contacto=?, telefono=?, "
                + "direccion=? WHERE id=?";
        try (Connection conn = Conexion.getConexion(); 
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, contacto);
            ps.setString(3, telefono);
            ps.setString(4, direccion);
            ps.setInt(5, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar proveedor: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarProveedor(int id) {
        String sql = "DELETE FROM proveedores WHERE id=?";
        try (Connection conn = Conexion.getConexion(); 
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar proveedor: " 
                    + e.getMessage());
            return false;
        }
    }

    public ResultSet obtenerProveedores() {
        String sql = "SELECT id, nombre, contacto, telefono, direccion "
                + "FROM proveedores";
        try {
            Connection conn = Conexion.getConexion();
            PreparedStatement ps = conn.prepareStatement(sql);
            return ps.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error al obtener proveedores: " 
                    + e.getMessage());
            return null;
        }
    }
}
