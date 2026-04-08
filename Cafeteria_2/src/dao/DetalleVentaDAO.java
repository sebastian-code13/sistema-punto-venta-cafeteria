package dao;

import conexion.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleVentaDAO {

    public void registrarDetallesVenta(List<Object[]> detalles) {
        String sql = "INSERT INTO detalle_venta "
                + "(venta_id, producto_id, cantidad, precio_unitario) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (Object[] d : detalles) {
                stmt.setInt(1, (int) d[0]); // venta_id
                stmt.setInt(2, (int) d[1]); // producto_id
                stmt.setInt(3, (int) d[2]); // cantidad
                stmt.setDouble(4, (double) d[3]); // precio_unitario
                stmt.addBatch();
            }

            stmt.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Object[]> obtenerDetallesPorVenta(int ventaId) {
    List<Object[]> lista = new ArrayList<>();
    String sql = "SELECT id, venta_id, producto_id, cantidad,"
            + " precio_unitario FROM detalle_venta WHERE venta_id = ?";

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, ventaId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getInt("id");
                fila[1] = rs.getInt("venta_id");
                fila[2] = rs.getInt("producto_id");
                fila[3] = rs.getInt("cantidad");
                fila[4] = rs.getBigDecimal("precio_unitario");
                lista.add(fila);
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return lista;
}
}
