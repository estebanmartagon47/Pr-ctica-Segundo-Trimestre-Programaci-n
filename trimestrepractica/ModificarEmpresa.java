package trimestrepractica;

import javax.swing.*;
import java.sql.*;

public class ModificarEmpresa extends JFrame {

    private static final long serialVersionUID = 1L;

    private JComboBox<String> comboEmpresas;
    private JTextField txtNombre, txtFecha, txtCif;
    private JButton btnModificar;

    private String tipoUsuario;

    public ModificarEmpresa(String tipoUsuario) {

        this.tipoUsuario = tipoUsuario;

        setTitle("Modificar Empresa");
        setSize(350, 300);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel lblEmpresa = new JLabel("Seleccione Empresa:");
        lblEmpresa.setBounds(20, 20, 150, 25);
        add(lblEmpresa);

        comboEmpresas = new JComboBox<>();
        comboEmpresas.setBounds(170, 20, 150, 25);
        add(comboEmpresas);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 60, 100, 25);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(120, 60, 180, 25);
        add(txtNombre);

        JLabel lblFecha = new JLabel("Fecha:");
        lblFecha.setBounds(20, 100, 100, 25);
        add(lblFecha);

        txtFecha = new JTextField();
        txtFecha.setBounds(120, 100, 180, 25);
        add(txtFecha);

        JLabel lblCif = new JLabel("CIF:");
        lblCif.setBounds(20, 140, 100, 25);
        add(lblCif);

        txtCif = new JTextField();
        txtCif.setBounds(120, 140, 180, 25);
        add(txtCif);

        btnModificar = new JButton("Modificar");
        btnModificar.setBounds(120, 200, 100, 30);
        add(btnModificar);

        cargarEmpresas();

        comboEmpresas.addActionListener(e -> cargarDatosEmpresa());

        btnModificar.addActionListener(e -> modificarEmpresa());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void cargarEmpresas() {

        comboEmpresas.removeAllItems();

        try (Connection con = ConexionBD.getConnection()) {

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT idEmpresa, nombreEmpresa FROM Empresas"
            );

            while (rs.next()) {
                comboEmpresas.addItem(
                        rs.getInt("idEmpresa") + " - " + rs.getString("nombreEmpresa")
                );
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar empresas: " + ex.getMessage());
        }
    }

    private void cargarDatosEmpresa() {

        if (comboEmpresas.getSelectedItem() == null) return;

        String seleccionado = comboEmpresas.getSelectedItem().toString();
        int id = Integer.parseInt(seleccionado.split(" - ")[0]);

        try (Connection con = ConexionBD.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM Empresas WHERE idEmpresa=?"
            );

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                txtNombre.setText(rs.getString("nombreEmpresa"));
                txtFecha.setText(rs.getString("fechaCreacion"));
                txtCif.setText(rs.getString("cif"));
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar datos: " + ex.getMessage());
        }
    }

    private void modificarEmpresa() {

        if (comboEmpresas.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una empresa");
            return;
        }

        String seleccionado = comboEmpresas.getSelectedItem().toString();
        int id = Integer.parseInt(seleccionado.split(" - ")[0]);

        try (Connection con = ConexionBD.getConnection()) {

            String sql = "UPDATE Empresas SET nombreEmpresa=?, fechaCreacion=?, cif=? WHERE idEmpresa=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtFecha.getText());
            ps.setString(3, txtCif.getText());
            ps.setInt(4, id);

            ps.executeUpdate();

            // 🔥 LOG MEJORADO
            Log.escribir(tipoUsuario, "Modificación empresa ID: " + id);

            JOptionPane.showMessageDialog(this,
                    "Empresa modificada correctamente");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al modificar: " + ex.getMessage());
        }
    }
}