package trimestrepractica;

import javax.swing.*;
import java.sql.*;

public class AltaParque extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField txtNombre, txtUbicacion, txtPrecio;
    private JButton btnGuardar;

    private String tipoUsuario;

    public AltaParque(String tipoUsuario) {

        this.tipoUsuario = tipoUsuario;

        setTitle("Alta Parque");
        setSize(350, 250);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel lblNombre = new JLabel("Nombre Parque:");
        lblNombre.setBounds(20, 20, 120, 25);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(150, 20, 150, 25);
        add(txtNombre);

        JLabel lblUbicacion = new JLabel("Ubicación:");
        lblUbicacion.setBounds(20, 60, 100, 25);
        add(lblUbicacion);

        txtUbicacion = new JTextField();
        txtUbicacion.setBounds(150, 60, 150, 25);
        add(txtUbicacion);

        JLabel lblPrecio = new JLabel("Precio Entrada:");
        lblPrecio.setBounds(20, 100, 120, 25);
        add(lblPrecio);

        txtPrecio = new JTextField();
        txtPrecio.setBounds(150, 100, 150, 25);
        add(txtPrecio);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(120, 150, 100, 30);
        add(btnGuardar);

        btnGuardar.addActionListener(e -> guardarParque());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void guardarParque() {

        try {

            String nombre = txtNombre.getText();
            String ubicacion = txtUbicacion.getText();
            double precio = Double.parseDouble(txtPrecio.getText());

            try (Connection con = ConexionBD.getConnection()) {

                String sql = "INSERT INTO Parques(nombreParque, ubicacion, precioEntrada) VALUES (?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(sql);

                ps.setString(1, nombre);
                ps.setString(2, ubicacion);
                ps.setDouble(3, precio);

                ps.executeUpdate();

                // 🔥 LOG MEJORADO
                Log.escribir(tipoUsuario,
                        "Alta parque: " + nombre + " | Precio: " + precio);

                JOptionPane.showMessageDialog(this,
                        "Parque registrado correctamente");

                // limpiar campos
                txtNombre.setText("");
                txtUbicacion.setText("");
                txtPrecio.setText("");

            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Precio inválido");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar: " + ex.getMessage());
        }
    }
}