package trimestrepractica;

import javax.swing.*;
import java.sql.*;

/**
 * Clase para eliminar una atracción de la base de datos.
 */
public class BajaAtracciones extends JFrame {

    private static final long serialVersionUID = 1L;

    private JComboBox<String> cbAtracciones;
    private JButton btnBaja;
    private String tipoUsuario;

    public BajaAtracciones(String tipoUsuario) {

        this.tipoUsuario = tipoUsuario;

        setTitle("Baja Atracciones");
        setSize(400, 150);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel lblAtraccion = new JLabel("Seleccione Atracción:");
        lblAtraccion.setBounds(20, 20, 150, 25);
        add(lblAtraccion);

        cbAtracciones = new JComboBox<>();
        cbAtracciones.setBounds(160, 20, 200, 25);
        add(cbAtracciones);

        btnBaja = new JButton("Eliminar");
        btnBaja.setBounds(140, 60, 100, 30);
        add(btnBaja);

        btnBaja.addActionListener(e -> eliminarAtraccion());

        cargarAtracciones();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void cargarAtracciones() {

        cbAtracciones.removeAllItems(); // 🔥 evita duplicados

        String sql = "SELECT idAtraccion, nombreAtraccion FROM Atracciones";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                cbAtracciones.addItem(
                        rs.getInt("idAtraccion") + " - " + rs.getString("nombreAtraccion")
                );
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar atracciones: " + ex.getMessage());
        }
    }

    private void eliminarAtraccion() {

        if (cbAtracciones.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                    "No hay atracciones para eliminar");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que desea eliminar esta atracción?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            int idAtraccion = Integer.parseInt(
                    cbAtracciones.getSelectedItem().toString().split(" - ")[0]
            );

            try (Connection con = ConexionBD.getConnection()) {

                String sql = "DELETE FROM Atracciones WHERE idAtraccion=?";
                PreparedStatement pst = con.prepareStatement(sql);

                pst.setInt(1, idAtraccion);
                pst.executeUpdate();

                // 🔥 LOG MEJORADO
                Log.escribir(tipoUsuario,
                        "Baja atracción ID: " + idAtraccion);

                JOptionPane.showMessageDialog(this,
                        "Atracción eliminada correctamente");

                cargarAtracciones();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar atracción: " + ex.getMessage());
            }
        }
    }
}