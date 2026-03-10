package trimestrepractica;

import javax.swing.*;

/**
 * Menú principal de la aplicación.
 * Desde aquí se pueden abrir las ventanas de gestión según el tipo de usuario.
 */
public class MenuPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    private String tipoUsuario;
    private JButton btnEmpresas, btnParques, btnAtracciones, btnUsuarios, btnSalir;

    public MenuPrincipal(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
        initComponents();
    }

    private void initComponents() {
        setTitle("Menú Principal");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        btnEmpresas = new JButton("Gestión Empresas");
        btnEmpresas.setBounds(100, 30, 200, 30);
        add(btnEmpresas);
        btnEmpresas.addActionListener(e -> new ConsultaEmpresa(tipoUsuario).setVisible(true));

        btnParques = new JButton("Gestión Parques");
        btnParques.setBounds(100, 80, 200, 30);
        add(btnParques);
        btnParques.addActionListener(e -> new ConsultaParques(tipoUsuario).setVisible(true));

        btnAtracciones = new JButton("Gestión Atracciones");
        btnAtracciones.setBounds(100, 130, 200, 30);
        add(btnAtracciones);
        btnAtracciones.addActionListener(e -> new ConsultaAtracciones(tipoUsuario).setVisible(true));

        btnUsuarios = new JButton("Gestión Usuarios");
        btnUsuarios.setBounds(100, 180, 200, 30);
        add(btnUsuarios);
        btnUsuarios.addActionListener(e -> new ConsultaUsuarios(tipoUsuario).setVisible(true));

        btnSalir = new JButton("Salir");
        btnSalir.setBounds(100, 230, 200, 30);
        add(btnSalir);
        btnSalir.addActionListener(e -> System.exit(0));
    }
}