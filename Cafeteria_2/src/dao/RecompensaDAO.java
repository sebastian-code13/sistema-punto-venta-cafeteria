    package dao;

import conexion.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecompensaDAO {

    public void agregarRecompensa(String nombre, int puntos,
            String descripcion) {
        String sql = "INSERT INTO recompensas "
                + "(nombre, puntos_requeridos, descripcion) VALUES (?, ?, ?)";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setInt(2, puntos);
            stmt.setString(3, descripcion);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String[]> obtenerRecompensas() {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, puntos_requeridos, "
                + "descripcion FROM recompensas";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new String[]{
                    rs.getString("id"),
                    rs.getString("nombre"),
                    rs.getString("puntos_requeridos"),
                    rs.getString("descripcion")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void actualizarRecompensa(int id, String nombre,
            int puntos_requeridos, String descripcion) {
        String sql = "UPDATE recompensas SET nombre = ?, puntos_requeridos = ?,"
                + " descripcion = ? WHERE id = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setInt(2, puntos_requeridos);
            stmt.setString(3, descripcion);
            stmt.setInt(4, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarRecompensa(int id) {
        String sql = "DELETE FROM recompensas WHERE id = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
