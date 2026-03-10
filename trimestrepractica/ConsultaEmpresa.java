package trimestrepractica;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

/**
 * Consulta y gestión de Empresas
 */
public class ConsultaEmpresa extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTable tabla;
    private DefaultTableModel modelo;
    private JButton btnAlta, btnBaja, btnModificar;
    private String tipoUsuario;

    public ConsultaEmpresa(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
        setTitle("Gestión de Empresas");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new String[]{"ID", "Nombre", "Fecha", "CIF"});
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(20, 20, 540, 200);
        add(scroll);

        // Botones
        btnAlta = new JButton("Alta");
        btnAlta.setBounds(50, 250, 100, 30);
        add(btnAlta);
        btnBaja = new JButton("Baja");
        btnBaja.setBounds(200, 250, 100, 30);
        add(btnBaja);
        btnModificar = new JButton("Modificar");
        btnModificar.setBounds(350, 250, 120, 30);
        add(btnModificar);

        controlarPermisos();
        cargarEmpresas();
        agregarListeners();
    }

    private void controlarPermisos() {
        if (!tipoUsuario.equalsIgnoreCase("Administrador")) {
            btnAlta.setEnabled(false);
            btnBaja.setEnabled(false);
            btnModificar.setEnabled(false);
        }
    }

    private void cargarEmpresas() {
        try (Connection con = ConexionBD.getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Empresas");
            modelo.setRowCount(0);
            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("idEmpresa"),
                        rs.getString("nombreEmpresa"),
                        rs.getString("fechaCreacion"),
                        rs.getString("cif")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar empresas: " + ex.getMessage());
        }
    }

    private void agregarListeners() {
        btnAlta.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog(this, "Nombre empresa:");
            String cif = JOptionPane.showInputDialog(this, "CIF:");
            if (nombre != null && cif != null) {
                try (Connection con = ConexionBD.getConnection()) {
                    PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO Empresas(nombreEmpresa, cif) VALUES(?, ?)");
                    ps.setString(1, nombre);
                    ps.setString(2, cif);
                    ps.executeUpdate();
                    cargarEmpresas();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error al dar de alta: " + ex.getMessage());
                }
            }
        });

        btnBaja.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                int id = (int) modelo.getValueAt(fila, 0);
                int resp = JOptionPane.showConfirmDialog(this,
                        "¿Seguro que quieres borrar la empresa?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (resp == JOptionPane.YES_OPTION) {
                    try (Connection con = ConexionBD.getConnection()) {
                        PreparedStatement ps = con.prepareStatement(
                                "DELETE FROM Empresas WHERE idEmpresa = ?");
                        ps.setInt(1, id);
                        ps.executeUpdate();
                        cargarEmpresas();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Error al borrar: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona primero una empresa.");
            }
        });

        btnModificar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                int id = (int) modelo.getValueAt(fila, 0);
                String nombre = JOptionPane.showInputDialog(this, "Nuevo nombre:",
                        modelo.getValueAt(fila, 1));
                String cif = JOptionPane.showInputDialog(this, "Nuevo CIF:",
                        modelo.getValueAt(fila, 3));
                if (nombre != null && cif != null) {
                    try (Connection con = ConexionBD.getConnection()) {
                        PreparedStatement ps = con.prepareStatement(
                                "UPDATE Empresas SET nombreEmpresa=?, cif=? WHERE idEmpresa=?");
                        ps.setString(1, nombre);
                        ps.setString(2, cif);
                        ps.setInt(3, id);
                        ps.executeUpdate();
                        cargarEmpresas();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Error al modificar: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona primero una empresa.");
            }
        });
    }
}