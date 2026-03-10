package trimestrepractica;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Clase Login - Controla el acceso a la aplicación
 */
public class Login extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnEntrar;

    public Login() {

        setTitle("Login - Gestión Trimestre");
        setSize(350, 220);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana

        // Etiqueta Usuario
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setBounds(40, 30, 100, 25);
        add(lblUsuario);

        // Campo Usuario
        txtUsuario = new JTextField();
        txtUsuario.setBounds(140, 30, 150, 25);
        add(txtUsuario);

        // Etiqueta Password
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setBounds(40, 70, 100, 25);
        add(lblPassword);

        // Campo Password
        txtPassword = new JPasswordField();
        txtPassword.setBounds(140, 70, 150, 25);
        add(txtPassword);

        // Botón Entrar
        btnEntrar = new JButton("Entrar");
        btnEntrar.setBounds(110, 120, 120, 30);
        add(btnEntrar);

        // Acción del botón
        btnEntrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                validarLogin();
            }
        });
    }

    /**
     * Método que valida el usuario contra la base de datos
     */
    private void validarLogin() {

        String usuario = txtUsuario.getText();
        String password = new String(txtPassword.getPassword());

        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe rellenar todos los campos");
            return;
        }

        try (Connection con = ConexionBD.getConnection()) {

            String sql = "SELECT tipos FROM Usuarios WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String tipoUsuario = rs.getString("tipos");

                JOptionPane.showMessageDialog(this, "Bienvenido " + usuario);

                // Abrimos menú pasando el tipo de usuario
                MenuPrincipal menu = new MenuPrincipal(tipoUsuario);
                menu.setVisible(true);

                dispose(); // Cerramos login

            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error en la conexión: " + ex.getMessage());
        }
    }
}