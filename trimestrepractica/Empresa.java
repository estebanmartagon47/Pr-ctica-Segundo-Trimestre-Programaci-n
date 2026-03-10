package trimestrepractica;

/**
 * Modelo de la tabla Empresas
 */
public class Empresa {
    private int idEmpresa;
    private String nombreEmpresa;
    private String fechaCreacion;
    private String cif;

    // Getters y setters
    public int getIdEmpresa() { return idEmpresa; }
    public void setIdEmpresa(int idEmpresa) { this.idEmpresa = idEmpresa; }

    public String getNombreEmpresa() { return nombreEmpresa; }
    public void setNombreEmpresa(String nombreEmpresa) { this.nombreEmpresa = nombreEmpresa; }

    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getCif() { return cif; }
    public void setCif(String cif) { this.cif = cif; }
}