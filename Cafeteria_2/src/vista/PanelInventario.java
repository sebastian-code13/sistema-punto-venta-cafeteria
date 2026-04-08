package vista;

import dao.InventarioDAO;
import conexion.Conexion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanelInventario extends JPanel {
    private JComboBox<String> comboProductos;
    private JTextField txtCantidad, txtDescripcion;
    private JComboBox<String> comboTipo;
    private DefaultTableModel modelo;
    private JTable tabla;
    private InventarioDAO dao = new InventarioDAO();
    private Map<String, Integer> mapaProductos = new HashMap<>();

    public PanelInventario() {
        setLayout(new BorderLayout());

        // Formulario
        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
                "Registrar Movimiento"));

        comboProductos = new JComboBox<>();
        cargarProductos();

        txtCantidad = new JTextField();
        txtDescripcion = new JTextField();
        comboTipo = new JComboBox<>(new String[]{"entrada", "salida"});

        JButton btnRegistrar = new JButton("Registrar");

        panelFormulario.add(new JLabel("Producto:"));
        panelFormulario.add(comboProductos);
        panelFormulario.add(new JLabel("Cantidad:"));
        panelFormulario.add(txtCantidad);
        panelFormulario.add(new JLabel("Tipo de Movimiento:"));
        panelFormulario.add(comboTipo);
        panelFormulario.add(new JLabel("Descripción:"));
        panelFormulario.add(txtDescripcion);
        panelFormulario.add(new JLabel(""));
        panelFormulario.add(btnRegistrar);

        add(panelFormulario, BorderLayout.NORTH);

        // Tabla
        modelo = new DefaultTableModel(new String[]{
            "ID", "Producto", "Cantidad", "Tipo", "Fecha", "Descripción"}, 0);
        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarMovimientos();

        // Acción del botón
        btnRegistrar.addActionListener(e -> {
            try {
                String nombreProducto = (String) 
                        comboProductos.getSelectedItem();
                int productoId = mapaProductos.get(nombreProducto);
                int cantidad = Integer.parseInt(txtCantidad.getText());
                String tipo = (String) comboTipo.getSelectedItem();
                String descripcion = txtDescripcion.getText();

                if (dao.registrarMovimiento(
                        productoId, cantidad, tipo, descripcion)) {
                    JOptionPane.showMessageDialog(this, 
                            "Movimiento registrado.");
                    txtCantidad.setText("");
                    txtDescripcion.setText("");
                    cargarMovimientos();
                } else {
                    JOptionPane.showMessageDialog(this, 
                            "Error al registrar movimiento.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Datos inválidos.");
            }
        });
    }

    private void cargarProductos() {
        mapaProductos.clear();
        comboProductos.removeAllItems();
        String sql = "SELECT id, nombre FROM productos";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                comboProductos.addItem(nombre);
                mapaProductos.put(nombre, id);
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar productos: " + e.getMessage());
        }
    }

    private void cargarMovimientos() {
        modelo.setRowCount(0);
        List<String[]> movimientos = dao.obtenerMovimientos();
        for (String[] fila : movimientos) {
            modelo.addRow(fila);
        }
    }
}
