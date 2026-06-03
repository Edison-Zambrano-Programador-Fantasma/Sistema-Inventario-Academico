package modelo;

/**
 *
 * @author GRUPO 3 -
 */
public class TipoUsuario {

    //Atributos
    private int idTipoUsuario;
    private String descripcion;
    private String estado;

    //Constructor
    public TipoUsuario() {
        this.idTipoUsuario = 0;
        this.descripcion = "";
        this.estado = "";
    }

    public TipoUsuario(int idTipoUsuario, String descripcion, String estado) {
        this.idTipoUsuario = idTipoUsuario;
        this.descripcion = descripcion;
        this.estado = estado;
    }
     

    //metodos get and set
    public int getIdTipoUsuario() {
        return idTipoUsuario;
    }

    public void setIdTipoUsuario(int idTipoUsuario) {
        this.idTipoUsuario = idTipoUsuario;
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
