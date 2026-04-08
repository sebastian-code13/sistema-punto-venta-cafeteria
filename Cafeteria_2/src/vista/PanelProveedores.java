package vista;

import dao.ProveedorDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PanelProveedores extends JPanel {
    private JTextField txtId, txtNombre, txtContacto, txtTelefono, txtDireccion;
    private JButton btnGuardar, btnActualizar, btnEliminar;
    private JTable tablaProveedores;
    private DefaultTableModel modelo;
    private ProveedorDAO dao = new ProveedorDAO();

    public PanelProveedores() {
        setLayout(new BorderLayout());

        JPanel formulario = new JPanel(new GridLayout(6, 2, 10, 10));
        formulario.setBorder(BorderFactory.createTitledBorder(
                "Gestión de Proveedores"));

        txtId = new JTextField(); txtId.setEditable(false);
        txtNombre = new JTextField();
        txtContacto = new JTextField();
        txtTelefono = new JTextField();
        txtDireccion = new JTextField();

        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");

        formulario.add(new JLabel("ID:"));
        formulario.add(txtId);
        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Correo:"));
        formulario.add(txtContacto);
        formulario.add(new JLabel("Teléfono:"));
        formulario.add(txtTelefono);
        formulario.add(new JLabel("Dirección:"));
        formulario.add(txtDireccion);
        formulario.add(btnGuardar);
        formulario.add(btnActualizar);

        add(formulario, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new String[]{
            "ID", "Nombre", "Contacto", "Teléfono", "Dirección"}, 0);
        tablaProveedores = new JTable(modelo);
        add(new JScrollPane(tablaProveedores), BorderLayout.CENTER);

        JPanel panelInferior = new JPanel();
        panelInferior.add(btnEliminar);
        add(panelInferior, BorderLayout.SOUTH);

        cargarTabla();

        btnGuardar.addActionListener(e -> guardarProveedor());
        btnActualizar.addActionListener(e -> actualizarProveedor());
        btnEliminar.addActionListener(e -> eliminarProveedor());
        tablaProveedores.getSelectionModel().addListSelectionListener
        (e -> cargarSeleccionado());
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        try (ResultSet rs = dao.obtenerProveedores()) {
            while (rs != null && rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("contacto"),
                        rs.getString("telefono"),
                        rs.getString("direccion")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar proveedores.");
        }
    }

    private void guardarProveedor() {
        String nombre = txtNombre.getText();
        String correo = txtContacto.getText();
        String telefono = txtTelefono.getText();
        String direccion = txtDireccion.getText();

        if (dao.agregarProveedor(nombre, correo, telefono, direccion)) {
            JOptionPane.showMessageDialog(this, "Proveedor agregado.");
            limpiarCampos();
            cargarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar proveedor.");
        }
    }

    private void actualizarProveedor() {
        try {
            int id = Integer.parseInt(txtId.getText());
            String nombre = txtNombre.getText();
            String correo = txtContacto.getText();
            String telefono = txtTelefono.getText();
            String direccion = txtDireccion.getText();

            if (dao.actualizarProveedor(
                    id, nombre, correo, telefono, direccion)) {
                JOptionPane.showMessageDialog(this, "Proveedor actualizado.");
                limpiarCampos();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Error al actualizar proveedor.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Datos inválidos.");
        }
    }

    private void eliminarProveedor() {
        int fila = tablaProveedores.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un proveedor.");
            return;
        }

        int id = Integer.parseInt(
                tablaProveedores.getValueAt(fila, 0).toString());
        int confirm = JOptionPane.showConfirmDialog(this, 
                "¿Eliminar proveedor?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION && dao.eliminarProveedor(id)) {
            JOptionPane.showMessageDialog(this, "Proveedor eliminado.");
            limpiarCampos();
            cargarTabla();
        }
    }

    private void cargarSeleccionado() {
        int fila = tablaProveedores.getSelectedRow();
        if (fila != -1) {
            txtId.setText(
                    tablaProveedores.getValueAt(fila, 0).toString());
            txtNombre.setText(
                    tablaProveedores.getValueAt(fila, 1).toString());
            txtContacto.setText(
                    tablaProveedores.getValueAt(fila, 2).toString());
            txtTelefono.setText(
                    tablaProveedores.getValueAt(fila, 3).toString());
            txtDireccion.setText(
                    tablaProveedores.getValueAt(fila, 4).toString());
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtContacto.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
    }
}
