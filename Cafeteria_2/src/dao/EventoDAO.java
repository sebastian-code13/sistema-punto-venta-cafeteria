package dao;

import conexion.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO {

    public void agregarEvento(String titulo, String descripcion, 
                          java.sql.Date fecha_inicio, String tipo, String premio) {
        try (Connection conn = Conexion.getConexion()) {
            String sql = "INSERT INTO eventos "
                    + "(titulo, descripcion, fecha_inicio, tipo, premio) "
                    + "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, titulo);
            stmt.setString(2, descripcion);
            stmt.setDate(3, fecha_inicio);
            stmt.setString(4, tipo);
            stmt.setString(5, premio);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizarEvento(int id, String titulo, String descripcion, 
            java.sql.Date fecha_inicio, String tipo, String premio) {
        try (Connection conn = Conexion.getConexion()) {
            String sql = "UPDATE eventos SET titulo=?, descripcion=?, "
                    + "fecha_inicio=?, tipo=?, premio=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, titulo);
            stmt.setString(2, descripcion);
            stmt.setDate(3, fecha_inicio);
            stmt.setString(4, tipo);
            stmt.setString(5, premio);
            stmt.setInt(6, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarEvento(int id) {
        try (Connection conn = Conexion.getConexion()) {
            String sql = "DELETE FROM eventos WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String[]> obtenerEventos() {
        List<String[]> lista = new ArrayList<>();
        try (Connection conn = Conexion.getConexion()) {
            String sql = "SELECT * FROM eventos";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new String[]{
                    rs.getString("id"),
                    rs.getString("titulo"),
                    rs.getString("descripcion"),
                    rs.getString("fecha_inicio"),
                    rs.getString("tipo"),
                    rs.getString("premio")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
  
}
