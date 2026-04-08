package vista;

import dao.EmpleadoDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PanelEmpleados extends JPanel {
    private JTextField txtId, txtNombre, txtUsuario, 
            txtContraseña, txtFechaEntrada;
    private JButton btnGuardar, btnActualizar, btnEliminar;
    private JTable tabla;
    private DefaultTableModel modelo;
    private EmpleadoDAO dao = new EmpleadoDAO();

    public PanelEmpleados() {
        setLayout(new BorderLayout());

        // Panel de formulario
        JPanel formulario = new JPanel(new GridLayout(6, 2, 10, 10));
        formulario.setBorder(BorderFactory.createTitledBorder(
                "Gestión de Empleados"));

        txtId = new JTextField(); txtId.setEditable(false);
        txtNombre = new JTextField();
        txtUsuario = new JTextField();
        txtContraseña = new JTextField();
        txtFechaEntrada = new JTextField(); 

        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");

        formulario.add(new JLabel("ID:"));
        formulario.add(txtId);
        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Usuario:"));
        formulario.add(txtUsuario);
        formulario.add(new JLabel("Contraseña:"));
        formulario.add(txtContraseña);
        formulario.add(new JLabel("Fecha Entrada (YYYY-MM-DD):"));
        formulario.add(txtFechaEntrada);
        formulario.add(btnGuardar);
        formulario.add(btnActualizar);

        add(formulario, BorderLayout.NORTH);

        // Tabla
        modelo = new DefaultTableModel(new String[]{
            "ID", "Nombre", "Usuario", "Contraseña", "Fecha Entrada"}, 0);
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel();
        panelInferior.add(btnEliminar);
        add(panelInferior, BorderLayout.SOUTH);

        cargarTabla();

        btnGuardar.addActionListener(e -> guardarEmpleado());
        btnActualizar.addActionListener(e -> actualizarEmpleado());
        btnEliminar.addActionListener(e -> eliminarEmpleado());
        tabla.getSelectionModel().addListSelectionListener
        (e -> cargarSeleccionado());
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        try (ResultSet rs = dao.obtenerEmpleados()) {
            while (rs != null && rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("usuario"),
                        rs.getString("contraseña"),
                        rs.getDate("fecha_ingreso")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar empleados.");
        }
    }

    private void guardarEmpleado() {
        try {
            String nombre = txtNombre.getText();
            String usuario = txtUsuario.getText();
            String contrasena = txtContraseña.getText();
            Date fechaEntrada = Date.valueOf(txtFechaEntrada.getText());

            if (dao.agregarEmpleado(nombre, usuario, contrasena, fechaEntrada)){
                JOptionPane.showMessageDialog(this, "Empleado agregado.");
                limpiarCampos();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Datos inválidos.");
        }
    }

    private void actualizarEmpleado() {
        try {
            int id = Integer.parseInt(txtId.getText());
            String nombre = txtNombre.getText();
            String usuario = txtUsuario.getText();
            String contrasena = txtContraseña.getText();
            Date fechaEntrada = Date.valueOf(txtFechaEntrada.getText());

            if (dao.actualizarEmpleado(
                    id, nombre, usuario, contrasena, fechaEntrada)) {
                JOptionPane.showMessageDialog(this, "Empleado actualizado.");
                limpiarCampos();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Datos inválidos.");
        }
    }

    private void eliminarEmpleado() {
        try {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un empleado.");
                return;
            }
            int id = Integer.parseInt(tabla.getValueAt(fila, 0).toString());
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "¿Eliminar empleado?", "Confirmar", 
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION && dao.eliminarEmpleado(id)) {
                JOptionPane.showMessageDialog(this, "Empleado eliminado.");
                limpiarCampos();
                cargarTabla();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar.");
        }
    }

    private void cargarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            txtId.setText(tabla.getValueAt(fila, 0).toString());
            txtNombre.setText(tabla.getValueAt(fila, 1).toString());
            txtUsuario.setText(tabla.getValueAt(fila, 2).toString());
            txtContraseña.setText(tabla.getValueAt(fila, 3).toString());
            txtFechaEntrada.setText(tabla.getValueAt(fila, 4).toString());
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtUsuario.setText("");
        txtContraseña.setText("");
        txtFechaEntrada.setText("");
    }
}
