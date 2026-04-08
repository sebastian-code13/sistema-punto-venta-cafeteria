package vista;

import javax.swing.*;
import java.awt.*;

public class Vista extends JFrame {
    private JPanel panelMenu, panelContenido;
    private JButton btnClientes, btnProductos, btnVentas, btnInventario, 
            btnEventos, btnTickets;

    public Vista() {
        setTitle("Sistema de Cafetería Pokémon");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        panelMenu = new JPanel();
        panelMenu.setLayout(new GridLayout(6, 1));
        panelMenu.setPreferredSize(new Dimension(200, 0));

        btnClientes = new JButton("Clientes");
        
        btnProductos.addActionListener(e -> {
        panelContenido.removeAll();
        panelContenido.add(new PanelProductos(), BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
        });
        
        btnVentas = new JButton("Ventas");
        btnInventario = new JButton("Inventario");
        btnEventos = new JButton("Eventos");
        btnTickets = new JButton("Tickets");

        panelMenu.add(btnClientes);
        panelMenu.add(btnProductos);
        panelMenu.add(btnVentas);
        panelMenu.add(btnInventario);
        panelMenu.add(btnEventos);
        panelMenu.add(btnTickets);

        panelContenido = new JPanel();
        panelContenido.setLayout(new BorderLayout());

        add(panelMenu, BorderLayout.WEST);
        add(panelContenido, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        new Vista();
    }
}