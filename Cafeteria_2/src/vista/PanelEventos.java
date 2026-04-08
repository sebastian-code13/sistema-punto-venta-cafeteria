package vista;

import dao.EventoDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelEventos extends JPanel {
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtTitulo, txtDescripcion, txtFecha_inicio, 
            txtTipo, txtPremio;
    private EventoDAO eventoDAO = new EventoDAO();

    public PanelEventos() {
        setLayout(new BorderLayout());

        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
                "Registro de Eventos/Dinámicas"));

        txtTitulo = new JTextField();
        txtDescripcion = new JTextField();
        txtFecha_inicio = new JTextField();
        txtTipo = new JTextField();
        txtPremio = new JTextField();

        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtTitulo);
        panelFormulario.add(new JLabel("Descripción:"));
        panelFormulario.add(txtDescripcion);
        panelFormulario.add(new JLabel("Fecha (AAAA-MM-DD):"));
        panelFormulario.add(txtFecha_inicio);
        panelFormulario.add(new JLabel("Tipo:"));
        panelFormulario.add(txtTipo);
        panelFormulario.add(new JLabel("Premio:"));
        panelFormulario.add(txtPremio);

        JButton btnAgregar = new JButton("Agregar");
        panelFormulario.add(btnAgregar);

        add(panelFormulario, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new String[]{
            "ID", "Nombre", "Descripción", "Fecha", "Tipo", "Premio"}, 0);
        tabla = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        add(panelBotones, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> agregarEvento());
        btnModificar.addActionListener(e -> modificarEvento());
        btnEliminar.addActionListener(e -> eliminarEvento());

        cargarEventos();
    }

private void agregarEvento() {
    try {
        String fechaTexto = txtFecha_inicio.getText();
        
        // Convierte el texto a java.sql.Date
        java.util.Date utilDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(fechaTexto);
        java.sql.Date fechaSQL = new java.sql.Date(utilDate.getTime());

        eventoDAO.agregarEvento(
            txtTitulo.getText(),
            txtDescripcion.getText(),
            fechaSQL,  // <-- pasamos la fecha ya convertida
            txtTipo.getText(),
            txtPremio.getText()
        );

        limpiarCampos();
        cargarEventos();
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Usa YYYY-MM-DD");
        ex.printStackTrace();
    }
}


    private void modificarEvento() {
    int fila = tabla.getSelectedRow();
    if (fila != -1) {
        try {
            int id = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
            String fechaTexto = txtFecha_inicio.getText();

            java.util.Date utilDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(fechaTexto);
            java.sql.Date fechaSQL = new java.sql.Date(utilDate.getTime());

            eventoDAO.actualizarEvento(id,
                txtTitulo.getText(),
                txtDescripcion.getText(),
                fechaSQL,
                txtTipo.getText(),
                txtPremio.getText()
            );
            limpiarCampos();
            cargarEventos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Usa YYYY-MM-DD");
            ex.printStackTrace();
        }
    }
}

    private void eliminarEvento() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            int id = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
            eventoDAO.eliminarEvento(id);
            limpiarCampos();
            cargarEventos();
        }
    }

    private void cargarEventos() {
        modelo.setRowCount(0);
        List<String[]> lista = eventoDAO.obtenerEventos();
        for (String[] fila : lista) {
            modelo.addRow(fila);
        }
    }

    private void limpiarCampos() {
        txtTitulo.setText("");
        txtDescripcion.setText("");
        txtFecha_inicio.setText("");
        txtTipo.setText("");
        txtPremio.setText("");
    }

    public static JPanel crearPanel() {
        return new PanelEventos();
    }
}
