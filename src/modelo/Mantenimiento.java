package modelo;

import java.time.LocalDate;

/**
 *
 * @author GRUPO 3 -
 */
public class Mantenimiento {

    //Atributos
    private int idMantenimiento;
    private Equipo equipo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String descripcion;
    private String estado;

    //Constructor
    public Mantenimiento() {
        this.idMantenimiento = 0;
        this.equipo = new Equipo();
        this.fechaInicio = LocalDate.now();
        this.fechaFin = LocalDate.now();
        this.descripcion = "";
        this.estado = "";
    }

    public Mantenimiento(int idMantenimiento, Equipo equipo, LocalDate fechaInicio, LocalDate fechaFin, String descripcion, String estado) {
        this.idMantenimiento = idMantenimiento;
        this.equipo = equipo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.descripcion = descripcion;
        this.estado = estado;
    }



    //Metodos get and set
    public int getIdMantenimiento() {
        return idMantenimiento;
    }

    public void setIdMantenimiento(int idMantenimiento) {
        this.idMantenimiento = idMantenimiento;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }


    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
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
