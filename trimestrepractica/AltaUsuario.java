package trimestrepractica;

import javax.swing.*;
import java.sql.*;

public class AltaUsuario extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField username;
    private JPasswordField password;
    private JComboBox<String> cbTipo;
    private JButton btnAlta;

    private String tipoUsuario;

    public AltaUsuario(String tipoUsuario) {

        this.tipoUsuario = tipoUsuario;

        setTitle("Alta Usuarios");
        setSize(300, 220);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel lblUsername = new JLabel("Usuario:");
        lblUsername.setBounds(20, 20, 80, 25);
        add(lblUsername);

        username = new JTextField();
        username.setBounds(100, 20, 150, 25);
        add(username);

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setBounds(20, 60, 90, 25);
        add(lblPassword);

        password = new JPasswordField();
        password.setBounds(100, 60, 150, 25);
        add(password);

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setBounds(20, 100, 80, 25);
        add(lblTipo);

        cbTipo = new JComboBox<>();
        cbTipo.setBounds(100, 100, 150, 25);
        cbTipo.addItem("Administrador");
        cbTipo.addItem("Usuario");
        add(cbTipo);

        btnAlta = new JButton("Agregar");
        btnAlta.setBounds(100, 140, 100, 30);
        add(btnAlta);

        btnAlta.addActionListener(e -> agregarUsuario());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void agregarUsuario() {

        String user = username.getText();
        String pass = new String(password.getPassword());
        String tipo = cbTipo.getSelectedItem().toString();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Rellena todos los campos");
            return;
        }

        String sql = "INSERT INTO Usuarios (username, password, tipos) VALUES (?, ?, ?)";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, user);
            pst.setString(2, pass);
            pst.setString(3, tipo);

            pst.executeUpdate();

            // 🔥 LOG MEJORADO
            Log.escribir(tipoUsuario,
                    "Alta usuario: " + user + " | Tipo: " + tipo);

            JOptionPane.showMessageDialog(this,
                    "Usuario agregado correctamente");

            username.setText("");
            password.setText("");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al agregar usuario: " + ex.getMessage());
        }
    }
}