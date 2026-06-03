package modelo;

/**
 *
 * @author GRUPO 3 -
 */
public class Equipo {

    //Atributos
    private int idEquipo;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private String codigo;
    private String nombre;
    private String marca;
    private String modelo;
    private String status;
    private String estado;

    //Constructor
    public Equipo() {
        this.idEquipo = 0;
        this.categoria = new Categoria();
        this.ubicacion = new Ubicacion();
        this.codigo = "";
        this.nombre = "";
        this.marca = "";
        this.modelo = "";
        this.status = "";
        this.estado = "";
    }

    public Equipo(int idEquipo, Categoria categoria, Ubicacion ubicacion, String codigo, String nombre, String marca, String modelo, String status, String estado) {
        this.idEquipo = idEquipo;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.codigo = codigo;
        this.nombre = nombre;
        this.marca = marca;
        this.modelo = modelo;
        this.status = status;
        this.estado = estado;
    }

    //Metodos get and set
    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return nombre;
    }

}
