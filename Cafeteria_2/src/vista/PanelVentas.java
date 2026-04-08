package vista;

import dao.ClienteDAO;
import dao.DetalleVentaDAO;
import dao.ProductoDAO;
import dao.VentaDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import modelo.Venta;
import modelo.DetalleVenta;

public class PanelVentas extends JPanel {

    private JTextField txtClienteId, txtEmpleadoId, txtCantidad, txtPrecioUnitario;
    private JTable tablaDetalle;
    private DefaultTableModel modeloDetalle;
    private List<Object[]> detallesVenta = new ArrayList<>();
    private VentaDAO ventaDAO = new VentaDAO();
    private DetalleVentaDAO detalleVentaDAO = new DetalleVentaDAO();
    private JComboBox<String> comboProducto;
    private JComboBox<String> comboTipoPago;
    private JLabel lblSubtotal;
    private List<Integer> listaIdsProducto = new ArrayList<>();
    private String rolUsuario;

    public PanelVentas(String rolUsuario) {
        this.rolUsuario = rolUsuario;
        inicializarComponentes();
        aplicarRestriccionesPorRol();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        JPanel panelEntrada = new JPanel(new GridLayout(5, 4, 5, 5));
        panelEntrada.setBorder(BorderFactory.createTitledBorder("Datos de la Venta"));

        txtClienteId = new JTextField();
        txtEmpleadoId = new JTextField();
        txtCantidad = new JTextField();
        txtPrecioUnitario = new JTextField();
        comboProducto = new JComboBox<>();
        comboTipoPago = new JComboBox<>(new String[]{"Efectivo", "Tarjeta", "Transferencia", "Otro"});

        cargarProductosEnCombo();

        panelEntrada.add(new JLabel("ID Cliente:"));
        panelEntrada.add(txtClienteId);
        panelEntrada.add(new JLabel("ID Empleado:"));
        panelEntrada.add(txtEmpleadoId);
        panelEntrada.add(new JLabel("Producto:"));
        panelEntrada.add(comboProducto);
        panelEntrada.add(new JLabel("Cantidad:"));
        panelEntrada.add(txtCantidad);
        panelEntrada.add(new JLabel("Precio Unitario:"));
        panelEntrada.add(txtPrecioUnitario);
        panelEntrada.add(new JLabel("Tipo de Pago:"));
        panelEntrada.add(comboTipoPago);

        JButton btnAgregarDetalle = new JButton("Agregar Producto");
        panelEntrada.add(btnAgregarDetalle);

        lblSubtotal = new JLabel("Subtotal: $0.00");
        panelEntrada.add(lblSubtotal);

        add(panelEntrada, BorderLayout.NORTH);

        modeloDetalle = new DefaultTableModel(new String[]{"Producto", "Cantidad", "Precio Unitario"}, 0);
        tablaDetalle = new JTable(modeloDetalle);
        add(new JScrollPane(tablaDetalle), BorderLayout.CENTER);

        JButton btnRegistrarVenta = new JButton("Registrar Venta");
        add(btnRegistrarVenta, BorderLayout.SOUTH);

        btnAgregarDetalle.addActionListener(e -> agregarDetalle());
        btnRegistrarVenta.addActionListener(e -> registrarVenta());
    }

    private void aplicarRestriccionesPorRol() {
        boolean esGerente = rolUsuario.equalsIgnoreCase("gerente");
        txtPrecioUnitario.setEditable(esGerente);
    }

    private void cargarProductosEnCombo() {
        ProductoDAO productoDAO = new ProductoDAO();
        comboProducto.removeAllItems();
        listaIdsProducto.clear();

        productoDAO.obtenerTodosLosProductos().forEach(producto -> {
            comboProducto.addItem(producto.getNombre());
            listaIdsProducto.add(producto.getId());
        });

        comboProducto.addActionListener(e -> {
            String nombreSeleccionado = (String) comboProducto.getSelectedItem();
            if (nombreSeleccionado != null) {
                double precio = productoDAO.obtenerPrecioPorNombre(nombreSeleccionado);
                txtPrecioUnitario.setText(String.valueOf(precio));
            }
        });
    }

    private void agregarDetalle() {
        try {
            String nombreProducto = (String) comboProducto.getSelectedItem();
            ProductoDAO productoDAO = new ProductoDAO();
            int productoId = productoDAO.obtenerIdPorNombre(nombreProducto);
            int cantidad = Integer.parseInt(txtCantidad.getText());
            double precio = Double.parseDouble(txtPrecioUnitario.getText());

            Object[] fila = new Object[]{nombreProducto, cantidad, precio};
            detallesVenta.add(fila);
            modeloDetalle.addRow(fila);

            actualizarSubtotal();

            txtCantidad.setText("");
            txtPrecioUnitario.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Verifica que la cantidad y el precio sean números válidos.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar producto: " + ex.getMessage());
        }
    }

    private void actualizarSubtotal() {
        double total = 0;
        for (Object[] fila : detallesVenta) {
            int cantidad = ((Number) fila[1]).intValue();
            double precio = ((Number) fila[2]).doubleValue();
            total += cantidad * precio;
        }
        lblSubtotal.setText(String.format("Subtotal: $%.2f", total));
    }

    private void registrarVenta() {
        try {
            int clienteId = Integer.parseInt(txtClienteId.getText());
            int empleadoId = Integer.parseInt(txtEmpleadoId.getText());
            String tipoPago = (String) comboTipoPago.getSelectedItem();

            double total = 0;
            List<DetalleVenta> detalles = new ArrayList<>();
            ProductoDAO productoDAO = new ProductoDAO();

            for (Object[] fila : detallesVenta) {
                String nombreProducto = (String) fila[0];
                int productoId = productoDAO.obtenerIdPorNombre(nombreProducto);
                int cantidad = ((Number) fila[1]).intValue();
                double precio = ((Number) fila[2]).doubleValue();

                total += cantidad * precio;
                detalles.add(new DetalleVenta(productoId, cantidad, BigDecimal.valueOf(precio)));
            }

            Venta venta = new Venta(clienteId, empleadoId, BigDecimal.valueOf(total), tipoPago);
            boolean exito = ventaDAO.registrarVenta(venta, detalles);

            if (exito) {
                JOptionPane.showMessageDialog(this, "Venta registrada correctamente.");
                generarTicket(venta.getId(), clienteId, empleadoId, detallesVenta, total, tipoPago);
                limpiarTodo();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar la venta. Verifica stock o datos.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cliente y Empleado deben ser números.");
        }
    }

    private void limpiarTodo() {
        txtClienteId.setText("");
        txtEmpleadoId.setText("");
        txtCantidad.setText("");
        txtPrecioUnitario.setText("");
        modeloDetalle.setRowCount(0);
        detallesVenta.clear();
        lblSubtotal.setText("Subtotal: $0.00");
    }

    private void generarTicket(int ventaId, int clienteId, int empleadoId, List<Object[]> detalles, double total, String tipoPago) {
        try {
            StringBuilder ticket = new StringBuilder();
            ClienteDAO clienteDAO = new ClienteDAO();
            ProductoDAO productoDAO = new ProductoDAO();

            String nombreCliente = clienteDAO.obtenerNombreClientePorId(clienteId);
            int puntosGenerados = (int) total / 10;
            clienteDAO.agregarPuntosACliente(clienteId, puntosGenerados);
            int puntosTotales = clienteDAO.obtenerPuntosCliente(clienteId);

            ticket.append("========== CAFETERÍA - TICKET DE VENTA ==========\n");
            ticket.append("RFC: JIG250509C-13\n");
            ticket.append("ID Venta: ").append(ventaId).append("\n");
            ticket.append("Cliente: ").append(nombreCliente).append(" (ID: ").append(clienteId).append(")\n");
            ticket.append("ID Empleado: ").append(empleadoId).append("\n");
            ticket.append("Tipo de Pago: ").append(tipoPago).append("\n");
            ticket.append("Fecha: ").append(new java.util.Date()).append("\n\n");

            ticket.append(String.format("%-20s %-10s %-10s %-10s\n", "Producto", "Cantidad", "P.Unitario", "Subtotal"));
            ticket.append("--------------------------------------------------\n");

            for (Object[] fila : detalles) {
                String nombreProducto = (String) fila[0];
                int cantidad = ((Number) fila[1]).intValue();
                double precio = ((Number) fila[2]).doubleValue();
                double subtotal = cantidad * precio;

                ticket.append(String.format("%-20s %-10d %-10.2f %-10.2f\n", nombreProducto, cantidad, precio, subtotal));
            }

            ticket.append("--------------------------------------------------\n");
            ticket.append(String.format("TOTAL: $%.2f\n", total));
            ticket.append("Puntos generados: ").append(puntosGenerados).append("\n");
            ticket.append("Puntos acumulados: ").append(puntosTotales).append("\n");
            ticket.append("==================================================\n");
            ticket.append("");

            String nombreArchivo = "ticket_venta_" + ventaId + ".txt";
            java.nio.file.Files.write(java.nio.file.Paths.get(nombreArchivo), ticket.toString().getBytes());

            JTextArea textArea = new JTextArea(ticket.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));

            JOptionPane.showMessageDialog(this, scrollPane, "Ticket de Venta", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al generar el ticket: " + ex.getMessage());
        }
    }

    public static JPanel crearPanel(String rolUsuario) {
        return new PanelVentas(rolUsuario);
    }
    
}
