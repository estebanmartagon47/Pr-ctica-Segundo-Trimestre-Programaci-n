package trimestrepractica;

import javax.swing.*;
import java.sql.*;

public class AltaAtracciones extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Campos de texto para nombre y precio
    private JTextField nombreAtraccion, precio;

    // ComboBox para seleccionar el parque asociado
    private JComboBox<String> cbParques;

    // Botón para dar de alta
    private JButton btnAlta;

    public AltaAtracciones() {
        setTitle("Alta Atracciones");
        setSize(350, 200);
        setLayout(null);

        // Nombre de la atracción
        JLabel lblNombre = new JLabel("Nombre Atracción:");
        lblNombre.setBounds(20, 20, 120, 25);
        add(lblNombre);
        nombreAtraccion = new JTextField();
        nombreAtraccion.setBounds(150, 20, 150, 25);
        add(nombreAtraccion);

        // Precio de la atracción
        JLabel lblPrecio = new JLabel("Precio:");
        lblPrecio.setBounds(20, 60, 120, 25);
        add(lblPrecio);
        precio = new JTextField();
        precio.setBounds(150, 60, 100, 25);
        add(precio);

        // Parque al que pertenece
        JLabel lblParque = new JLabel("Parque:");
        lblParque.setBounds(20, 100, 120, 25);
        add(lblParque);
        cbParques = new JComboBox<>();
        cbParques.setBounds(150, 100, 150, 25);
        add(cbParques);

        // Botón de alta
        btnAlta = new JButton("Agregar");
        btnAlta.setBounds(120, 140, 100, 30);
        add(btnAlta);

        // Acción del botón
        btnAlta.addActionListener(e -> agregarAtraccion());

        // Cargar parques desde la base de datos
        cargarParques();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void cargarParques() {
        String sql = "SELECT idParque, nombreParque FROM Parques";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while(rs.next()) {
                cbParques.addItem(rs.getInt("idParque") + " - " + rs.getString("nombreParque"));
            }
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar parques: " + ex.getMessage());
        }
    }

    private void agregarAtraccion() {
        String nombre = nombreAtraccion.getText();
        double precioAtr = Double.parseDouble(precio.getText());
        int idParque = Integer.parseInt(cbParques.getSelectedItem().toString().split(" - ")[0]);

        String sql = "INSERT INTO Atracciones (nombreAtraccion, precio, idParque) VALUES (?, ?, ?)";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, nombre);
            pst.setDouble(2, precioAtr);
            pst.setInt(3, idParque);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Atracción agregada correctamente");
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar atracción: " + ex.getMessage());
        }
    }
}