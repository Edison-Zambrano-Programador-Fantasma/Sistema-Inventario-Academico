package controlador;

import conexion.Conexion;
import modelo.Equipo;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import modelo.Categoria;
import modelo.Ubicacion;
import vista.InterEquipo;
import vista.NuevoEquipoDialogForm;

public class EquipoController {

    /**
     * **********************************************
     * Método para guardar un nuevo equipo
     * **********************************************
     */
    public boolean guardar(Equipo objeto) {

        boolean respuesta = false;

        String sql = "INSERT INTO equipo "
                + "(id_categoria, id_ubicacion, codigo, nombre, marca, modelo, "
                + "status, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection cn = Conexion.conectar(); PreparedStatement consulta = cn.prepareStatement(sql)) {
//            System.out.println("Objeto Equipo: " + objeto);
//            System.out.println("idCate: " + objeto.getCategoria().getIdCategoria());
//            System.out.println("idUbica: " + objeto.getUbicacion().getIdUbicacion());
//            
            consulta.setInt(1, objeto.getCategoria().getIdCategoria());
            consulta.setInt(2, objeto.getUbicacion().getIdUbicacion());
            consulta.setString(3, objeto.getCodigo());
            consulta.setString(4, objeto.getNombre());
            consulta.setString(5, objeto.getMarca());
            consulta.setString(6, objeto.getModelo());
            consulta.setString(7, objeto.getStatus());
            consulta.setString(8, objeto.getEstado());

            respuesta = consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al guardar equipo: " + e.getMessage());
        }

        return respuesta;
    }

    /**
     * **********************************************
     * Método para actualizar equipo
     * **********************************************
     */
    public boolean actualizar(Equipo objeto, int idEquipo) {

        boolean respuesta = false;

        String sql = "UPDATE equipo SET "
                + "id_categoria = ?, "
                + "id_ubicacion = ?, "
                + "codigo = ?, "
                + "nombre = ?, "
                + "marca = ?, "
                + "modelo = ?, "
                + "status = ?, "
                + "estado = ? "
                + "WHERE id_equipo = ?";

        try (Connection cn = Conexion.conectar(); PreparedStatement consulta = cn.prepareStatement(sql)) {

            consulta.setInt(1, objeto.getCategoria().getIdCategoria());
            consulta.setInt(2, objeto.getUbicacion().getIdUbicacion());
            consulta.setString(3, objeto.getCodigo());
            consulta.setString(4, objeto.getNombre());
            consulta.setString(5, objeto.getMarca());
            consulta.setString(6, objeto.getModelo());
            consulta.setString(7, objeto.getStatus());
            consulta.setString(8, objeto.getEstado());
            consulta.setInt(9, idEquipo);

            respuesta = consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar equipo: " + e.getMessage());
        }

        return respuesta;
    }

    /**
     * **********************************************
     * Método para eliminar equipo
     * **********************************************
     */
    public boolean eliminar(int idEquipo) {

        boolean respuesta = false;

        String sql = "DELETE FROM equipo WHERE id_equipo = ?";

        try (Connection cn = Conexion.conectar(); PreparedStatement consulta = cn.prepareStatement(sql)) {

            consulta.setInt(1, idEquipo);

            respuesta = consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar equipo: " + e.getMessage());
        }

        return respuesta;
    }

    /**
     * **********************************************
     * Método para listar equipos
     */
    public ArrayList<Equipo> obtenerEquipos() {

        ArrayList<Equipo> listaEquipos = new ArrayList<>();

        String sql
                = "SELECT e.*, "
                + "c.id_categoria, c.nombre AS nombre_categoria, "
                + "u.id_ubicacion, u.nombre AS nombre_ubicacion "
                + "FROM equipo e "
                + "INNER JOIN categoria c ON e.id_categoria = c.id_categoria "
                + "INNER JOIN ubicacion u ON e.id_ubicacion = u.id_ubicacion";

        try (Connection cn = Conexion.conectar(); Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                Equipo equipo = new Equipo();
                Categoria categoria = new Categoria();
                Ubicacion ubicacion = new Ubicacion();

                equipo.setIdEquipo(rs.getInt("id_equipo"));

                categoria.setIdCategoria(rs.getInt("id_categoria"));
                categoria.setNombre(rs.getString("nombre_categoria"));

                ubicacion.setIdUbicacion(rs.getInt("id_ubicacion"));
                ubicacion.setNombre(rs.getString("nombre_ubicacion"));

                equipo.setCategoria(categoria);
                equipo.setUbicacion(ubicacion);

                equipo.setCodigo(rs.getString("codigo"));
                equipo.setNombre(rs.getString("nombre"));
                equipo.setMarca(rs.getString("marca"));
                equipo.setModelo(rs.getString("modelo"));
                equipo.setStatus(rs.getString("status"));
                equipo.setEstado(rs.getString("estado"));

                listaEquipos.add(equipo);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar equipos: " + e.getMessage());
        }

        return listaEquipos;
    }

    /**
     * **************************************************
     * Método que envía los datos seleccionados al modal
     * **************************************************
     */
    public void enviarDatosAModal(int idEquipo) {

        String sql = "SELECT * FROM equipo WHERE id_equipo = ?";

        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql)) {

            pst.setInt(1, idEquipo);

            try (ResultSet rs = pst.executeQuery()) {

                if (rs.next()) {

                    // Crear instancia del diálogo
                    NuevoEquipoDialogForm dialog
                            = new NuevoEquipoDialogForm(
                                    (JFrame) SwingUtilities.getWindowAncestor(
                                            InterEquipo.jTable_equipos), true
                            );

                    // Configurar botones
                    NuevoEquipoDialogForm.jButton_guardar.setEnabled(false);
                    NuevoEquipoDialogForm.jButton_actualizar.setEnabled(true);

                    // Cargar datos en el formulario
                    NuevoEquipoDialogForm.jComboBox_categorias.setSelectedIndex(rs.getInt("id_categoria"));
                    NuevoEquipoDialogForm.jComboBox_ubicaciones.setSelectedIndex(rs.getInt("id_ubicacion"));
                    NuevoEquipoDialogForm.txt_codigo.setText(rs.getString("codigo"));
                    NuevoEquipoDialogForm.txt_nombre.setText(rs.getString("nombre"));
                    NuevoEquipoDialogForm.txt_modelo.setText(rs.getString("modelo"));
                    NuevoEquipoDialogForm.txt_marca.setText(rs.getString("marca"));
                    NuevoEquipoDialogForm.txt_status.setText(rs.getString("status"));
                    NuevoEquipoDialogForm.jComboBox_estado.setSelectedItem(rs.getString("estado"));

                    // Mostrar diálogo
                    dialog.setVisible(true);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al seleccionar equipo: "
                    + e.getMessage());
        }
    }

    /**
     * **************************************************
     * Método para graficar equipos
     * **************************************************
     */
    public Map<String, Integer> obtenerCantidadEquiposPorEstado() {

        Map<String, Integer> datos = new HashMap<>();

        String sql = """
                 SELECT status, COUNT(*) cantidad
                 FROM equipo
                 GROUP BY status
                 """;

        try (
                Connection cn = Conexion.conectar(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                datos.put(
                        rs.getString("status"),
                        rs.getInt("cantidad")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener datos para gráfica: "
                    + e.getMessage());
        }

        return datos;
    }

    /**
     * **************************************************
     * Método para graficar equipos
     * **************************************************
     */
    public Map<String, Integer> obtenerCantidadEquiposPorCategoria() {

        Map<String, Integer> datos = new HashMap<>();

        String sql = """
                 SELECT c.nombre as categoria,
                 COUNT(*) cantidad
                 FROM equipo e
                 INNER JOIN categoria c
                 ON e.id_categoria = c.id_categoria
                  GROUP BY c.nombre;
                 """;

        try (
                Connection cn = Conexion.conectar(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                datos.put(
                        rs.getString("categoria"),
                        rs.getInt("cantidad")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener datos para gráfica: "
                    + e.getMessage());
        }

        return datos;
    }

    /**
     * **********************************************
     * Método para buscar equipos **********************************************
     */
    public ArrayList<Equipo> obtenerEquiposBusqueda(String busqueda) {

        ArrayList<Equipo> listaEquipos = new ArrayList<>();

        String sql
                = "SELECT e.*, "
                + "c.id_categoria, c.nombre AS nombre_categoria, "
                + "u.id_ubicacion, u.nombre AS nombre_ubicacion "
                + "FROM equipo e "
                + "INNER JOIN categoria c ON e.id_categoria = c.id_categoria "
                + "INNER JOIN ubicacion u ON e.id_ubicacion = u.id_ubicacion "
                + "WHERE e.nombre LIKE ? "
                + "OR e.codigo LIKE ? "
                + "OR e.marca LIKE ? "
                + "ORDER BY e.id_equipo";

        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql)) {

            String criterio = "%" + busqueda + "%";

            pst.setString(1, criterio);
            pst.setString(2, criterio);
            pst.setString(3, criterio);

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {

                    Equipo equipo = new Equipo();
                    Categoria categoria = new Categoria();
                    Ubicacion ubicacion = new Ubicacion();

                    equipo.setIdEquipo(
                            rs.getInt("id_equipo"));

                    categoria.setIdCategoria(
                            rs.getInt("id_categoria"));

                    categoria.setNombre(
                            rs.getString("nombre_categoria"));

                    ubicacion.setIdUbicacion(
                            rs.getInt("id_ubicacion"));

                    ubicacion.setNombre(
                            rs.getString("nombre_ubicacion"));

                    equipo.setCategoria(categoria);
                    equipo.setUbicacion(ubicacion);

                    equipo.setCodigo(
                            rs.getString("codigo"));

                    equipo.setNombre(
                            rs.getString("nombre"));

                    equipo.setMarca(
                            rs.getString("marca"));

                    equipo.setModelo(
                            rs.getString("modelo"));

                    equipo.setStatus(
                            rs.getString("status"));

                    equipo.setEstado(
                            rs.getString("estado"));

                    listaEquipos.add(equipo);
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al buscar equipos: "
                    + e.getMessage());
        }

        return listaEquipos;
    }
}
