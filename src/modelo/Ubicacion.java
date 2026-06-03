package modelo;

/**
 *
 * @author GRUPO 3 -
 */
public class Ubicacion {

    //Atributos
    private int idUbicacion;
    private String nombre;
    private String descripcion;
    private String estado;

    //Constructor
    public Ubicacion() {
        this.idUbicacion = 0;
        this.nombre = "";
        this.descripcion = "";
        this.estado = "";
    }

    public Ubicacion(int idUbicacion, String nombre, String descripcion, String estado) {
        this.idUbicacion = idUbicacion;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
    } 

    //Metodos get and set
    public int getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(int idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
