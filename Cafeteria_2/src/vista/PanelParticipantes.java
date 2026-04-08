package vista;

import dao.ParticipanteDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class PanelParticipantes extends JPanel {
    private JTextField txtId, txtEventoId, txtClienteId, txtFecha, txtResultado;
    private JButton btnAgregar, btnActualizar, btnEliminar;
    private JTable tabla;
    private DefaultTableModel modelo;
    private ParticipanteDAO dao;

    public PanelParticipantes() throws SQLException {
        dao = new ParticipanteDAO();
        setLayout(new BorderLayout());

        // Formulario
        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
                "Participante"));

        txtId = new JTextField(); txtId.setEditable(false);
        txtEventoId = new JTextField();
        txtClienteId = new JTextField();
        txtFecha = new JTextField();
        txtResultado = new JTextField();

        btnAgregar = new JButton("Agregar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");

        panelFormulario.add(new JLabel("ID:"));
        panelFormulario.add(txtId);
        panelFormulario.add(new JLabel("ID Evento:"));
        panelFormulario.add(txtEventoId);
        panelFormulario.add(new JLabel("ID Cliente:"));
        panelFormulario.add(txtClienteId);
        panelFormulario.add(new JLabel("Fecha Inscripción (AAAA-MM-DD):"));
        panelFormulario.add(txtFecha);
        panelFormulario.add(new JLabel("Resultado:"));
        panelFormulario.add(txtResultado);
        panelFormulario.add(btnAgregar);
        panelFormulario.add(btnActualizar);

        add(panelFormulario, BorderLayout.NORTH);

        // Tabla
        modelo = new DefaultTableModel(new String[]{
            "ID", "Evento ID", "Cliente ID", "Fecha", "Resultado"}, 0);
        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Botón eliminar
        JPanel panelBoton = new JPanel();
        panelBoton.add(btnEliminar);
        add(panelBoton, BorderLayout.SOUTH);

        // Eventos
        btnAgregar.addActionListener(e -> agregarParticipante());
        btnActualizar.addActionListener(e -> actualizarParticipante());
        btnEliminar.addActionListener(e -> eliminarParticipante());
        tabla.getSelectionModel().addListSelectionListener
        (e -> cargarSeleccionado());

        cargarTabla();
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        List<Object[]> participantes = dao.listarParticipantes();
        for (Object[] fila : participantes) {
            modelo.addRow(fila);
        }
    }

    private void agregarParticipante() {
        try {
            int eventoId = Integer.parseInt(txtEventoId.getText());
            int clienteId = Integer.parseInt(txtClienteId.getText());
            Date fecha = Date.valueOf(txtFecha.getText());
            String resultado = txtResultado.getText();

            if (dao.agregarParticipante(eventoId, clienteId, fecha, resultado)){
                JOptionPane.showMessageDialog(this, "Participante agregado.");
                limpiarCampos();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Error al agregar participante.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Datos inválidos.");
        }
    }

    private void actualizarParticipante() {
        try {
            int id = Integer.parseInt(txtId.getText());
            int eventoId = Integer.parseInt(txtEventoId.getText());
            int clienteId = Integer.parseInt(txtClienteId.getText());
            Date fecha = Date.valueOf(txtFecha.getText());
            String resultado = txtResultado.getText();

            if (dao.actualizarParticipante
        (id, eventoId, clienteId, fecha, resultado)) {
                JOptionPane.showMessageDialog(this, 
                        "Participante actualizado.");
                limpiarCampos();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Error al actualizar participante.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Datos inválidos.");
        }
    }

    private void eliminarParticipante() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            int id = (int) modelo.getValueAt(fila, 0);
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "¿Eliminar participante?", "Confirmar", 
                                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (dao.eliminarParticipante(id)) {
                    JOptionPane.showMessageDialog(this, 
                            "Participante eliminado.");
                    limpiarCampos();
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, 
                            "Error al eliminar participante.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un participante.");
        }
    }

    private void cargarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            txtId.setText(modelo.getValueAt(fila, 0).toString());
            txtEventoId.setText(modelo.getValueAt(fila, 1).toString());
            txtClienteId.setText(modelo.getValueAt(fila, 2).toString());
            txtFecha.setText(modelo.getValueAt(fila, 3).toString());
            txtResultado.setText(modelo.getValueAt(fila, 4).toString());
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtEventoId.setText("");
        txtClienteId.setText("");
        txtFecha.setText("");
        txtResultado.setText("");
    }
}
