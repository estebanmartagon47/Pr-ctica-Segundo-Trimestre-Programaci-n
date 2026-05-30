package trimestrepractica;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

// 📄 PDF
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;

/**
 * Gestión de Empresas
 * CRUD + LOG + EXPORTACIÓN PDF + JTable
 */
public class ConsultaEmpresa extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTable tabla;
    private DefaultTableModel modelo;

    private JButton btnAlta, btnBaja, btnModificar, btnPDF;

    private String tipoUsuario;

    public ConsultaEmpresa(String tipoUsuario) {

        this.tipoUsuario = tipoUsuario;

        setTitle("Gestión de Empresas");
        setSize(650, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        // ======================
        // TABLA
        // ======================
        modelo = new DefaultTableModel();

        modelo.setColumnIdentifiers(new String[]{
                "ID", "Nombre", "Fecha", "CIF"
        });

        tabla = new JTable(modelo);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(20, 20, 580, 200);
        add(scroll);

        // ======================
        // BOTONES
        // ======================
        btnAlta = new JButton("Alta");
        btnAlta.setBounds(50, 250, 100, 30);
        add(btnAlta);

        btnBaja = new JButton("Baja");
        btnBaja.setBounds(180, 250, 100, 30);
        add(btnBaja);

        btnModificar = new JButton("Modificar");
        btnModificar.setBounds(310, 250, 120, 30);
        add(btnModificar);

        btnPDF = new JButton("Exportar PDF");
        btnPDF.setBounds(460, 250, 150, 30);
        add(btnPDF);

        controlarPermisos();
        cargarEmpresas();
        eventos();

        setVisible(true);
    }

    // ======================
    // PERMISOS
    // ======================
    private void controlarPermisos() {

        if (!tipoUsuario.equalsIgnoreCase("Administrador")) {
            btnAlta.setEnabled(true);
            btnBaja.setEnabled(false);
            btnModificar.setEnabled(false);
        }
    }

    // ======================
    // CARGA DATOS (JTABLE)
    // ======================
    private void cargarEmpresas() {

        try (Connection con = ConexionBD.getConnection()) {

            String sql = "SELECT * FROM Empresas";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            modelo.setRowCount(0);

            while (rs.next()) {

                modelo.addRow(new Object[]{
                        rs.getInt("idEmpresa"),
                        rs.getString("nombreEmpresa"),
                        rs.getDate("fechaCreacion"),
                        rs.getString("cif")
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar empresas: " + ex.getMessage());
        }
    }

    // ======================
    // EVENTOS
    // ======================
    private void eventos() {

        // 🔵 ALTA
        btnAlta.addActionListener(e -> {

            String nombre = JOptionPane.showInputDialog("Nombre empresa:");
            String cif = JOptionPane.showInputDialog("CIF:");

            if (nombre != null && cif != null) {

                try (Connection con = ConexionBD.getConnection()) {

                    PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO Empresas(nombreEmpresa, fechaCreacion, cif) VALUES(?, CURRENT_DATE, ?)"
                    );

                    ps.setString(1, nombre);
                    ps.setString(2, cif);

                    ps.executeUpdate();

                    // 📌 LOG
                    Log.escribir(tipoUsuario, "Alta empresa: " + nombre);

                    cargarEmpresas();

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        // 🔴 BAJA
        btnBaja.addActionListener(e -> {

            int fila = tabla.getSelectedRow();

            if (fila >= 0) {

                int id = (int) modelo.getValueAt(fila, 0);

                try (Connection con = ConexionBD.getConnection()) {

                    PreparedStatement ps = con.prepareStatement(
                            "DELETE FROM Empresas WHERE idEmpresa=?"
                    );

                    ps.setInt(1, id);
                    ps.executeUpdate();

                    // 📌 LOG
                    Log.escribir(tipoUsuario, "Baja empresa ID: " + id);

                    cargarEmpresas();

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }

            } else {
                JOptionPane.showMessageDialog(this, "Selecciona una empresa");
            }
        });

        // 🟡 MODIFICAR
        btnModificar.addActionListener(e -> {

            int fila = tabla.getSelectedRow();

            if (fila >= 0) {

                int id = (int) modelo.getValueAt(fila, 0);

                String nombre = JOptionPane.showInputDialog(
                        "Nombre:",
                        modelo.getValueAt(fila, 1)
                );

                String cif = JOptionPane.showInputDialog(
                        "CIF:",
                        modelo.getValueAt(fila, 3)
                );

                if (nombre != null && cif != null) {

                    try (Connection con = ConexionBD.getConnection()) {

                        PreparedStatement ps = con.prepareStatement(
                                "UPDATE Empresas SET nombreEmpresa=?, cif=? WHERE idEmpresa=?"
                        );

                        ps.setString(1, nombre);
                        ps.setString(2, cif);
                        ps.setInt(3, id);

                        ps.executeUpdate();

                        // 📌 LOG
                        Log.escribir(tipoUsuario, "Modificación empresa ID: " + id);

                        cargarEmpresas();

                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                    }
                }

            } else {
                JOptionPane.showMessageDialog(this, "Selecciona una empresa");
            }
        });

        // 📄 PDF
        btnPDF.addActionListener(e -> exportarPDF());
    }

    // ======================
    // EXPORTAR PDF
    // ======================
    private void exportarPDF() {

        try {

            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream("Empresas.pdf"));

            doc.open();

            PdfPTable table = new PdfPTable(4);

            table.addCell("ID");
            table.addCell("Nombre");
            table.addCell("Fecha");
            table.addCell("CIF");

            for (int i = 0; i < modelo.getRowCount(); i++) {

                table.addCell(modelo.getValueAt(i, 0).toString());
                table.addCell(modelo.getValueAt(i, 1).toString());
                table.addCell(modelo.getValueAt(i, 2).toString());
                table.addCell(modelo.getValueAt(i, 3).toString());
            }

            doc.add(table);
            doc.close();

            // 📌 LOG
            Log.escribir(tipoUsuario, "Exportación PDF Empresas");

            JOptionPane.showMessageDialog(this, "PDF generado correctamente");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error PDF: " + ex.getMessage());
        }
    }
}