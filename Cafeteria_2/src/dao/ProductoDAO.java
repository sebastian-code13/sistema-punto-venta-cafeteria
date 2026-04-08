package dao;

import conexion.Conexion;
import modelo.Productos;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    
    public ResultSet obtenerProductos() {
    try {
        Connection conn = Conexion.getConexion();
        String sql = """
            SELECT p.id AS id_producto, p.nombre, p.precio, p.stock, 
            pr.nombre AS proveedor FROM productos p
            LEFT JOIN proveedores pr ON p.proveedor_id = pr.id
        """;
        PreparedStatement ps = conn.prepareStatement(sql);
        return ps.executeQuery();
    } catch (SQLException e) {
        System.out.println("Error al obtener productos: " + e.getMessage());
        return null;
    }
}

    public boolean agregarProducto(String nombre, double precio, 
            int stock, int proveedor_id) {
        String sql = "INSERT INTO productos "
                + "(nombre, precio, stock, proveedor_id) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexion.getConexion(); 
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setDouble(2, precio);
            ps.setInt(3, stock);
            ps.setInt(4, proveedor_id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al agregar producto: " + e.getMessage());
            return false;
        }
    }

    private Connection conexion;

    public ProductoDAO() {
        try {
            conexion = Conexion.getConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> obtenerNombresProductos() {
        List<String> nombres = new ArrayList<>();
        String sql = "SELECT nombre FROM producto";
        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                nombres.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener nombres de productos: " 
                    + e.getMessage());
        }
        return nombres;
    }

    
        public List<Productos> obtenerTodosLosProductos() {
        List<Productos> productos = new ArrayList<>();
        String sql = "SELECT id, nombre FROM productos";

        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Productos producto = new Productos();
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("nombre"));
                productos.add(producto);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener productos: " + e.getMessage());
        }

        return productos;
    }
    
    public boolean actualizarProducto(int id, String nombre, double precio, 
            int stock, int proveedor_id) {
        try {
            Connection conn = Conexion.getConexion();
            String sql = "UPDATE productos SET nombre=?, precio=?, "
                    + "stock=?, proveedor_id=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setDouble(2, precio);
            ps.setInt(3, stock);
            ps.setInt(4, proveedor_id);
            ps.setInt(5, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar producto: " 
                    + e.getMessage());
            return false;
        }
    }

    public boolean eliminarProducto(int id) {
        try {
            Connection conn = Conexion.getConexion();
            String sql = "DELETE FROM productos WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }
    
    public String obtenerNombreProductoPorId(int productoId) {
    String nombre = "";
    String sql = "SELECT nombre FROM productos WHERE id = ?";

    try (Connection conn = Conexion.getConexion();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, productoId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            nombre = rs.getString("nombre");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return nombre;
}

    public int obtenerIdPorNombre(String nombreProducto) {
    int id = -1;
    String sql = "SELECT id FROM productos WHERE nombre = ?";
    try (Connection conn = Conexion.getConexion();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, nombreProducto);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                id = rs.getInt("id");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return id;
}
    
    public double obtenerPrecioPorNombre(String nombre) {
    double precio = 0.0;
    String sql = "SELECT precio FROM productos WHERE nombre = ?";

    try (Connection conn = Conexion.getConexion();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, nombre);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            precio = rs.getDouble("precio");
        }

    } catch (SQLException e) {
        System.err.println("Error al obtener precio del producto: " + e.getMessage());
    }

    return precio;
}


}
