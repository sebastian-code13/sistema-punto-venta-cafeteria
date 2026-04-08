package dao;

import conexion.Conexion;
import modelo.DetalleVenta;
import modelo.Venta;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VentaDAO {

    public boolean registrarVenta(Venta venta, List<DetalleVenta> detalles) {
        String sqlVenta = "INSERT INTO ventas "
    + "(cliente_id, empleado_id, total, tipo_pago) VALUES (?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_venta (venta_id, producto_id,"
                + " cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        String sqlStockProductos = "SELECT stock FROM productos WHERE id = ?";
        String sqlActualizarProductos = "UPDATE productos SET "
                + "stock = stock - ? WHERE id = ?";
        String sqlActualizarInventario = "UPDATE inventario SET "
                + "cantidad = cantidad - ? WHERE producto_id = ?";
        

        try (Connection conn = Conexion.getConexion()) {
            conn.setAutoCommit(false);

            // Validar stock
            for (DetalleVenta detalle : detalles) {
                try (PreparedStatement psStock = conn.prepareStatement(
                        sqlStockProductos)) {
                    psStock.setInt(1, detalle.getProductoId());
                    try (ResultSet rs = psStock.executeQuery()) {
                        if (rs.next()) {
                            int stock = rs.getInt("stock");
                            if (stock < detalle.getCantidad()) {
                                conn.rollback();
                                System.out.println(
                                        "No hay suficiente stock para el "
                                                + "producto ID: " +
                                                detalle.getProductoId());
                                return false;
                            }
                        } else {
                            conn.rollback();
                            System.out.println("Producto no encontrado: ID "
                                    + detalle.getProductoId());
                            return false;
                        }
                    }
                }
            }

            // Insertar venta
            int ventaId;
            try (PreparedStatement psVenta = conn.prepareStatement(
                    sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                psVenta.setInt(1, venta.getClienteId());
                psVenta.setInt(2, venta.getEmpleadoId());
                psVenta.setBigDecimal(3, venta.getTotal());
                psVenta.setString(4, venta.getTipoPago()); 
                psVenta.executeUpdate();
                try (ResultSet rs = psVenta.getGeneratedKeys()) {
                    if (rs.next()) {
                        ventaId = rs.getInt(1);
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            }

            // Insertar detalles de venta
            try (PreparedStatement psDetalle = conn.prepareStatement(
                    sqlDetalle)) {
                for (DetalleVenta detalle : detalles) {
                    psDetalle.setInt(1, ventaId);
                    psDetalle.setInt(2, detalle.getProductoId());
                    psDetalle.setInt(3, detalle.getCantidad());
                    psDetalle.setBigDecimal(4, detalle.getPrecioUnitario());
                    psDetalle.addBatch();
                }
                psDetalle.executeBatch();
            }

            // Actualizar stock en productos e inventario
            try (PreparedStatement psProd = conn.prepareStatement(
                    sqlActualizarProductos);
                 PreparedStatement psInv = conn.prepareStatement(
                         sqlActualizarInventario)) {
                for (DetalleVenta detalle : detalles) {
                    psProd.setInt(1, detalle.getCantidad());
                    psProd.setInt(2, detalle.getProductoId());
                    psProd.addBatch();

                    psInv.setInt(1, detalle.getCantidad());
                    psInv.setInt(2, detalle.getProductoId());
                    psInv.addBatch();
                }
                psProd.executeBatch();
                psInv.executeBatch();
            }       

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // En VentaDAO.java
public List<Object[]> obtenerVentas() {
    List<Object[]> lista = new ArrayList<>();
    String sql = "SELECT id, cliente_id, empleado_id, fecha, total FROM ventas";

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Object[] fila = new Object[5];
            fila[0] = rs.getInt("id");
            fila[1] = rs.getInt("cliente_id");
            fila[2] = rs.getInt("empleado_id");
            fila[3] = rs.getTimestamp("fecha");
            fila[4] = rs.getBigDecimal("total");
            lista.add(fila);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return lista;
}

public double obtenerTotalVentasDelDia(int empleadoId) {
    double total = 0;
    String sql = "SELECT SUM(total) FROM ventas WHERE empleado_id = ? AND DATE(fecha) = CURDATE()";
    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, empleadoId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            total = rs.getDouble(1);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return total;
}

public Map<String, Double> obtenerTotalesPorTipoPagoDelDia(int empleadoId) {
    Map<String, Double> totales = new HashMap<>();
    String sql = "SELECT tipo_pago, SUM(total) AS total " +
                 "FROM ventas " +
                 "WHERE DATE(fecha) = CURDATE() AND empleado_id = ? " +
                 "GROUP BY tipo_pago";

    try (Connection conn = Conexion.getConexion();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, empleadoId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            String tipo = rs.getString("tipo_pago");
            double total = rs.getDouble("total");
            totales.put(tipo, total);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return totales;
}


}
