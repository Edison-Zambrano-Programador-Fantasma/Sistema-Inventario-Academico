package controlador;

import conexion.Conexion;
import modelo.Mantenimiento;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import modelo.Categoria;
import modelo.Equipo;
import vista.InterEquipo;
import vista.InterMantenimiento;
import vista.NuevoEquipoDialogForm;
import vista.NuevoMantenimientoDialogForm;

public class MantenimientoController {

    /**
     * **********************************************
     * Método para guardar un mantenimiento
     * **********************************************
     */
    public boolean guardar(Mantenimiento objeto) {

        boolean respuesta = false;

        String sql = "INSERT INTO mantenimiento "
                + "(id_equipo, fecha_inicio, fecha_fin, descripcion, estado) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection cn = Conexion.conectar(); PreparedStatement consulta = cn.prepareStatement(sql)) {

            consulta.setInt(1, objeto.getEquipo().getIdEquipo());
            consulta.setDate(2, java.sql.Date.valueOf(objeto.getFechaInicio()));
            consulta.setDate(3, java.sql.Date.valueOf(objeto.getFechaFin()));
            consulta.setString(4, objeto.getDescripcion());
            consulta.setString(5, objeto.getEstado());

            respuesta = consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al guardar mantenimiento: " + e.getMessage());
        }

        return respuesta;
    }

    /**
     * **********************************************
     * Método para actualizar mantenimiento
     * **********************************************
     */
    public boolean actualizar(Mantenimiento objeto, int idMantenimiento) {

        boolean respuesta = false;

        String sql = "UPDATE mantenimiento SET "
                + "id_equipo = ?, "
                + "fecha_inicio = ?, "
                + "fecha_fin = ?, "
                + "descripcion = ?, "
                + "estado = ? "
                + "WHERE id_mantenimiento = ?";

        try (Connection cn = Conexion.conectar()) {

            cn.setAutoCommit(false);

            try (
                    PreparedStatement consulta = cn.prepareStatement(sql); PreparedStatement updateEquipo = cn.prepareStatement(
                    "UPDATE equipo SET status = ? WHERE id_equipo = ?")) {

                // Actualizar mantenimiento
                consulta.setInt(1, objeto.getEquipo().getIdEquipo());

                consulta.setDate(2,
                        java.sql.Date.valueOf(objeto.getFechaInicio()));

                consulta.setDate(3,
                        java.sql.Date.valueOf(objeto.getFechaFin()));

                consulta.setString(4, objeto.getDescripcion());
                consulta.setString(5, objeto.getEstado());

                consulta.setInt(6, idMantenimiento);

                int filasMantenimiento = consulta.executeUpdate();

                // Actualizar status del equipo
                updateEquipo.setString(
                        1,
                        objeto.getEquipo().getStatus());

                updateEquipo.setInt(
                        2,
                        objeto.getEquipo().getIdEquipo());

                int filasEquipo = updateEquipo.executeUpdate();

                if (filasMantenimiento > 0 && filasEquipo > 0) {
                    cn.commit();
                    respuesta = true;
                } else {
                    cn.rollback();
                }

            } catch (SQLException e) {
                cn.rollback();
                throw e;
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al actualizar mantenimiento: "
                    + e.getMessage());
        }

        return respuesta;
    }

    /**
     * **********************************************
     * Método para eliminar mantenimiento
     * **********************************************
     */
    public boolean eliminar(int idMantenimiento) {

        boolean respuesta = false;

        String sql = "DELETE FROM mantenimiento WHERE id_mantenimiento = ?";

        try (Connection cn = Conexion.conectar(); PreparedStatement consulta = cn.prepareStatement(sql)) {

            consulta.setInt(1, idMantenimiento);

            respuesta = consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar mantenimiento: " + e.getMessage());
        }

        return respuesta;
    }

    /**
     * **********************************************
     * Método para listar mantenimientos
     * **********************************************
     */
    public ArrayList<Mantenimiento> obtenerMantenimientos() {

        ArrayList<Mantenimiento> lista = new ArrayList<>();

        String sql
                = "SELECT m.*, "
                + "e.id_equipo, e.codigo, e.nombre AS nombre_equipo, e.status as status "
                + "FROM mantenimiento m "
                + "INNER JOIN equipo e "
                + "ON m.id_equipo = e.id_equipo";

        try (Connection cn = Conexion.conectar(); Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                Mantenimiento mantenimiento = new Mantenimiento();
                Equipo equipo = new Equipo();

                mantenimiento.setIdMantenimiento(
                        rs.getInt("id_mantenimiento"));

                equipo.setIdEquipo(
                        rs.getInt("id_equipo"));

                equipo.setCodigo(
                        rs.getString("codigo"));

                equipo.setNombre(
                        rs.getString("nombre_equipo"));

                equipo.setStatus(rs.getString("status"));

                mantenimiento.setEquipo(equipo);

                java.sql.Date fechaInicio
                        = rs.getDate("fecha_inicio");

                if (fechaInicio != null) {
                    mantenimiento.setFechaInicio(
                            fechaInicio.toLocalDate());
                }

                java.sql.Date fechaFin
                        = rs.getDate("fecha_fin");

                if (fechaFin != null) {
                    mantenimiento.setFechaFin(
                            fechaFin.toLocalDate());
                }

                mantenimiento.setDescripcion(
                        rs.getString("descripcion"));

                mantenimiento.setEstado(
                        rs.getString("estado"));

                lista.add(mantenimiento);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar mantenimientos: "
                    + e.getMessage());
        }

        return lista;
    }

    /**
     * **************************************************
     * Método que envía los datos seleccionados al modal
     * **************************************************
     */
    public void enviarDatosAModal(int idMantenimiento) {

        String sql = """
        SELECT m.*, e.codigo, e.marca, e.status
        FROM mantenimiento m
        INNER JOIN equipo e ON m.id_equipo = e.id_equipo
        WHERE m.id_mantenimiento = ?
    """;

        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql)) {

            pst.setInt(1, idMantenimiento);

            try (ResultSet rs = pst.executeQuery()) {

                if (rs.next()) {

                    // Crear instancia del diálogo
                    NuevoMantenimientoDialogForm dialog
                            = new NuevoMantenimientoDialogForm(
                                    (JFrame) SwingUtilities.getWindowAncestor(
                                            InterMantenimiento.jTable_mantenimientos),
                                    true);

                    // Configurar botones
                    NuevoMantenimientoDialogForm.jButton_guardar.setEnabled(false);
                    NuevoMantenimientoDialogForm.jButton_actualizar.setEnabled(true);

                    // Seleccionar equipo
                    seleccionarEquipoEnCombo(
                            NuevoMantenimientoDialogForm.jComboBox_equipos,
                            rs.getInt("id_equipo")
                    );

                    // Mostrar código y marca
                    NuevoMantenimientoDialogForm.txt_codigo.setText(
                            rs.getString("codigo")
                            + " - "
                            + rs.getString("marca")
                    );

                    NuevoMantenimientoDialogForm.jComboBox_status.setSelectedItem(
                            rs.getString("status")
                    );

                    // Fechas
                    NuevoMantenimientoDialogForm.jDateChooser_fecha_inicio.setDate(
                            rs.getDate("fecha_inicio")
                    );

                    NuevoMantenimientoDialogForm.jDateChooser_fecha_fin.setDate(
                            rs.getDate("fecha_fin")
                    );

                    // Descripción
                    NuevoMantenimientoDialogForm.jTextArea_descripcion.setText(
                            rs.getString("descripcion")
                    );

                    // Estado
                    NuevoMantenimientoDialogForm.jComboBox_estado.setSelectedItem(
                            rs.getString("estado")
                    );

                    dialog.setVisible(true);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al seleccionar mantenimiento: "
                    + e.getMessage());
        }
    }

    private void seleccionarEquipoEnCombo(
            JComboBox combo,
            int idEquipo) {

        for (int i = 0; i < combo.getItemCount(); i++) {

            Equipo equipo = (Equipo) combo.getItemAt(i);

            if (equipo.getIdEquipo() == idEquipo) {
                combo.setSelectedIndex(i);
                break;
            }
        }
    }

    /**
     * **************************************************
     * Método que nos permite filtrar los mantenimientos por categorias y status de los equipos 
     * **************************************************
     */
    public ArrayList<Mantenimiento> filtrarMantenimientos(
            Integer idCategoria,
            String status) {

        ArrayList<Mantenimiento> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT m.*, "
                + "e.codigo, "
                + "e.nombre AS nombre_equipo, "
                + "e.status, "
                + "c.id_categoria, "
                + "c.nombre AS nombre_categoria "
                + "FROM mantenimiento m "
                + "INNER JOIN equipo e "
                + "ON m.id_equipo = e.id_equipo "
                + "INNER JOIN categoria c "
                + "ON e.id_categoria = c.id_categoria "
                + "WHERE 1=1 ");

        if (idCategoria != null) {
            sql.append(" AND c.id_categoria = ? ");
        }

        if (status != null && !status.isEmpty()) {
            sql.append(" AND e.status = ? ");
        }

        try (
                Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql.toString())) {

            int parametro = 1;

            if (idCategoria != null) {
                pst.setInt(parametro++, idCategoria);
            }

            if (status != null && !status.isEmpty()) {
                pst.setString(parametro++, status);
            }

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                Mantenimiento mantenimiento = new Mantenimiento();
                Equipo equipo = new Equipo();
                Categoria categoria = new Categoria();

                mantenimiento.setIdMantenimiento(
                        rs.getInt("id_mantenimiento"));

                categoria.setIdCategoria(
                        rs.getInt("id_categoria"));

                categoria.setNombre(
                        rs.getString("nombre_categoria"));

                equipo.setIdEquipo(
                        rs.getInt("id_equipo"));

                equipo.setCodigo(
                        rs.getString("codigo"));

                equipo.setNombre(
                        rs.getString("nombre_equipo"));

                equipo.setStatus(
                        rs.getString("status"));

                equipo.setCategoria(categoria);

                mantenimiento.setEquipo(equipo);

                java.sql.Date fechaInicio
                        = rs.getDate("fecha_inicio");

                if (fechaInicio != null) {
                    mantenimiento.setFechaInicio(
                            fechaInicio.toLocalDate());
                }

                java.sql.Date fechaFin
                        = rs.getDate("fecha_fin");

                if (fechaFin != null) {
                    mantenimiento.setFechaFin(
                            fechaFin.toLocalDate());
                }

                mantenimiento.setDescripcion(
                        rs.getString("descripcion"));

                mantenimiento.setEstado(
                        rs.getString("estado"));

                lista.add(mantenimiento);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al filtrar mantenimientos: "
                    + e.getMessage());
        }

        return lista;
    }
//
//    /**
//     * **************************************************
//     * Método que envía los datos seleccionados al modal
//     * **************************************************
//     */
//    public void enviarDatosAModal(int idEquipo) {
//
//        String sql = "SELECT * FROM mantenimiento WHERE id_equipo = ?";
//
//        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql)) {
//
//            pst.setInt(1, idEquipo);
//
//            try (ResultSet rs = pst.executeQuery()) {
//
//                if (rs.next()) {
//
//                    // Crear instancia del diálogo
//                    NuevoMantenimientoDialogForm dialog
//                            = new NuevoMantenimientoDialogForm(
//                                    (JFrame) SwingUtilities.getWindowAncestor(
//                                            InterEquipo.jTable_equipos), true
//                            );
//
//                    // Configurar botones
//                    NuevoMantenimientoDialogForm.jButton_guardar.setEnabled(false);
//                    NuevoMantenimientoDialogForm.jButton_actualizar.setEnabled(true);
//
//                    // Cargar datos en el formulario
//                    NuevoMantenimientoDialogForm.jComboBox_categorias.setSelectedIndex(rs.getInt("id_categoria"));
//                    NuevoMantenimientoDialogForm.jComboBox_ubicaciones.setSelectedIndex(rs.getInt("id_ubicacion"));
//                    NuevoMantenimientoDialogForm.txt_codigo.setText(rs.getString("codigo"));
//                    NuevoMantenimientoDialogForm.txt_nombre.setText(rs.getString("nombre"));
//                    NuevoMantenimientoDialogForm.txt_modelo.setText(rs.getString("modelo"));
//                    NuevoMantenimientoDialogForm.txt_marca.setText(rs.getString("marca"));
//                    NuevoMantenimientoDialogForm.txt_status.setText(rs.getString("status"));
//                    NuevoMantenimientoDialogForm.jComboBox_estado.setSelectedItem(rs.getString("estado"));
//
//                    // Mostrar diálogo
//                    dialog.setVisible(true);
//                }
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Error al seleccionar equipo: "
//                    + e.getMessage());
//        }
//    }
}
