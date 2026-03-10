package trimestrepractica;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ConsultaAtracciones extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable tabla;
    private DefaultTableModel modelo;
    private JButton btnAlta, btnBaja;
    private String tipoUsuario;

    public ConsultaAtracciones(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;

        setTitle("Gestión de Atracciones");
        setSize(700,400);
        setLocationRelativeTo(null);
        setLayout(null);

        modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new String[]{"ID","Nombre","Precio","ID Parque"});

        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(20,20,640,200);
        add(scroll);

        btnAlta = new JButton("Alta");
        btnAlta.setBounds(100,250,120,30);
        add(btnAlta);

        btnBaja = new JButton("Baja");
        btnBaja.setBounds(300,250,120,30);
        add(btnBaja);

        if(!tipoUsuario.equalsIgnoreCase("Administrador")){
            btnAlta.setEnabled(false);
            btnBaja.setEnabled(false);
        }

        cargarAtracciones();
        eventos();
    }

    private void cargarAtracciones(){
        try(Connection con = ConexionBD.getConnection()){
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Atracciones");

            modelo.setRowCount(0);

            while(rs.next()){
                modelo.addRow(new Object[]{
                        rs.getInt("idAtraccion"),
                        rs.getString("nombreAtraccion"),
                        rs.getDouble("precio"),
                        rs.getInt("idParque")
                });
            }

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"Error al cargar atracciones");
        }
    }

    private void eventos(){

        btnAlta.addActionListener(e -> {

            JTextField nombre = new JTextField();
            JTextField precio = new JTextField();
            JComboBox<String> parques = new JComboBox<>();

            try(Connection con = ConexionBD.getConnection()){
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT idParque, nombreParque FROM Parques");

                while(rs.next()){
                    parques.addItem(rs.getInt("idParque") + " - " + rs.getString("nombreParque"));
                }

            }catch(Exception ex){
                JOptionPane.showMessageDialog(this,"Error cargando parques");
            }

            Object[] campos = {
                    "Nombre:", nombre,
                    "Precio:", precio,
                    "Parque:", parques
            };

            int opcion = JOptionPane.showConfirmDialog(this,campos,"Nueva Atracción",JOptionPane.OK_CANCEL_OPTION);

            if(opcion == JOptionPane.OK_OPTION){

                try(Connection con = ConexionBD.getConnection()){

                    String parqueSeleccionado = (String) parques.getSelectedItem();
                    int idParque = Integer.parseInt(parqueSeleccionado.split(" - ")[0]);

                    PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO Atracciones(nombreAtraccion,precio,idParque) VALUES(?,?,?)");

                    ps.setString(1,nombre.getText());
                    ps.setDouble(2,Double.parseDouble(precio.getText()));
                    ps.setInt(3,idParque);

                    ps.executeUpdate();

                    cargarAtracciones();

                }catch(Exception ex){
                    JOptionPane.showMessageDialog(this,"Error al insertar atracción");
                }

            }

        });

        btnBaja.addActionListener(e -> {

            int fila = tabla.getSelectedRow();

            if(fila>=0){

                int id = (int) modelo.getValueAt(fila,0);

                int confirm = JOptionPane.showConfirmDialog(this,
                        "¿Eliminar atracción?");

                if(confirm==JOptionPane.YES_OPTION){

                    try(Connection con = ConexionBD.getConnection()){

                        PreparedStatement ps = con.prepareStatement(
                                "DELETE FROM Atracciones WHERE idAtraccion=?");

                        ps.setInt(1,id);
                        ps.executeUpdate();

                        cargarAtracciones();

                    }catch(Exception ex){
                        JOptionPane.showMessageDialog(this,"Error al eliminar");
                    }

                }

            }else{
                JOptionPane.showMessageDialog(this,"Selecciona una atracción");
            }

        });

    }
}