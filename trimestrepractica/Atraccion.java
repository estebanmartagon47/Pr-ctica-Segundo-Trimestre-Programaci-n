package trimestrepractica;

/**
 * Modelo de la tabla Atracciones
 */
public class Atraccion {
    private int idAtraccion;
    private String nombreAtraccion;
    private double precio;

    // Getters y setters
    public int getIdAtraccion() { return idAtraccion; }
    public void setIdAtraccion(int idAtraccion) { this.idAtraccion = idAtraccion; }

    public String getNombreAtraccion() { return nombreAtraccion; }
    public void setNombreAtraccion(String nombreAtraccion) { this.nombreAtraccion = nombreAtraccion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
}