package vista;

import dao.VentaDAO;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cafeteria extends JFrame {
    
    private int empleadoId;
    private String rol;
    private int idEmpleado;



    private JPanel panelContenido;

    public Cafeteria(String usuario, String rol, int idEmpleado){
        this.empleadoId = empleadoId    ;
        this.rol = rol;
        this.empleadoId = idEmpleado;

            
        setTitle("Sistema de Punto de Venta - Cafetería Pokémon");
        setSize(1200, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelLateral = new JPanel();
        panelLateral.setLayout(new GridLayout(11, 1, 5, 5));
        panelLateral.setBackground(new Color(230, 240, 255));
        panelLateral.setPreferredSize(new Dimension(200, 0));

        // Crear botones
        JButton btnProductos = new JButton("Productos");
        JButton btnClientes = new JButton("Clientes");
        JButton btnVentas = new JButton("Ventas");
        JButton btnInventario = new JButton("Inventario");
        JButton btnEmpleados = new JButton("Empleados");
        JButton btnEventos = new JButton("Eventos");
        JButton btnParticipantes = new JButton("Participantes");
        JButton btnRecompensas = new JButton("Recompensa");
        JButton btnProveedores = new JButton("Proveedores");
        JButton btnHistorial = new JButton("Historial de Ventas");
        JButton btnSalir = new JButton("Salir");

        if (rol.equalsIgnoreCase("gerente")) {
            panelLateral.add(btnProductos);
            panelLateral.add(btnClientes);
            panelLateral.add(btnVentas);
            panelLateral.add(btnInventario);
            panelLateral.add(btnEmpleados);
            panelLateral.add(btnEventos);
            panelLateral.add(btnParticipantes);
            panelLateral.add(btnRecompensas);
            panelLateral.add(btnProveedores);
            panelLateral.add(btnHistorial);
        } else if (rol.equalsIgnoreCase("cajero")) {
            panelLateral.add(btnProductos);
            panelLateral.add(btnClientes);
            panelLateral.add(btnVentas);
            panelLateral.add(btnEventos);
            panelLateral.add(btnParticipantes);
            panelLateral.add(btnRecompensas);
            panelLateral.add(btnHistorial);
        }

        panelLateral.add(btnSalir);
        add(panelLateral, BorderLayout.WEST);

        panelContenido = new JPanel(new BorderLayout());
        add(panelContenido, BorderLayout.CENTER);

        // Listeners comunes
        btnProductos.addActionListener(e -> mostrarPanel(new PanelProductos(rol)));
        btnClientes.addActionListener(e -> mostrarPanel(new PanelClientes()));
        btnVentas.addActionListener(e -> mostrarPanel(new PanelVentas(rol)));
        btnInventario.addActionListener(e -> mostrarPanel(new PanelInventario()));
        btnEmpleados.addActionListener(e -> mostrarPanel(new PanelEmpleados()));
        btnEventos.addActionListener(e -> mostrarPanel(new PanelEventos()));
        btnParticipantes.addActionListener(e -> {
            try {
                mostrarPanel(new PanelParticipantes());
            } catch (SQLException ex) {
                Logger.getLogger(Cafeteria.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        btnRecompensas.addActionListener(e -> mostrarPanel(new PanelRecompensas()));
        btnProveedores.addActionListener(e -> mostrarPanel(new PanelProveedores()));
        btnHistorial.addActionListener(e -> mostrarPanel(new PanelHistorialVentas()));

        btnSalir.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Deseas cerrar sesión?",
                    "Cerrar sesión", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new Login().setVisible(true);
            }
            if (rol.equalsIgnoreCase("cajero")) {
                VentaDAO ventaDAO = new VentaDAO();
                Map<String, Double> totales = ventaDAO.obtenerTotalesPorTipoPagoDelDia(idEmpleado);

StringBuilder mensaje = new StringBuilder("Resumen de ventas del día por tipo de pago:\n");
mensaje.append("Efectivo: $").append(String.format("%.2f", totales.getOrDefault("efectivo", 0.0))).append("\n");
mensaje.append("Tarjeta: $").append(String.format("%.2f", totales.getOrDefault("tarjeta", 0.0))).append("\n");
mensaje.append("Transferencia: $").append(String.format("%.2f", totales.getOrDefault("transferencia", 0.0))).append("\n");

JOptionPane.showMessageDialog(null, mensaje.toString(), "Cierre de Caja", JOptionPane.INFORMATION_MESSAGE);

// Luego cerrar la sesión:
dispose(); // o lo que uses para cerrar ventana actual
new Login().setVisible(true); // o volver al login
            }
        });
    }


    private void mostrarPanel(JPanel panel) {
        panelContenido.removeAll();
        panelContenido.add(panel, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }
}
