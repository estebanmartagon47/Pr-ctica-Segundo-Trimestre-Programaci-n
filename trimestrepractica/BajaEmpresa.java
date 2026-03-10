package trimestrepractica;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Ventana para dar de baja una Empresa
 */
public class BajaEmpresa extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<String> comboEmpresas;
    private JButton btnBorrar;

    public BajaEmpresa() {
        setTitle("Baja Empresa");
        setSize(350, 200);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel lblEmpresa = new JLabel("Seleccione Empresa:");
        lblEmpresa.setBounds(20, 20, 120, 25);
        add(lblEmpresa);

        comboEmpresas = new JComboBox<>();
        comboEmpresas.setBounds(150, 20, 150, 25);
        add(comboEmpresas);

        btnBorrar = new JButton("Borrar");
        btnBorrar.setBounds(120, 70, 100, 30);
        add(btnBorrar);

        cargarEmpresas();

        btnBorrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                borrarEmpresa();
            }
        });
    }

    private void cargarEmpresas() {
        try (Connection con = ConexionBD.getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT idEmpresa, nombreEmpresa FROM Empresas");
            while (rs.next()) {
                comboEmpresas.addItem(rs.getInt("idEmpresa") + " - " + rs.getString("nombreEmpresa"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar empresas: " + ex.getMessage());
        }
    }

    private void borrarEmpresa() {
        if (comboEmpresas.getSelectedItem() != null) {
            String seleccionado = comboEmpresas.getSelectedItem().toString();
            int id = Integer.parseInt(seleccionado.split(" - ")[0]);
            int confirmar = JOptionPane.showConfirmDialog(this, "¿Está seguro de borrar la empresa?");
            if (confirmar == JOptionPane.YES_OPTION) {
                try (Connection con = ConexionBD.getConnection()) {
                    String sql = "DELETE FROM Empresas WHERE idEmpresa=?";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setInt(1, id);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Empresa borrada correctamente");
                    comboEmpresas.removeAllItems();
                    cargarEmpresas();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error al borrar: " + ex.getMessage());
                }
            }
        }
    }
}