package trimestrepractica;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

/**
 * Consulta y gestión de Parques
 */
public class ConsultaParques extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTable tabla;
    private DefaultTableModel modelo;
    private JButton btnAlta, btnBaja, btnModificar;
    private String tipoUsuario;

    public ConsultaParques(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
        setTitle("Gestión de Parques");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new String[]{"ID", "Nombre", "Ubicación", "Precio Entrada", "ID Empresa"});
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(20, 20, 640, 200);
        add(scroll);

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
        cargarParques();
        agregarListeners();
    }

    private void controlarPermisos() {
        if (!tipoUsuario.equalsIgnoreCase("Administrador")) {
            btnAlta.setEnabled(false);
            btnBaja.setEnabled(false);
            btnModificar.setEnabled(false);
        }
    }

    private void cargarParques() {
        try (Connection con = ConexionBD.getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Parques");
            modelo.setRowCount(0);
            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("idParque"),
                        rs.getString("nombreParque"),
                        rs.getString("ubicacion"),
                        rs.getBigDecimal("precioEntrada"),
                        rs.getInt("idEmpresa")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar parques: " + ex.getMessage());
        }
    }

    private void agregarListeners() {
        btnAlta.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog(this, "Nombre parque:");
            String ubicacion = JOptionPane.showInputDialog(this, "Ubicación:");
            String precioStr = JOptionPane.showInputDialog(this, "Precio Entrada:");
            String idEmpStr = JOptionPane.showInputDialog(this, "ID Empresa:");
            if (nombre != null && ubicacion != null && precioStr != null && idEmpStr != null) {
                try (Connection con = ConexionBD.getConnection()) {
                    PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO Parques(nombreParque, ubicacion, precioEntrada, idEmpresa) VALUES(?, ?, ?, ?)");
                    ps.setString(1, nombre);
                    ps.setString(2, ubicacion);
                    ps.setBigDecimal(3, new java.math.BigDecimal(precioStr));
                    ps.setInt(4, Integer.parseInt(idEmpStr));
                    ps.executeUpdate();
                    cargarParques();
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
                        "¿Seguro que quieres borrar el parque?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (resp == JOptionPane.YES_OPTION) {
                    try (Connection con = ConexionBD.getConnection()) {
                        PreparedStatement ps = con.prepareStatement(
                                "DELETE FROM Parques WHERE idParque = ?");
                        ps.setInt(1, id);
                        ps.executeUpdate();
                        cargarParques();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Error al borrar: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona primero un parque.");
            }
        });

        btnModificar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                int id = (int) modelo.getValueAt(fila, 0);
                String nombre = JOptionPane.showInputDialog(this, "Nuevo nombre:", modelo.getValueAt(fila, 1));
                String ubicacion = JOptionPane.showInputDialog(this, "Nueva ubicación:", modelo.getValueAt(fila, 2));
                String precioStr = JOptionPane.showInputDialog(this, "Nuevo precio:", modelo.getValueAt(fila, 3));
                String idEmpStr = JOptionPane.showInputDialog(this, "Nuevo ID Empresa:", modelo.getValueAt(fila, 4));
                if (nombre != null && ubicacion != null && precioStr != null && idEmpStr != null) {
                    try (Connection con = ConexionBD.getConnection()) {
                        PreparedStatement ps = con.prepareStatement(
                                "UPDATE Parques SET nombreParque=?, ubicacion=?, precioEntrada=?, idEmpresa=? WHERE idParque=?");
                        ps.setString(1, nombre);
                        ps.setString(2, ubicacion);
                        ps.setBigDecimal(3, new java.math.BigDecimal(precioStr));
                        ps.setInt(4, Integer.parseInt(idEmpStr));
                        ps.setInt(5, id);
                        ps.executeUpdate();
                        cargarParques();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Error al modificar: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona primero un parque.");
            }
        });
    }
}