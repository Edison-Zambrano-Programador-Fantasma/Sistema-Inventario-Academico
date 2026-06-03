package modelo;

/**
 *
 * @author GRUPO 3 - 
 */
public class Categoria {

    //Atributos
    private int idCategoria;
    private String nombre;
    private String descripcion;
    private String estado;

    //Constructor
    public Categoria() {
        this.idCategoria = 0;
        this.nombre = "";
        this.descripcion = "";
        this.estado = "";
    }

    public Categoria(int idCategoria, String nombre, String descripcion, String estado) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
    }
    
    
    //metodos get and set
    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
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
