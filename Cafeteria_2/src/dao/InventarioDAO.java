package dao;

import conexion.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventarioDAO {

    public boolean registrarMovimiento(int productoId, int cantidad, 
            String tipoMovimiento, String descripcion) {
        String sql = "INSERT INTO inventario (producto_id, cantidad, "
                + "tipo_movimiento, descripcion) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexion.getConexion(); 
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productoId);
            stmt.setInt(2, cantidad);
            stmt.setString(3, tipoMovimiento);
            stmt.setString(4, descripcion);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al registrar movimiento: " 
                    + e.getMessage());
            return false;
        }
    }

    public List<String[]> obtenerMovimientos() {
        List<String[]> lista = new ArrayList<>();
        String sql = """
            SELECT i.id, p.nombre AS nombre_producto, i.cantidad, 
                     i.tipo_movimiento, i.fecha, i.descripcion
            FROM inventario i
            JOIN productos p ON i.producto_id = p.id
            ORDER BY i.fecha DESC
        """;
        try (Connection conn = Conexion.getConexion(); 
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new String[]{
                    String.valueOf(rs.getInt("id")),
                    rs.getString("nombre_producto"),
                    String.valueOf(rs.getInt("cantidad")),
                    rs.getString("tipo_movimiento"),
                    rs.getString("fecha"),
                    rs.getString("descripcion")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener movimientos: "
                    + e.getMessage());
        }
        return lista;
    }
}
