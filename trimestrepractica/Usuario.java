package trimestrepractica;

/**
 * Modelo de la tabla Usuarios
 */
public class Usuario {
    private int idUsuario;
    private String username;
    private String tipos; // Administrador o Basico
    private String password;

    // Getters y setters
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getTipos() { return tipos; }
    public void setTipos(String tipos) { this.tipos = tipos; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}