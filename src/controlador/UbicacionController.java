package controlador;

import conexion.Conexion;
import modelo.Ubicacion;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import vista.InterUbicacion;
import vista.NuevaUbicacionDialogForm;

public class UbicacionController {

    /**
     * **********************************************
     * Método para guardar una nueva ubicación
     * **********************************************
     */
    public boolean guardar(Ubicacion objeto) {

        boolean respuesta = false;

        String sql = "INSERT INTO ubicacion(nombre, descripcion, estado) "
                + "VALUES (?, ?, ?)";

        try (Connection cn = Conexion.conectar(); PreparedStatement consulta = cn.prepareStatement(sql)) {

            consulta.setString(1, objeto.getNombre());
            consulta.setString(2, objeto.getDescripcion());
            consulta.setString(3, objeto.getEstado());

            respuesta = consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al guardar ubicación: " + e.getMessage());
        }

        return respuesta;
    }

    /**
     * **********************************************
     * Método para actualizar ubicación
     * **********************************************
     */
    public boolean actualizar(Ubicacion objeto, int idUbicacion) {

        boolean respuesta = false;

        String sql = "UPDATE ubicacion "
                + "SET nombre = ?, descripcion = ?, estado = ? "
                + "WHERE id_ubicacion = ?";

        try (Connection cn = Conexion.conectar(); PreparedStatement consulta = cn.prepareStatement(sql)) {

            consulta.setString(1, objeto.getNombre());
            consulta.setString(2, objeto.getDescripcion());
            consulta.setString(3, objeto.getEstado());
            consulta.setInt(4, idUbicacion);

            respuesta = consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar ubicación: " + e.getMessage());
        }

        return respuesta;
    }

    /**
     * **********************************************
     * Método para eliminar ubicación
     * **********************************************
     */
    public boolean eliminar(int idUbicacion) {

        boolean respuesta = false;

        String sql = "DELETE FROM ubicacion WHERE id_ubicacion = ?";

        try (Connection cn = Conexion.conectar(); PreparedStatement consulta = cn.prepareStatement(sql)) {

            consulta.setInt(1, idUbicacion);

            respuesta = consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar ubicación: " + e.getMessage());
        }

        return respuesta;
    }

    /**
     * **********************************************
     * Método para obtener todas las ubicaciones
     * **********************************************
     */
    public ArrayList<Ubicacion> obtenerUbicaciones() {

        ArrayList<Ubicacion> listaUbicaciones = new ArrayList<>();

        String sql = "SELECT id_ubicacion, nombre, descripcion, estado "
                + "FROM ubicacion";

        try (Connection cn = Conexion.conectar(); Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                Ubicacion ubicacion = new Ubicacion(
                        rs.getInt("id_ubicacion"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("estado")
                );

                listaUbicaciones.add(ubicacion);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar ubicaciones: " + e.getMessage());
        }

        return listaUbicaciones;
    }

    /**
     * **************************************************
     * Método que envía los datos seleccionados al modal
     * **************************************************
     */
    public void enviarDatosAModal(int idUbicacion) {

        String sql = "SELECT * FROM ubicacion WHERE id_ubicacion = ?";

        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql)) {

            pst.setInt(1, idUbicacion);

            try (ResultSet rs = pst.executeQuery()) {

                if (rs.next()) {

                    // Crear instancia del diálogo
                    NuevaUbicacionDialogForm dialog
                            = new NuevaUbicacionDialogForm(
                                    (JFrame) SwingUtilities.getWindowAncestor(
                                            InterUbicacion.jTable_ubicaciones),
                                    true);

                    // Configurar botones
                    NuevaUbicacionDialogForm.jButton_guardar.setEnabled(false);
                    NuevaUbicacionDialogForm.jButton_actualizar.setEnabled(true);

                    // Cargar datos en el formulario
                    NuevaUbicacionDialogForm.txt_nombre.setText(rs.getString("nombre"));
                    NuevaUbicacionDialogForm.txt_descripcion.setText(rs.getString("descripcion"));
                    NuevaUbicacionDialogForm.jComboBox_estado.setSelectedItem(rs.getString("estado"));

                    // Mostrar diálogo
                    dialog.setVisible(true);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al seleccionar ubicacion: "
                    + e.getMessage());
        }
    }
}
