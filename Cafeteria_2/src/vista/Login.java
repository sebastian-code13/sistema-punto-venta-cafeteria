package vista;

import com.sun.jdi.connect.spi.Connection;
import conexion.Conexion;
import dao.EmpleadoDAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import modelo.Empleado;

public class Login extends JFrame {

    public Login() {
        setTitle("Inicio de Sesión");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblUsuario = new JLabel("Usuario:");
        JLabel lblClave = new JLabel("Contraseña:");
        JTextField txtUsuario = new JTextField();
        JPasswordField txtClave = new JPasswordField();
        JButton btnEntrar = new JButton("Entrar");

        lblUsuario.setBounds(50, 60, 100, 25);
        txtUsuario.setBounds(150, 60, 180, 25);
        lblClave.setBounds(50, 100, 100, 25);
        txtClave.setBounds(150, 100, 180, 25);
        btnEntrar.setBounds(150, 150, 100, 30);

        add(lblUsuario);
        add(txtUsuario);
        add(lblClave);
        add(txtClave);
        add(btnEntrar);

        btnEntrar.addActionListener(e -> {
    String usuario = txtUsuario.getText();
    String clave = new String(txtClave.getPassword());

    EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    Empleado empleado = empleadoDAO.obtenerEmpleadoPorCredenciales(usuario, clave);

    if (empleado != null) {
        dispose(); 
        new Cafeteria(usuario, empleado.getRol(), empleado.getId()).setVisible(true);
    } else {
        JOptionPane.showMessageDialog(this, 
                "Usuario o contraseña incorrectos.");
    }
});
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }


public boolean validarCredenciales(String usuario, String clave) {
    String sql = "SELECT * FROM empleados WHERE usuario = ? AND contraseña = ?";

    try (java.sql.Connection conn = Conexion.getConexion();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, usuario);
        stmt.setString(2, clave);

        ResultSet rs = stmt.executeQuery();
        return rs.next(); 

    } catch (SQLException e) {
        System.err.println("Error al validar credenciales: " + e.getMessage());
        return false;
    }
}
}