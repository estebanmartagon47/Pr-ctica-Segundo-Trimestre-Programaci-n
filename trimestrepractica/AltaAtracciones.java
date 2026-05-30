package trimestrepractica;

import javax.swing.*;
import java.sql.*;

public class AltaAtracciones extends JFrame {

    private static final long serialVersionUID = 1L;

    private String tipoUsuario;

    private JTextField nombreAtraccion, precio;
    private JComboBox<String> cbParques;
    private JButton btnAlta;

    public AltaAtracciones(String tipoUsuario) {

        this.tipoUsuario = tipoUsuario;

        setTitle("Alta Atracciones");
        setSize(350, 250);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel lblNombre = new JLabel("Nombre Atracción:");
        lblNombre.setBounds(20, 20, 130, 25);
        add(lblNombre);

        nombreAtraccion = new JTextField();
        nombreAtraccion.setBounds(150, 20, 150, 25);
        add(nombreAtraccion);

        JLabel lblPrecio = new JLabel("Precio:");
        lblPrecio.setBounds(20, 60, 100, 25);
        add(lblPrecio);

        precio = new JTextField();
        precio.setBounds(150, 60, 100, 25);
        add(precio);

        JLabel lblParque = new JLabel("Parque:");
        lblParque.setBounds(20, 100, 100, 25);
        add(lblParque);

        cbParques = new JComboBox<>();
        cbParques.setBounds(150, 100, 150, 25);
        add(cbParques);

        btnAlta = new JButton("Agregar");
        btnAlta.setBounds(120, 150, 100, 30);
        add(btnAlta);

        btnAlta.addActionListener(e -> agregarAtraccion());

        cargarParques();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void cargarParques() {

        cbParques.removeAllItems();

        String sql = "SELECT idParque, nombreParque FROM Parques";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                cbParques.addItem(
                        rs.getInt("idParque") + " - " + rs.getString("nombreParque")
                );
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar parques: " + ex.getMessage());
        }
    }

    private void agregarAtraccion() {

        if (cbParques.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un parque");
            return;
        }

        try {

            String nombre = nombreAtraccion.getText();
            double precioAtr = Double.parseDouble(precio.getText());

            int idParque = Integer.parseInt(
                    cbParques.getSelectedItem().toString().split(" - ")[0]
            );

            Date fechaCreacion = new Date(System.currentTimeMillis());

            String sql = "INSERT INTO Atracciones (nombreAtraccion, precio, idParque, fechaCreacion) VALUES (?, ?, ?, ?)";

            try (Connection con = ConexionBD.getConnection();
                 PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setString(1, nombre);
                pst.setDouble(2, precioAtr);
                pst.setInt(3, idParque);
                pst.setDate(4, fechaCreacion);

                pst.executeUpdate();

                // 🔥 LOG MEJORADO
                Log.escribir(tipoUsuario,
                        "Alta atracción: " + nombre + " | Parque ID: " + idParque);

                JOptionPane.showMessageDialog(this,
                        "Atracción agregada correctamente");

                // opcional: limpiar campos
                nombreAtraccion.setText("");
                precio.setText("");

            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Precio inválido");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al agregar atracción: " + ex.getMessage());
        }
    }
}