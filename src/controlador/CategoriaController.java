package controlador;

import conexion.Conexion;
import modelo.Categoria;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import vista.InterCategoria;
import vista.NuevaCategoriaDialogForm;

public class CategoriaController {

    /**
     * **********************************************
     * Método para guardar una nueva categoría
     * **********************************************
     */
    public boolean guardar(Categoria objeto) {
        boolean respuesta = false;

        String sql = "INSERT INTO categoria(nombre, descripcion, estado) VALUES (?, ?, ?)";

        try (Connection cn = Conexion.conectar(); PreparedStatement consulta = cn.prepareStatement(sql)) {

            consulta.setString(1, objeto.getNombre());
            consulta.setString(2, objeto.getDescripcion());
            consulta.setString(3, objeto.getEstado());

            respuesta = consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al guardar categoría: " + e.getMessage());
        }

        return respuesta;
    }

    /**
     * **********************************************
     * Método para actualizar categoría
     * **********************************************
     * @param objeto
     * @param idCategoria
     * @return 
     */
    public boolean actualizar(Categoria objeto, int idCategoria) {
        boolean respuesta = false;

        String sql = "UPDATE categoria "
                + "SET nombre = ?, descripcion = ?, estado = ? "
                + "WHERE id_categoria = ?";

        try (Connection cn = Conexion.conectar(); PreparedStatement consulta = cn.prepareStatement(sql)) {

            consulta.setString(1, objeto.getNombre());
            consulta.setString(2, objeto.getDescripcion());
            consulta.setString(3, objeto.getEstado());
            consulta.setInt(4, idCategoria);

            respuesta = consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar categoría: " + e.getMessage());
        }

        return respuesta;
    }

    /**
     * **********************************************
     * Método para eliminar categoría
     * **********************************************
     */
    public boolean eliminar(int idCategoria) {
        boolean respuesta = false;

        String sql = "DELETE FROM categoria WHERE id_categoria = ?";

        try (Connection cn = Conexion.conectar(); PreparedStatement consulta = cn.prepareStatement(sql)) {

            consulta.setInt(1, idCategoria);

            respuesta = consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar categoría: " + e.getMessage());
        }

        return respuesta;
    }

    /**
     * **********************************************
     * Método para obtener todas las categorías
     * **********************************************
     */
    public ArrayList<Categoria> obtenerCategorias() {

        ArrayList<Categoria> listaCategorias = new ArrayList<>();

        String sql = "SELECT id_categoria, nombre, descripcion, estado FROM categoria";

        try (Connection cn = Conexion.conectar(); Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                Categoria categoria = new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("estado")
                );

                listaCategorias.add(categoria);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar categorías: " + e.getMessage());
        }

        return listaCategorias;
    }

    /**
     * **************************************************
     * Método que envía los datos seleccionados al modal
     * **************************************************
     * @param idCategoria
     */
    public void enviarDatosAModal(int idCategoria) {

        String sql = "SELECT * FROM categoria WHERE id_categoria = ?";

        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql)) {

            pst.setInt(1, idCategoria);

            try (ResultSet rs = pst.executeQuery()) {

                if (rs.next()) {

                    // Crear instancia del diálogo
                    NuevaCategoriaDialogForm dialog
                            = new NuevaCategoriaDialogForm(
                                    (JFrame) SwingUtilities.getWindowAncestor(
                                            InterCategoria.jTable_categorias),
                                    true);

                    // Configurar botones
                    NuevaCategoriaDialogForm.jButton_guardar.setEnabled(false);
                    NuevaCategoriaDialogForm.jButton_actualizar.setEnabled(true);

                    // Cargar datos en el formulario
                    NuevaCategoriaDialogForm.txt_nombre.setText(rs.getString("nombre"));
                    NuevaCategoriaDialogForm.txt_descripcion.setText(rs.getString("descripcion"));
                    NuevaCategoriaDialogForm.jComboBox_estado.setSelectedItem(rs.getString("estado"));

                    // Mostrar diálogo
                    dialog.setVisible(true);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al seleccionar categoría: "
                    + e.getMessage());
        }
    }
}
