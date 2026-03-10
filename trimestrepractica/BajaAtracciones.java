package trimestrepractica;

import javax.swing.*;
import java.sql.*;

/**
 * Clase para eliminar una atracción de la base de datos.
 */
public class BajaAtracciones extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<String> cbAtracciones; // Combo con todas las atracciones
    private JButton btnBaja;

    public BajaAtracciones() {
        setTitle("Baja Atracciones");
        setSize(400, 150);
        setLayout(null);

        JLabel lblAtraccion = new JLabel("Seleccione Atracción:");
        lblAtraccion.setBounds(20, 20, 140, 25);
        add(lblAtraccion);

        cbAtracciones = new JComboBox<>();
        cbAtracciones.setBounds(160, 20, 200, 25);
        add(cbAtracciones);

        btnBaja = new JButton("Eliminar");
        btnBaja.setBounds(140, 60, 100, 30);
        add(btnBaja);

        // Evento para eliminar la atracción seleccionada
        btnBaja.addActionListener(e -> eliminarAtraccion());

        // Cargar todas las atracciones al iniciar
        cargarAtracciones();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Carga las atracciones existentes en la base de datos en el combo
     */
    private void cargarAtracciones() {
        String sql = "SELECT idAtraccion, nombreAtraccion FROM Atracciones";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                cbAtracciones.addItem(rs.getInt("idAtraccion") + " - " + rs.getString("nombreAtraccion"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar atracciones: " + ex.getMessage());
        }
    }

    /**
     * Elimina la atracción seleccionada del combo de la base de datos
     */
    private void eliminarAtraccion() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que desea eliminar esta atracción?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int idAtraccion = Integer.parseInt(cbAtracciones.getSelectedItem().toString().split(" - ")[0]);
            String sql = "DELETE FROM Atracciones WHERE idAtraccion=?";
            try (Connection con = ConexionBD.getConnection();
                 PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setInt(1, idAtraccion);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Atracción eliminada correctamente");

                // Recargar combo después de eliminar
                cbAtracciones.removeAllItems();
                cargarAtracciones();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar atracción: " + ex.getMessage());
            }
        }
    }
}