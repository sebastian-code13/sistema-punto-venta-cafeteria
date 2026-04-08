package vista;

import dao.RecompensaDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelRecompensas extends JPanel {
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtNombre, txtpuntos_requeridos, txtDescripcion;
    private RecompensaDAO recompensaDAO = new RecompensaDAO();

    public PanelRecompensas() {
        setLayout(new BorderLayout());

        // Panel de formulario
        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
                "Gestión de Recompensas"));

        txtNombre = new JTextField();
        txtpuntos_requeridos = new JTextField();
        txtDescripcion = new JTextField();

        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Puntos requeridos:"));
        panelFormulario.add(txtpuntos_requeridos);
        panelFormulario.add(new JLabel("Descripción:"));
        panelFormulario.add(txtDescripcion);

        JButton btnAgregar = new JButton("Agregar");
        panelFormulario.add(btnAgregar);

        add(panelFormulario, BorderLayout.NORTH);

        // Tabla
        modelo = new DefaultTableModel(new String[]{
            "ID", "Nombre", "Puntos", "Descripción"}, 0);
        tabla = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        // Botones de acciones
        JPanel panelBotones = new JPanel();
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        add(panelBotones, BorderLayout.SOUTH);

        // Eventos
        btnAgregar.addActionListener(e -> agregarRecompensa());
        btnModificar.addActionListener(e -> modificarRecompensa());
        btnEliminar.addActionListener(e -> eliminarRecompensa());

        cargarRecompensas();
    }

    private void agregarRecompensa() {
    String nombre = txtNombre.getText().trim();
    String puntosStr = txtpuntos_requeridos.getText().trim();
    String descripcion = txtDescripcion.getText().trim();

    if (nombre.isEmpty() || puntosStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, 
                "Por favor complete el nombre y los puntos requeridos.");
        return;
    }

    try {
        int puntos = Integer.parseInt(puntosStr);
        recompensaDAO.agregarRecompensa(nombre, puntos, descripcion);
        limpiarCampos();
        cargarRecompensas();
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, 
                "Los puntos deben ser un número válido.");
    }
}

    private void modificarRecompensa() {
    int fila = tabla.getSelectedRow();
    if (fila != -1) {
        String nombre = txtNombre.getText().trim();
        String puntosStr = txtpuntos_requeridos.getText().trim();
        String descripcion = txtDescripcion.getText().trim();

        if (nombre.isEmpty() || puntosStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "Por favor complete el nombre y los puntos requeridos.");
            return;
        }

        try {
            int id = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
            int puntos = Integer.parseInt(puntosStr);
            recompensaDAO.actualizarRecompensa(id, nombre, puntos, descripcion);
            limpiarCampos();
            cargarRecompensas();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                    "Los puntos deben ser un número válido.");
        }
    } else {
        JOptionPane.showMessageDialog(this, 
                "Seleccione una recompensa de la tabla para modificar.");
    }
}

    private void eliminarRecompensa() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            int id = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
            recompensaDAO.eliminarRecompensa(id);
            limpiarCampos();
            cargarRecompensas();
        }
    }

    private void cargarRecompensas() {
        modelo.setRowCount(0);
        List<String[]> lista = recompensaDAO.obtenerRecompensas();
        for (String[] fila : lista) {
            modelo.addRow(fila);
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtpuntos_requeridos.setText("");
        txtDescripcion.setText("");
    }

    // Para añadirlo fácilmente al menú lateral
    public static JPanel crearPanel() {
        return new PanelRecompensas();
    }
}
