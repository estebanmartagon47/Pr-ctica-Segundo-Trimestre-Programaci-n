package trimestrepractica;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Ventana para dar de alta una Empresa
 */
public class AltaEmpresa extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtNombre, txtFecha, txtCif;
    private JButton btnGuardar;

    public AltaEmpresa() {
        setTitle("Alta Empresa");
        setSize(350, 250);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 20, 100, 25);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(120, 20, 180, 25);
        add(txtNombre);

        JLabel lblFecha = new JLabel("Fecha Creación:");
        lblFecha.setBounds(20, 60, 100, 25);
        add(lblFecha);

        txtFecha = new JTextField();
        txtFecha.setBounds(120, 60, 180, 25);
        add(txtFecha);

        JLabel lblCif = new JLabel("CIF:");
        lblCif.setBounds(20, 100, 100, 25);
        add(lblCif);

        txtCif = new JTextField();
        txtCif.setBounds(120, 100, 180, 25);
        add(txtCif);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(120, 150, 100, 30);
        add(btnGuardar);

        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardarEmpresa();
            }
        });
    }

    private void guardarEmpresa() {
        try (Connection con = ConexionBD.getConnection()) {
            String sql = "INSERT INTO Empresas(nombreEmpresa, fechaCreacion, cif) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtFecha.getText());
            ps.setString(3, txtCif.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Empresa registrada correctamente");
            txtNombre.setText("");
            txtFecha.setText("");
            txtCif.setText("");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }
}