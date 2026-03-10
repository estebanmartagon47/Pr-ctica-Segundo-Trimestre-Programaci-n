package trimestrepractica;

import javax.swing.*;
import java.sql.*;

/**
 * Clase para eliminar un usuario de la base de datos.
 */
public class BajaUsuario extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<String> cbUsuarios;
    private JButton btnBaja;

    public BajaUsuario() {
        setTitle("Baja Usuarios");
        setSize(400, 150);
        setLayout(null);

        JLabel lblUsuario = new JLabel("Seleccione Usuario:");
        lblUsuario.setBounds(20, 20, 140, 25);
        add(lblUsuario);

        cbUsuarios = new JComboBox<>();
        cbUsuarios.setBounds(160, 20, 200, 25);
        add(cbUsuarios);

        btnBaja = new JButton("Eliminar");
        btnBaja.setBounds(140, 60, 100, 30);
        add(btnBaja);

        btnBaja.addActionListener(e -> eliminarUsuario());

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

    private void eliminarUsuario() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que desea eliminar este usuario?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int idUsuario = Integer.parseInt(cbUsuarios.getSelectedItem().toString().split(" - ")[0]);
            String sql = "DELETE FROM Usuarios WHERE idUsuario=?";
            try (Connection con = ConexionBD.getConnection();
                 PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setInt(1, idUsuario);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente");

                cbUsuarios.removeAllItems();
                cargarUsuarios();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar usuario: " + ex.getMessage());
            }
        }
    }
}