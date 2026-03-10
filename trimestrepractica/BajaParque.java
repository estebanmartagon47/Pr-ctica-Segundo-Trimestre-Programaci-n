package trimestrepractica;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Ventana para dar de baja un Parque
 */
public class BajaParque extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<String> comboParques;
    private JButton btnBorrar;

    public BajaParque() {
        setTitle("Baja Parque");
        setSize(350, 200);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel lblParque = new JLabel("Seleccione Parque:");
        lblParque.setBounds(20, 20, 120, 25);
        add(lblParque);

        comboParques = new JComboBox<>();
        comboParques.setBounds(150, 20, 150, 25);
        add(comboParques);

        btnBorrar = new JButton("Borrar");
        btnBorrar.setBounds(120, 70, 100, 30);
        add(btnBorrar);

        cargarParques();

        btnBorrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                borrarParque();
            }
        });
    }

    private void cargarParques() {
        try (Connection con = ConexionBD.getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT idParque, nombreParque FROM Parques");
            while (rs.next()) {
                comboParques.addItem(rs.getInt("idParque") + " - " + rs.getString("nombreParque"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar parques: " + ex.getMessage());
        }
    }

    private void borrarParque() {
        if (comboParques.getSelectedItem() != null) {
            String seleccionado = comboParques.getSelectedItem().toString();
            int id = Integer.parseInt(seleccionado.split(" - ")[0]);
            int confirmar = JOptionPane.showConfirmDialog(this, "¿Está seguro de borrar el parque?");
            if (confirmar == JOptionPane.YES_OPTION) {
                try (Connection con = ConexionBD.getConnection()) {
                    String sql = "DELETE FROM Parques WHERE idParque=?";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setInt(1, id);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Parque borrado correctamente");
                    comboParques.removeAllItems();
                    cargarParques();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error al borrar: " + ex.getMessage());
                }
            }
        }
    }
}