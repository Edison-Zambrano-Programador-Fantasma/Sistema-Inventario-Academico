package modelo;

/**
 *
 * @author GRUPO 3 -
 */
public class Usuario {

    //Atributos
    private int idUsuario;
    private TipoUsuario tipo_usuario;
    private String nombres;
    private String apellidos;
    private String correo;
    private String clave;
    private String estado;

    //Constructor
    public Usuario() {
        this.idUsuario = 0;
        this.tipo_usuario = new TipoUsuario();
        this.nombres = "";
        this.apellidos = "";
        this.correo = "";
        this.clave = "";
        this.estado = "";
    }

    //Metodos Get and Set
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public TipoUsuario getTipo_usuario() {
        return tipo_usuario;
    }

    public void setTipo_usuario(TipoUsuario tipo_usuario) {
        this.tipo_usuario = tipo_usuario;
    }

    
    
    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
