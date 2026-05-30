package trimestrepractica;

import javax.swing.*;
import java.sql.*;

public class BajaParque extends JFrame {

    private static final long serialVersionUID = 1L;

    private JComboBox<String> comboParques;
    private JButton btnBorrar;

    private String tipoUsuario;

    public BajaParque(String tipoUsuario) {

        this.tipoUsuario = tipoUsuario;

        setTitle("Baja Parque");
        setSize(350, 200);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel lblParque = new JLabel("Seleccione Parque:");
        lblParque.setBounds(20, 20, 140, 25);
        add(lblParque);

        comboParques = new JComboBox<>();
        comboParques.setBounds(160, 20, 150, 25);
        add(comboParques);

        btnBorrar = new JButton("Borrar");
        btnBorrar.setBounds(120, 70, 100, 30);
        add(btnBorrar);

        cargarParques();

        btnBorrar.addActionListener(e -> borrarParque());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void cargarParques() {

        comboParques.removeAllItems(); // 🔥 evita duplicados

        try (Connection con = ConexionBD.getConnection()) {

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT idParque, nombreParque FROM Parques"
            );

            while (rs.next()) {
                comboParques.addItem(
                        rs.getInt("idParque") + " - " + rs.getString("nombreParque")
                );
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar parques: " + ex.getMessage());
        }
    }

    private void borrarParque() {

        if (comboParques.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                    "No hay parques para borrar");
            return;
        }

        int confirmar = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de borrar el parque?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmar == JOptionPane.YES_OPTION) {

            int id = Integer.parseInt(
                    comboParques.getSelectedItem().toString().split(" - ")[0]
            );

            try (Connection con = ConexionBD.getConnection()) {

                String sql = "DELETE FROM Parques WHERE idParque=?";
                PreparedStatement ps = con.prepareStatement(sql);

                ps.setInt(1, id);
                ps.executeUpdate();

                // 🔥 LOG MEJORADO
                Log.escribir(tipoUsuario,
                        "Baja parque ID: " + id);

                JOptionPane.showMessageDialog(this,
                        "Parque borrado correctamente");

                cargarParques();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al borrar: " + ex.getMessage());
            }
        }
    }
}