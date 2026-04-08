package dao;

import conexion.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParticipanteDAO {
    private Connection con;

    public ParticipanteDAO() throws SQLException {
        con = Conexion.getConexion();
    }

    public List<Object[]> listarParticipantes() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT * FROM participantes_evento";
        try (Statement st = con.createStatement(); 
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getInt("id");
                fila[1] = rs.getInt("evento_id");
                fila[2] = rs.getInt("cliente_id");
                fila[3] = rs.getDate("fecha_inscripcion");
                fila[4] = rs.getString("resultado");
                lista.add(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean agregarParticipante(int eventoId, int clienteId, 
            Date fecha, String resultado) {
        String sql = "INSERT INTO participantes_evento "
                + "(evento_id, cliente_id, fecha_inscripcion, resultado) "
                + "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, eventoId);
            ps.setInt(2, clienteId);
            ps.setDate(3, fecha);
            ps.setString(4, resultado);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarParticipante(int id, int eventoId, int clienteId, 
            Date fecha, String resultado) {
        String sql = "UPDATE participantes_evento SET "
                + "evento_id = ?, cliente_id = ?, fecha_inscripcion = ?, "
                + "resultado = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, eventoId);
            ps.setInt(2, clienteId);
            ps.setDate(3, fecha);
            ps.setString(4, resultado);
            ps.setInt(5, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarParticipante(int id) {
        String sql = "DELETE FROM participantes_evento WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
