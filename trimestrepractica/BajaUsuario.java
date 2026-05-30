package trimestrepractica;

import javax.swing.*;
import java.sql.*;

public class BajaUsuario extends JFrame {

    private static final long serialVersionUID = 1L;

    private JComboBox<String> cbUsuarios;
    private JButton btnBaja;

    private String tipoUsuario;

    public BajaUsuario(String tipoUsuario) {

        this.tipoUsuario = tipoUsuario;

        setTitle("Baja Usuarios");
        setSize(400, 160);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

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

        cbUsuarios.removeAllItems();

        try (Connection con = ConexionBD.getConnection()) {

            String sql = "SELECT idUsuario, username FROM Usuarios";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                cbUsuarios.addItem(
                        rs.getInt("idUsuario") + " - " + rs.getString("username")
                );
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar usuarios: " + ex.getMessage());
        }
    }

    private void eliminarUsuario() {

        if (cbUsuarios.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                    "No hay usuarios para eliminar");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que desea eliminar este usuario?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            int idUsuario = Integer.parseInt(
                    cbUsuarios.getSelectedItem().toString().split(" - ")[0]
            );

            try (Connection con = ConexionBD.getConnection()) {

                String sql = "DELETE FROM Usuarios WHERE idUsuario=?";
                PreparedStatement pst = con.prepareStatement(sql);

                pst.setInt(1, idUsuario);
                pst.executeUpdate();

                // 🔥 LOG MEJORADO
                Log.escribir(tipoUsuario,
                        "Baja usuario ID: " + idUsuario);

                JOptionPane.showMessageDialog(this,
                        "Usuario eliminado correctamente");

                cargarUsuarios();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar usuario: " + ex.getMessage());
            }
        }
    }
}