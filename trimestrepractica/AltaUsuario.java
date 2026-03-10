package trimestrepractica;

import javax.swing.*;
import java.sql.*;

public class AltaUsuario extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Campos de texto y contraseña
    private JTextField username;
    private JPasswordField password;

    // ComboBox para tipo de usuario (Administrador o Usuario básico)
    private JComboBox<String> cbTipo;

    // Botón de alta
    private JButton btnAlta;

    public AltaUsuario() {
        setTitle("Alta Usuarios");
        setSize(300, 200);
        setLayout(null);

        // Campo para el nombre de usuario
        JLabel lblUsername = new JLabel("Usuario:");
        lblUsername.setBounds(20, 20, 80, 25);
        add(lblUsername);
        username = new JTextField();
        username.setBounds(100, 20, 150, 25);
        add(username);

        // Campo para la contraseña (por defecto Studium2025#)
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setBounds(20, 60, 80, 25);
        add(lblPassword);
        password = new JPasswordField("Studium2025#");
        password.setBounds(100, 60, 150, 25);
        add(password);

        // Tipo de usuario
        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setBounds(20, 100, 80, 25);
        add(lblTipo);
        cbTipo = new JComboBox<>();
        cbTipo.setBounds(100, 100, 150, 25);
        cbTipo.addItem("Administrador");
        cbTipo.addItem("Usuario");
        add(cbTipo);

        // Botón para ejecutar alta
        btnAlta = new JButton("Agregar");
        btnAlta.setBounds(100, 140, 100, 30);
        add(btnAlta);
        btnAlta.addActionListener(e -> agregarUsuario());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Método que inserta un usuario en la base de datos
    private void agregarUsuario() {
        String user = username.getText();
        String pass = new String(password.getPassword());
        String tipo = cbTipo.getSelectedItem().toString();

        String sql = "INSERT INTO Usuarios (username, password, tipos) VALUES (?, ?, ?)";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, user);
            pst.setString(2, pass);
            pst.setString(3, tipo);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Usuario agregado correctamente");
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar usuario: " + ex.getMessage());
        }
    }
}