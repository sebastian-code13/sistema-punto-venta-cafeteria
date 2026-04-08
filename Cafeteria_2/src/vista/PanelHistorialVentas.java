package vista;

import dao.VentaDAO;
import dao.DetalleVentaDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Timestamp;
import java.util.List;

public class PanelHistorialVentas extends JPanel {

    private JTable tablaVentas;
    private DefaultTableModel modeloVentas;
    private JTable tablaDetalles;
    private DefaultTableModel modeloDetalles;
    private JTextField txtFiltroCliente, txtFiltroEmpleado;

    private VentaDAO ventaDAO = new VentaDAO();
    private DetalleVentaDAO detalleDAO = new DetalleVentaDAO();

    public PanelHistorialVentas() {
        setLayout(new BorderLayout());

        // Panel de filtros
        JPanel panelFiltros = new JPanel(new GridLayout(1, 4, 5, 5));
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros"));

        txtFiltroCliente = new JTextField();
        txtFiltroEmpleado = new JTextField();
        JButton btnBuscar = new JButton("Buscar");

        panelFiltros.add(new JLabel("ID Cliente:"));
        panelFiltros.add(txtFiltroCliente);
        panelFiltros.add(new JLabel("ID Empleado:"));
        panelFiltros.add(txtFiltroEmpleado);
        panelFiltros.add(btnBuscar);

        add(panelFiltros, BorderLayout.NORTH);

        // Tabla de ventas
        modeloVentas = new DefaultTableModel(new String[]{
            "ID", "Cliente ID", "Empleado ID", "Fecha", "Total"}, 0);
        tablaVentas = new JTable(modeloVentas);
        add(new JScrollPane(tablaVentas), BorderLayout.CENTER);

        // Tabla de detalles
        modeloDetalles = new DefaultTableModel(new String[]{
            "Producto ID", "Cantidad", "Precio Unitario"}, 0);
        tablaDetalles = new JTable(modeloDetalles);
        add(new JScrollPane(tablaDetalles), BorderLayout.SOUTH);

        // Acciones
        btnBuscar.addActionListener(e -> cargarVentas());
        tablaVentas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetallesVentaSeleccionada();
            }
        });

        cargarVentas(); // Carga inicial
    }

    private void cargarVentas() {
        modeloVentas.setRowCount(0);

        List<Object[]> ventas = ventaDAO.obtenerVentas();

        for (Object[] venta : ventas) {
            int clienteIdFiltro = txtFiltroCliente.getText().isEmpty() 
                    ? -1 : Integer.parseInt(txtFiltroCliente.getText());
            int empleadoIdFiltro = txtFiltroEmpleado.getText().isEmpty() 
                    ? -1 : Integer.parseInt(txtFiltroEmpleado.getText());

            int clienteId = (int) venta[1];
            int empleadoId = (int) venta[2];

            boolean coincide = (clienteIdFiltro == -1 ||
                    clienteId == clienteIdFiltro) &&
                               (empleadoIdFiltro == -1 ||
                    empleadoId == empleadoIdFiltro);

            if (coincide) {
                modeloVentas.addRow(venta);
            }
        }
    }

    private void mostrarDetallesVentaSeleccionada() {
        int fila = tablaVentas.getSelectedRow();
        if (fila >= 0) {
            int ventaId = (int) modeloVentas.getValueAt(fila, 0);
            List<Object[]> detalles = detalleDAO.obtenerDetallesPorVenta
        (ventaId);

            modeloDetalles.setRowCount(0);
            for (Object[] detalle : detalles) {
                modeloDetalles.addRow(
                        new Object[]{detalle[2], detalle[3], detalle[4]});
            }
        }
    }

    public static JPanel crearPanel() {
        return new PanelHistorialVentas();
    }
}
