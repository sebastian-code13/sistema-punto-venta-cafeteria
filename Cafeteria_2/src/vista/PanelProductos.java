package vista;

import conexion.Conexion;
import dao.ProductoDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class PanelProductos extends JPanel {
    private JTextField txtId, txtNombre, txtPrecio, txtStock;
    private JButton btnGuardar, btnActualizar, btnEliminar;
    private JTable tablaProductos;
    private DefaultTableModel modelo;
    private ProductoDAO dao = new ProductoDAO();
    private JComboBox<String> comboProveedores;
    private Map<String, Integer> mapaProveedores = new HashMap<>();
    private String rol;

public PanelProductos(String rol) {
    this(); 
    this.rol = rol;
    aplicarPermisosPorRol();
}

        private void aplicarPermisosPorRol() {
        if (rol.equalsIgnoreCase("cajero")) {
            btnGuardar.setEnabled(false);
            btnActualizar.setEnabled(false);
            btnEliminar.setEnabled(false);
        }
    }

    public PanelProductos() {
        setLayout(new BorderLayout());

        JPanel formulario = new JPanel(new GridLayout(6, 2, 10, 10));
        formulario.setBorder(BorderFactory.createTitledBorder(
                "Gestión de Productos"));

        txtId = new JTextField(); txtId.setEditable(false);
        txtNombre = new JTextField();
        txtPrecio = new JTextField();
        txtStock = new JTextField();
        comboProveedores = new JComboBox<>();
        cargarProveedores();

        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");

        formulario.add(new JLabel("ID:"));
        formulario.add(txtId);
        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Precio:"));
        formulario.add(txtPrecio);
        formulario.add(new JLabel("Stock:"));
        formulario.add(txtStock);
        formulario.add(new JLabel("Proveedor:"));
        formulario.add(comboProveedores);
        formulario.add(btnGuardar);
        formulario.add(btnActualizar);

        add(formulario, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new String[]{
            "ID", "Nombre", "Precio", "Stock", "Proveedor"}, 0);
        tablaProductos = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tablaProductos);
        add(scroll, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel();
        panelInferior.add(btnEliminar);
        add(panelInferior, BorderLayout.SOUTH);

        cargarTabla();

        btnGuardar.addActionListener(e -> guardarProducto());
        btnActualizar.addActionListener(e -> actualizarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        tablaProductos.getSelectionModel().addListSelectionListener
        (e -> cargarSeleccionado());
    }

    private void cargarProveedores() {
        try (Connection conn = Conexion.getConexion()) {
            String sql = "SELECT id, nombre FROM proveedores";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                int id = rs.getInt("id");
                comboProveedores.addItem(nombre);
                mapaProveedores.put(nombre, id);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar proveedores.");
        }
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        try (ResultSet rs = dao.obtenerProductos()) {
            while (rs != null && rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id_producto"),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getInt("stock"),
                    rs.getString("proveedor")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos.");
        }
    }

    private void guardarProducto() {
        try {
            String nombre = txtNombre.getText();
            double precio = Double.parseDouble(txtPrecio.getText());
            int stock = Integer.parseInt(txtStock.getText());
            String proveedorSeleccionado = (String) 
                    comboProveedores.getSelectedItem();
            int idProveedor = mapaProveedores.get(proveedorSeleccionado);

            if (dao.agregarProducto(nombre, precio, stock, idProveedor)) {
                JOptionPane.showMessageDialog(this, "Producto agregado.");
                limpiarCampos();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Datos inválidos.");
        }
    }

    private void actualizarProducto() {
        try {
            int id = Integer.parseInt(txtId.getText());
            String nombre = txtNombre.getText();
            double precio = Double.parseDouble(txtPrecio.getText());
            int stock = Integer.parseInt(txtStock.getText());
            String proveedorSeleccionado = (String) 
                    comboProveedores.getSelectedItem();
            int idProveedor = mapaProveedores.get(proveedorSeleccionado);

            if (dao.actualizarProducto(id, nombre, precio, stock, idProveedor)){
                JOptionPane.showMessageDialog(this, "Producto actualizado.");
                limpiarCampos();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Datos inválidos.");
        }
    }

    private void eliminarProducto() {
        try {
            int fila = tablaProductos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un producto.");
                return;
            }
            int id = Integer.parseInt(
                    tablaProductos.getValueAt(fila, 0).toString());
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "¿Eliminar producto?", "Confirmar", 
                            JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION && dao.eliminarProducto(id)) {
                JOptionPane.showMessageDialog(this, "Producto eliminado.");
                limpiarCampos();
                cargarTabla();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar.");
        }
    }

    private void cargarSeleccionado() {
        int fila = tablaProductos.getSelectedRow();
        if (fila != -1) {
            txtId.setText(
                    tablaProductos.getValueAt(fila, 0).toString());
            txtNombre.setText(
                    tablaProductos.getValueAt(fila, 1).toString());
            txtPrecio.setText(
                    tablaProductos.getValueAt(fila, 2).toString());
            txtStock.setText(
                    tablaProductos.getValueAt(fila, 3).toString());
            comboProveedores.setSelectedItem(
                    tablaProductos.getValueAt(fila, 4).toString());
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        comboProveedores.setSelectedIndex(0);
    }
}
