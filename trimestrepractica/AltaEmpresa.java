package trimestrepractica;

import javax.swing.*;
import java.sql.*;

public class AltaEmpresa extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtNombre, txtCif;
    private JButton btnGuardar;

    public AltaEmpresa() {

        setTitle("Alta Empresa");
        setSize(350, 200);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 20, 100, 25);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(120, 20, 180, 25);
        add(txtNombre);

        JLabel lblCif = new JLabel("CIF:");
        lblCif.setBounds(20, 60, 100, 25);
        add(lblCif);

        txtCif = new JTextField();
        txtCif.setBounds(120, 60, 180, 25);
        add(txtCif);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(120, 110, 100, 30);
        add(btnGuardar);

        btnGuardar.addActionListener(e -> guardarEmpresa());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void guardarEmpresa() {

        String sql = "INSERT INTO Empresas(nombreEmpresa, cif) VALUES (?, ?)";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String nombre = txtNombre.getText();
            String cif = txtCif.getText();

            ps.setString(1, nombre);
            ps.setString(2, cif);

            ps.executeUpdate();

            // 🔥 LOG IMPORTANTE
            Log.escribir("usuario", "Alta empresa: " + nombre);

            JOptionPane.showMessageDialog(this, "Empresa registrada correctamente");

            txtNombre.setText("");
            txtCif.setText("");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}