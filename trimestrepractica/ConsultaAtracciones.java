package trimestrepractica;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.sql.Date;

// 📌 Librería PDF (iText)
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;

/**
 * Ventana de gestión de Atracciones
 * Incluye: CRUD + JOIN + LOG + EXPORTAR PDF
 */
public class ConsultaAtracciones extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTable tabla;
    private DefaultTableModel modelo;

    private JButton btnAlta, btnBaja, btnModificar, btnPDF;

    private String tipoUsuario;

    public ConsultaAtracciones(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;

        setTitle("Gestión de Atracciones");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setLayout(null);

        // 📊 Modelo de tabla (no editable)
        modelo = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Columnas visibles (JOIN incluido)
        modelo.setColumnIdentifiers(new String[]{
                "ID", "Nombre", "Precio", "Parque"
        });

        tabla = new JTable(modelo);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(20, 20, 690, 200);
        add(scroll);

        // 🔘 Botones
        btnAlta = new JButton("Alta");
        btnAlta.setBounds(50, 250, 120, 30);
        add(btnAlta);

        btnBaja = new JButton("Baja");
        btnBaja.setBounds(220, 250, 120, 30);
        add(btnBaja);

        btnModificar = new JButton("Modificar");
        btnModificar.setBounds(390, 250, 120, 30);
        add(btnModificar);

        btnPDF = new JButton("Exportar PDF");
        btnPDF.setBounds(560, 250, 150, 30);
        add(btnPDF);

        // 🔐 permisos
        if (!tipoUsuario.equalsIgnoreCase("Administrador")) {
            btnAlta.setEnabled(true);
            btnBaja.setEnabled(false);
            btnModificar.setEnabled(false);
        }

        cargarAtracciones();
        eventos();

        setVisible(true);
    }

    /**
     * 📌 CARGAR ATRACCIONES CON JOIN
     * Sustituimos idParque por nombreParque
     */
    private void cargarAtracciones() {

        try (Connection con = ConexionBD.getConnection()) {

            String sql =
                    "SELECT a.idAtraccion, a.nombreAtraccion, a.precio, p.nombreParque " +
                    "FROM Atracciones a " +
                    "JOIN Parques p ON a.idParque = p.idParque";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            modelo.setRowCount(0);

            while (rs.next()) {

                modelo.addRow(new Object[]{
                        rs.getInt("idAtraccion"),
                        rs.getString("nombreAtraccion"),
                        rs.getDouble("precio"),
                        rs.getString("nombreParque")
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar atracciones: " + ex.getMessage());
        }
    }

    /**
     * 📌 EVENTOS CRUD + PDF + LOG
     */
    private void eventos() {

        // 🟢 ALTA
        btnAlta.addActionListener(e -> {

            JTextField nombre = new JTextField();
            JTextField precio = new JTextField();
            JComboBox<String> parques = new JComboBox<>();

            Date fechaCreacion = new Date(System.currentTimeMillis());

            try (Connection con = ConexionBD.getConnection()) {

                ResultSet rs = con.createStatement()
                        .executeQuery("SELECT idParque, nombreParque FROM Parques");

                while (rs.next()) {
                    parques.addItem(rs.getInt("idParque") + " - " + rs.getString("nombreParque"));
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error cargando parques");
            }

            Object[] campos = {
                    "Nombre:", nombre,
                    "Precio:", precio,
                    "Parque:", parques
            };

            int op = JOptionPane.showConfirmDialog(this, campos, "Nueva Atracción", JOptionPane.OK_CANCEL_OPTION);

            if (op == JOptionPane.OK_OPTION) {

                try (Connection con = ConexionBD.getConnection()) {

                    int idParque = Integer.parseInt(parques.getSelectedItem().toString().split(" - ")[0]);

                    PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO Atracciones(nombreAtraccion,precio,idParque,fechaCreacion) VALUES(?,?,?,?)"
                    );

                    ps.setString(1, nombre.getText());
                    ps.setDouble(2, Double.parseDouble(precio.getText()));
                    ps.setInt(3, idParque);
                    ps.setDate(4, fechaCreacion);

                    ps.executeUpdate();

                    // 🔥 LOG
                    Log.escribir(tipoUsuario, "Alta atracción: " + nombre.getText());

                    cargarAtracciones();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al insertar atracción");
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
                            "DELETE FROM Atracciones WHERE idAtraccion=?"
                    );

                    ps.setInt(1, id);
                    ps.executeUpdate();

                    // 🔥 LOG
                    Log.escribir(tipoUsuario, "Baja atracción ID: " + id);

                    cargarAtracciones();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar");
                }

            } else {
                JOptionPane.showMessageDialog(this, "Selecciona una atracción");
            }
        });

        // 🟡 MODIFICAR
        btnModificar.addActionListener(e -> {

            int fila = tabla.getSelectedRow();

            if (fila >= 0) {

                int id = (int) modelo.getValueAt(fila, 0);

                JTextField nombre = new JTextField((String) modelo.getValueAt(fila, 1));
                JTextField precio = new JTextField(String.valueOf(modelo.getValueAt(fila, 2)));

                Object[] campos = {
                        "Nombre:", nombre,
                        "Precio:", precio
                };

                int op = JOptionPane.showConfirmDialog(this, campos, "Modificar", JOptionPane.OK_CANCEL_OPTION);

                if (op == JOptionPane.OK_OPTION) {

                    try (Connection con = ConexionBD.getConnection()) {

                        PreparedStatement ps = con.prepareStatement(
                                "UPDATE Atracciones SET nombreAtraccion=?, precio=? WHERE idAtraccion=?"
                        );

                        ps.setString(1, nombre.getText());
                        ps.setDouble(2, Double.parseDouble(precio.getText()));
                        ps.setInt(3, id);

                        ps.executeUpdate();

                        // 🔥 LOG
                        Log.escribir(tipoUsuario, "Modificación atracción ID: " + id);

                        cargarAtracciones();

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Error al modificar");
                    }
                }

            } else {
                JOptionPane.showMessageDialog(this, "Selecciona una fila");
            }
        });

        // 📄 PDF EXPORT
        btnPDF.addActionListener(e -> exportarPDF());
    }

    /**
     * 📄 EXPORTAR A PDF
     */
    private void exportarPDF() {

        try {

            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream("Atracciones.pdf"));

            doc.open();

            PdfPTable pdfTable = new PdfPTable(4);

            pdfTable.addCell("ID");
            pdfTable.addCell("Nombre");
            pdfTable.addCell("Precio");
            pdfTable.addCell("Parque");

            for (int i = 0; i < modelo.getRowCount(); i++) {

                pdfTable.addCell(modelo.getValueAt(i, 0).toString());
                pdfTable.addCell(modelo.getValueAt(i, 1).toString());
                pdfTable.addCell(modelo.getValueAt(i, 2).toString());
                pdfTable.addCell(modelo.getValueAt(i, 3).toString());
            }

            doc.add(pdfTable);
            doc.close();

            // 🔥 LOG
            Log.escribir(tipoUsuario, "Exportación PDF Atracciones");

            JOptionPane.showMessageDialog(this, "PDF generado correctamente");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error PDF: " + ex.getMessage());
        }
    }
}