package trimestrepractica;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

/**
 * Consulta y gestión de Usuarios
 */
public class ConsultaUsuarios extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTable tabla;
    private DefaultTableModel modelo;
    private JButton btnAlta, btnBaja, btnModificar;
    private String tipoUsuario;

    public ConsultaUsuarios(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
        setTitle("Gestión de Usuarios");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new String[]{"ID", "Usuario", "Contraseña", "Tipo"});
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
        cargarUsuarios();
        agregarListeners();
    }

    private void controlarPermisos() {
        if (!tipoUsuario.equalsIgnoreCase("Administrador")) {
            btnAlta.setEnabled(false);
            btnBaja.setEnabled(false);
            btnModificar.setEnabled(false);
        }
    }

    private void cargarUsuarios() {
        try (Connection con = ConexionBD.getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Usuarios");
            modelo.setRowCount(0);
            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("idUsuario"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("tipos")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + ex.getMessage());
        }
    }

    private void agregarListeners() {
        btnAlta.addActionListener(e -> {
            String usuario = JOptionPane.showInputDialog(this, "Nombre de usuario:");
            String password = JOptionPane.showInputDialog(this, "Contraseña:");
            String tipo = JOptionPane.showInputDialog(this, "Tipo de usuario (Administrador/Usuario):");
            if (usuario != null && password != null && tipo != null) {
                try (Connection con = ConexionBD.getConnection()) {
                    PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO Usuarios(username, password, tipos) VALUES(?, ?, ?)");
                    ps.setString(1, usuario);
                    ps.setString(2, password);
                    ps.setString(3, tipo);
                    ps.executeUpdate();
                    cargarUsuarios();
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
                        "¿Seguro que quieres borrar el usuario?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (resp == JOptionPane.YES_OPTION) {
                    try (Connection con = ConexionBD.getConnection()) {
                        PreparedStatement ps = con.prepareStatement(
                                "DELETE FROM Usuarios WHERE idUsuario = ?");
                        ps.setInt(1, id);
                        ps.executeUpdate();
                        cargarUsuarios();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Error al borrar: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona primero un usuario.");
            }
        });

        btnModificar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                int id = (int) modelo.getValueAt(fila, 0);
                String usuario = JOptionPane.showInputDialog(this, "Nuevo usuario:", modelo.getValueAt(fila, 1));
                String password = JOptionPane.showInputDialog(this, "Nueva contraseña:", modelo.getValueAt(fila, 2));
                String tipo = JOptionPane.showInputDialog(this, "Nuevo tipo:", modelo.getValueAt(fila, 3));
                if (usuario != null && password != null && tipo != null) {
                    try (Connection con = ConexionBD.getConnection()) {
                        PreparedStatement ps = con.prepareStatement(
                                "UPDATE Usuarios SET username=?, password=?, tipos=? WHERE idUsuario=?");
                        ps.setString(1, usuario);
                        ps.setString(2, password);
                        ps.setString(3, tipo);
                        ps.setInt(4, id);
                        ps.executeUpdate();
                        cargarUsuarios();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Error al modificar: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona primero un usuario.");
            }
        });
    }
}