package trimestrepractica;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Ventana para dar de alta un Parque
 */
public class AltaParque extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtNombre, txtUbicacion, txtPrecio;
    private JButton btnGuardar;

    public AltaParque() {
        setTitle("Alta Parque");
        setSize(350, 250);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel lblNombre = new JLabel("Nombre Parque:");
        lblNombre.setBounds(20, 20, 100, 25);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(130, 20, 180, 25);
        add(txtNombre);

        JLabel lblUbicacion = new JLabel("Ubicación:");
        lblUbicacion.setBounds(20, 60, 100, 25);
        add(lblUbicacion);

        txtUbicacion = new JTextField();
        txtUbicacion.setBounds(130, 60, 180, 25);
        add(txtUbicacion);

        JLabel lblPrecio = new JLabel("Precio Entrada:");
        lblPrecio.setBounds(20, 100, 100, 25);
        add(lblPrecio);

        txtPrecio = new JTextField();
        txtPrecio.setBounds(130, 100, 180, 25);
        add(txtPrecio);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(120, 150, 100, 30);
        add(btnGuardar);

        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardarParque();
            }
        });
    }

    private void guardarParque() {
        try (Connection con = ConexionBD.getConnection()) {
            String sql = "INSERT INTO Parques(nombreParque, ubicacion, precioEntrada) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtUbicacion.getText());
            ps.setDouble(3, Double.parseDouble(txtPrecio.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Parque registrado correctamente");
            txtNombre.setText("");
            txtUbicacion.setText("");
            txtPrecio.setText("");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }
}