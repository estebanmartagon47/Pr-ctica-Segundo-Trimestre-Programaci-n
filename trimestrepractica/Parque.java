package trimestrepractica;

/**
 * Modelo de la tabla Parques
 */
public class Parque {
    private int idParque;
    private String nombreParque;
    private String ubicacion;
    private double precioEntrada;

    // Getters y setters
    public int getIdParque() { return idParque; }
    public void setIdParque(int idParque) { this.idParque = idParque; }

    public String getNombreParque() { return nombreParque; }
    public void setNombreParque(String nombreParque) { this.nombreParque = nombreParque; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public double getPrecioEntrada() { return precioEntrada; }
    public void setPrecioEntrada(double precioEntrada) { this.precioEntrada = precioEntrada; }
}