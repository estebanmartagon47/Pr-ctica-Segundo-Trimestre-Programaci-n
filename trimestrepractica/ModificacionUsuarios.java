package trimestrepractica;

import javax.swing.*;
import java.sql.*;

/**
 * Clase para modificar datos de usuarios existentes
 */
public class ModificacionUsuarios extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<String> cbUsuarios;
    private JTextField tfUsername, tfPassword, tfTipo;
    private JButton btnModificar;

    public ModificacionUsuarios() {
        setTitle("Modificar Usuarios");
        setSize(400, 250);
        setLayout(null);

        JLabel lblUsuario = new JLabel("Seleccione Usuario:");
        lblUsuario.setBounds(20, 20, 140, 25);
        add(lblUsuario);

        cbUsuarios = new JComboBox<>();
        cbUsuarios.setBounds(160, 20, 200, 25);
        add(cbUsuarios);
        cbUsuarios.addActionListener(e -> cargarDatos());

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(20, 60, 140, 25);
        add(lblUsername);

        tfUsername = new JTextField();
        tfUsername.setBounds(160, 60, 200, 25);
        add(tfUsername);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(20, 100, 140, 25);
        add(lblPassword);

        tfPassword = new JTextField();
        tfPassword.setBounds(160, 100, 200, 25);
        add(tfPassword);

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setBounds(20, 140, 140, 25);
        add(lblTipo);

        tfTipo = new JTextField();
        tfTipo.setBounds(160, 140, 200, 25);
        add(tfTipo);

        btnModificar = new JButton("Modificar");
        btnModificar.setBounds(140, 180, 100, 30);
        add(btnModificar);
        btnModificar.addActionListener(e -> modificarUsuario());

        cargarUsuarios();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void cargarUsuarios() {
        String sql = "SELECT idUsuario, username FROM Usuarios";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                cbUsuarios.addItem(rs.getInt("idUsuario") + " - " + rs.getString("username"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + ex.getMessage());
        }
    }

    private void cargarDatos() {
        int idUsuario = Integer.parseInt(cbUsuarios.getSelectedItem().toString().split(" - ")[0]);
        String sql = "SELECT username, password, tipos FROM Usuarios WHERE idUsuario=?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idUsuario);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                tfUsername.setText(rs.getString("username"));
                tfPassword.setText(rs.getString("password"));
                tfTipo.setText(rs.getString("tipos"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos del usuario: " + ex.getMessage());
        }
    }

    private void modificarUsuario() {
        int idUsuario = Integer.parseInt(cbUsuarios.getSelectedItem().toString().split(" - ")[0]);
        String sql = "UPDATE Usuarios SET username=?, password=?, tipos=? WHERE idUsuario=?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, tfUsername.getText());
            pst.setString(2, tfPassword.getText());
            pst.setString(3, tfTipo.getText());
            pst.setInt(4, idUsuario);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Usuario modificado correctamente");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar usuario: " + ex.getMessage());
        }
    }
}