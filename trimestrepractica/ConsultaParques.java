package trimestrepractica;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;

/**
 * Gestión de Parques
 * CRUD + FK (Empresa) + JOIN + LOG + PDF
 */
public class ConsultaParques extends JFrame {

    private static final long serialVersionUID = 1L;

    // Tabla visual
    private JTable tabla;
    private DefaultTableModel modelo;

    // Botones CRUD + PDF
    private JButton btnAlta, btnBaja, btnModificar, btnPDF;

    private String tipoUsuario;

    public ConsultaParques(String tipoUsuario) {

        this.tipoUsuario = tipoUsuario;

        setTitle("Gestión de Parques");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        // ======================
        // MODELO TABLA
        // ======================
        modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new String[]{
                "ID", "Nombre", "Ubicación", "Precio", "Empresa"
        });

        tabla = new JTable(modelo);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(20, 20, 680, 200);
        add(scroll);

        // ======================
        // BOTONES
        // ======================
        btnAlta = new JButton("Alta");
        btnAlta.setBounds(50, 250, 120, 30);
        add(btnAlta);

        btnBaja = new JButton("Baja");
        btnBaja.setBounds(200, 250, 120, 30);
        add(btnBaja);

        btnModificar = new JButton("Modificar");
        btnModificar.setBounds(350, 250, 120, 30);
        add(btnModificar);

        btnPDF = new JButton("Exportar PDF");
        btnPDF.setBounds(500, 250, 150, 30);
        add(btnPDF);

        // Inicialización
        controlarPermisos();
        cargarParques();
        eventos();

        setVisible(true);
    }

    // ======================
    // PERMISOS USUARIO
    // ======================
    private void controlarPermisos() {

        // Usuario normal NO puede modificar datos
        if (!tipoUsuario.equalsIgnoreCase("Administrador")) {

            btnAlta.setEnabled(false);
            btnBaja.setEnabled(false);
            btnModificar.setEnabled(false);
        }
    }

    // ======================
    // CARGA CON JOIN (FK EMPRESA)
    // ======================
    private void cargarParques() {

        try (Connection con = ConexionBD.getConnection()) {

            String sql =
                    "SELECT p.idParque, p.nombreParque, p.ubicacion, p.precioEntrada, e.nombreEmpresa " +
                    "FROM Parques p " +
                    "JOIN Empresas e ON p.idEmpresa = e.idEmpresa";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            modelo.setRowCount(0);

            while (rs.next()) {

                modelo.addRow(new Object[]{
                        rs.getInt("idParque"),
                        rs.getString("nombreParque"),
                        rs.getString("ubicacion"),
                        rs.getBigDecimal("precioEntrada"),
                        rs.getString("nombreEmpresa")
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar parques: " + ex.getMessage());
        }
    }

    // ======================
    // EVENTOS CRUD + LOG
    // ======================
    private void eventos() {

        // ======================
        // ALTA
        // ======================
        btnAlta.addActionListener(e -> {

            String nombre = JOptionPane.showInputDialog("Nombre parque:");
            String ubicacion = JOptionPane.showInputDialog("Ubicación:");
            String precioStr = JOptionPane.showInputDialog("Precio entrada:");
            String idEmpStr = JOptionPane.showInputDialog("ID Empresa:");

            if (nombre != null && ubicacion != null && precioStr != null && idEmpStr != null) {

                try (Connection con = ConexionBD.getConnection()) {

                    String sql = "INSERT INTO Parques(nombreParque, ubicacion, precioEntrada, idEmpresa) VALUES(?,?,?,?)";

                    PreparedStatement ps = con.prepareStatement(sql);

                    ps.setString(1, nombre);
                    ps.setString(2, ubicacion);
                    ps.setBigDecimal(3, new java.math.BigDecimal(precioStr));
                    ps.setInt(4, Integer.parseInt(idEmpStr));

                    ps.executeUpdate();

                    // LOG
                    Log.escribir(tipoUsuario, "Alta parque: " + nombre);

                    cargarParques();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        // ======================
        // BAJA
        // ======================
        btnBaja.addActionListener(e -> {

            int fila = tabla.getSelectedRow();

            if (fila >= 0) {

                int id = (int) modelo.getValueAt(fila, 0);

                try (Connection con = ConexionBD.getConnection()) {

                    PreparedStatement ps = con.prepareStatement(
                            "DELETE FROM Parques WHERE idParque=?"
                    );

                    ps.setInt(1, id);
                    ps.executeUpdate();

                    // LOG
                    Log.escribir(tipoUsuario, "Baja parque ID: " + id);

                    cargarParques();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }

            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un parque");
            }
        });

        // ======================
        // MODIFICAR
        // ======================
        btnModificar.addActionListener(e -> {

            int fila = tabla.getSelectedRow();

            if (fila >= 0) {

                int id = (int) modelo.getValueAt(fila, 0);

                String nombre = JOptionPane.showInputDialog("Nombre:", modelo.getValueAt(fila, 1));
                String ubicacion = JOptionPane.showInputDialog("Ubicación:", modelo.getValueAt(fila, 2));
                String precioStr = JOptionPane.showInputDialog("Precio:", modelo.getValueAt(fila, 3));
                String idEmpStr = JOptionPane.showInputDialog("ID Empresa:", modelo.getValueAt(fila, 4));

                try (Connection con = ConexionBD.getConnection()) {

                    String sql = "UPDATE Parques SET nombreParque=?, ubicacion=?, precioEntrada=?, idEmpresa=? WHERE idParque=?";

                    PreparedStatement ps = con.prepareStatement(sql);

                    ps.setString(1, nombre);
                    ps.setString(2, ubicacion);
                    ps.setBigDecimal(3, new java.math.BigDecimal(precioStr));
                    ps.setInt(4, Integer.parseInt(idEmpStr));
                    ps.setInt(5, id);

                    ps.executeUpdate();

                    // LOG
                    Log.escribir(tipoUsuario, "Modificación parque ID: " + id);

                    cargarParques();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }

            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un parque");
            }
        });

        // ======================
        // PDF
        // ======================
        btnPDF.addActionListener(e -> exportarPDF());
    }

    // ======================
    // EXPORTAR PDF
    // ======================
    private void exportarPDF() {

        try {

            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream("Parques.pdf"));

            doc.open();

            PdfPTable table = new PdfPTable(5);

            table.addCell("ID");
            table.addCell("Nombre");
            table.addCell("Ubicación");
            table.addCell("Precio");
            table.addCell("Empresa");

            for (int i = 0; i < modelo.getRowCount(); i++) {

                table.addCell(modelo.getValueAt(i, 0).toString());
                table.addCell(modelo.getValueAt(i, 1).toString());
                table.addCell(modelo.getValueAt(i, 2).toString());
                table.addCell(modelo.getValueAt(i, 3).toString());
                table.addCell(modelo.getValueAt(i, 4).toString());
            }

            doc.add(table);
            doc.close();

            // LOG
            Log.escribir(tipoUsuario, "Exportación PDF Parques");

            JOptionPane.showMessageDialog(this, "PDF generado correctamente");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error PDF: " + ex.getMessage());
        }
    }
}