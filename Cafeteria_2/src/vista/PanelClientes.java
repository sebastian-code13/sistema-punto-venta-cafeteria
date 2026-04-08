package vista;

import dao.ClienteDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PanelClientes extends JPanel {
    private JTextField txtId, txtNombre, txtCorreo, txtTelefono, txtPuntos;
    private JButton btnGuardar, btnActualizar, btnEliminar, btnActualizarPuntos;
    private JTable tablaClientes;
    private DefaultTableModel modelo;
    private ClienteDAO dao = new ClienteDAO();

    public PanelClientes() {
        setLayout(new BorderLayout());

        JPanel formulario = new JPanel(new GridLayout(6, 2, 10, 10));
        formulario.setBorder(BorderFactory.createTitledBorder(
                "Gestión de Clientes"));

        txtId = new JTextField(); txtId.setEditable(false);
        txtNombre = new JTextField();
        txtCorreo = new JTextField();
        txtTelefono = new JTextField();
        txtPuntos = new JTextField();  // Campo de puntos

        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnActualizarPuntos = new JButton("Actualizar Puntos");

        formulario.add(new JLabel("ID:"));
        formulario.add(txtId);
        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Correo:"));
        formulario.add(txtCorreo);
        formulario.add(new JLabel("Teléfono:"));
        formulario.add(txtTelefono);
        formulario.add(new JLabel("Puntos:"));
        formulario.add(txtPuntos);
        formulario.add(btnGuardar);
        formulario.add(btnActualizar);

        add(formulario, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new String[]{
            "ID", "Nombre", "Correo", "Teléfono", "Puntos"}, 0);
        tablaClientes = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tablaClientes);
        add(scroll, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel();
        panelInferior.add(btnEliminar);
        panelInferior.add(btnActualizarPuntos);  
        add(panelInferior, BorderLayout.SOUTH);

        cargarTabla();

        btnGuardar.addActionListener(e -> guardarCliente());
        btnActualizar.addActionListener(e -> actualizarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());
        btnActualizarPuntos.addActionListener(e -> actualizarPuntos());
        tablaClientes.getSelectionModel().addListSelectionListener
        (e -> cargarSeleccionado());
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        try (ResultSet rs = dao.obtenerClientes()) {
            while (rs != null && rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rs.getInt("puntos")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar clientes.");
        }
    }

    private void guardarCliente() {
        try {
            String nombre = txtNombre.getText();
            String correo = txtCorreo.getText();
            String telefono = txtTelefono.getText();

            if (dao.agregarCliente(nombre, correo, telefono)) {
                JOptionPane.showMessageDialog(this, "Cliente agregado.");
                limpiarCampos();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Datos inválidos.");
        }
    }

    private void actualizarCliente() {
        try {
            int id = Integer.parseInt(txtId.getText());
            String nombre = txtNombre.getText();
            String correo = txtCorreo.getText();
            String telefono = txtTelefono.getText();

            if (dao.actualizarCliente(id, nombre, correo, telefono)) {
                JOptionPane.showMessageDialog(this, "Cliente actualizado.");
                limpiarCampos();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Datos inválidos.");
        }
    }

    private void eliminarCliente() {
        try {
            int fila = tablaClientes.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un cliente.");
                return;
            }
            int id = Integer.parseInt(
                    tablaClientes.getValueAt(fila, 0).toString());
            int confirm = JOptionPane.showConfirmDialog(
this, "¿Eliminar cliente?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION && dao.eliminarCliente(id)) {
                JOptionPane.showMessageDialog(this, "Cliente eliminado.");
                limpiarCampos();
                cargarTabla();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar.");
        }
    }

    private void actualizarPuntos() {
        try {
            int id = Integer.parseInt(txtId.getText());
            int puntos = Integer.parseInt(txtPuntos.getText());

            if (dao.ClienteDAOPuntosCliente(id, puntos)) {
                JOptionPane.showMessageDialog(this, "Puntos actualizados.");
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Error al actualizar puntos.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Puntos inválidos.");
        }
    }

    private void cargarSeleccionado() {
        int fila = tablaClientes.getSelectedRow();
        if (fila != -1) {
            txtId.setText(tablaClientes.getValueAt(fila, 0).toString());
            txtNombre.setText(tablaClientes.getValueAt(fila, 1).toString());
            txtCorreo.setText(tablaClientes.getValueAt(fila, 2).toString());
            txtTelefono.setText(tablaClientes.getValueAt(fila, 3).toString());
            txtPuntos.setText(tablaClientes.getValueAt(fila, 4).toString());
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");
        txtPuntos.setText("");
    }
}
