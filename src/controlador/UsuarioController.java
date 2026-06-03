package controlador;

import conexion.Conexion;
import modelo.Usuario;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import modelo.TipoUsuario;
import vista.InterUsuario;
import vista.NuevoUsuarioDialogForm;

public class UsuarioController {

    /**
     * **********************************************
     * Método para guardar usuario
     * **********************************************
     */
    public boolean guardar(Usuario objeto, TipoUsuario tipo) {

        boolean respuesta = false;

        String sql = "INSERT INTO usuario "
                + "(id_tipo_usuario, nombres, apellidos, correo, clave, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection cn = Conexion.conectar(); PreparedStatement consulta = cn.prepareStatement(sql)) {

            consulta.setInt(1, tipo.getIdTipoUsuario());
            consulta.setString(2, objeto.getNombres());
            consulta.setString(3, objeto.getApellidos());
            consulta.setString(4, objeto.getCorreo());
            consulta.setString(5, objeto.getClave());
            consulta.setString(6, objeto.getEstado());

            respuesta = consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al guardar usuario: " + e.getMessage());
        }

        return respuesta;
    }

    /**
     * **********************************************
     * Método para actualizar usuario
     * **********************************************
     */
    public boolean actualizar(Usuario objeto, TipoUsuario tipo, int idUsuario) {

        boolean respuesta = false;

        String sql = "UPDATE usuario SET "
                + "id_tipo_usuario = ?, "
                + "nombres = ?, "
                + "apellidos = ?, "
                + "correo = ?, "
                + "clave = ?, "
                + "estado = ? "
                + "WHERE id_usuario = ?";

        try (Connection cn = Conexion.conectar(); PreparedStatement consulta = cn.prepareStatement(sql)) {

            consulta.setInt(1, tipo.getIdTipoUsuario());
//            consulta.setInt(1, objeto.getIdTipoUsuario());
            consulta.setString(2, objeto.getNombres());
            consulta.setString(3, objeto.getApellidos());
            consulta.setString(4, objeto.getCorreo());
            consulta.setString(5, objeto.getClave());
            consulta.setString(6, objeto.getEstado());
            consulta.setInt(7, idUsuario);

            respuesta = consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
        }

        return respuesta;
    }

    /**
     * **********************************************
     * Método para eliminar usuario
     * **********************************************
     */
    public boolean eliminar(int idUsuario) {

        boolean respuesta = false;

        String sql = "DELETE FROM usuario WHERE id_usuario = ?";

        try (Connection cn = Conexion.conectar(); PreparedStatement consulta = cn.prepareStatement(sql)) {

            consulta.setInt(1, idUsuario);

            respuesta = consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());
        }

        return respuesta;
    }

    /**
     * **********************************************
     * Método para listar usuarios
     * **********************************************
     */
    public ArrayList<Usuario> obtenerUsuarios() {

        ArrayList<Usuario> listaUsuarios = new ArrayList<>();

        String sql = "SELECT \n"
                + "    u.id_usuario,\n"
                + "    t.descripcion AS tipo_usuario,\n"
                + "    u.nombres,\n"
                + "    u.apellidos,\n"
                + "    u.correo,\n"
                + "    u.clave,\n"
                + "    u.estado\n"
                + "FROM usuario u\n"
                + "INNER JOIN tipo_usuario t \n"
                + "ON u.id_tipo_usuario = t.id_tipo_usuario;";

        try (Connection cn = Conexion.conectar(); Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                Usuario usuario = new Usuario();
                TipoUsuario tipoUsuario = new TipoUsuario();

                usuario.setIdUsuario(rs.getInt("id_usuario"));

                tipoUsuario.setDescripcion(rs.getString("tipo_usuario"));
                usuario.setTipo_usuario(tipoUsuario);

                usuario.setNombres(rs.getString("nombres"));
                usuario.setApellidos(rs.getString("apellidos"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setClave(rs.getString("clave"));
                usuario.setEstado(rs.getString("estado"));

                listaUsuarios.add(usuario);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }

        return listaUsuarios;
    }

    /**
     * **************************************************
     * Método que envía los datos seleccionados al modal
     * **************************************************
     */
    public void enviarDatosAModal(int idUsuario) {

        String sql = "SELECT * FROM usuario WHERE id_usuario = ?";

        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql)) {

            pst.setInt(1, idUsuario);

            try (ResultSet rs = pst.executeQuery()) {

                if (rs.next()) {

                    // Guardar datos para validaciones posteriores
                    //correo = rs.getString("correo");
                    // Crear instancia del diálogo
                    NuevoUsuarioDialogForm dialog
                            = new NuevoUsuarioDialogForm(
                                    (JFrame) SwingUtilities.getWindowAncestor(
                                            InterUsuario.jTable_usuarios), true
                            );

                    // Configurar botones
                    NuevoUsuarioDialogForm.jButton_guardar.setEnabled(false);
                    NuevoUsuarioDialogForm.jButton_actualizar.setEnabled(true);

                    // Cargar datos en el formulario
                    NuevoUsuarioDialogForm.txt_nombres.setText(rs.getString("nombres"));
                    NuevoUsuarioDialogForm.txt_apellidos.setText(rs.getString("apellidos"));
                    NuevoUsuarioDialogForm.txt_correo.setText(rs.getString("correo"));
                    NuevoUsuarioDialogForm.txt_clave.setText(rs.getString("clave"));
                    NuevoUsuarioDialogForm.jComboBox_tipo_usuario.setSelectedIndex(rs.getInt("id_tipo_usuario"));
                    NuevoUsuarioDialogForm.jComboBox_estado.setSelectedItem(rs.getString("estado"));

                    // Mostrar diálogo
                    dialog.setVisible(true);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al seleccionar usuario: "
                    + e.getMessage());
        }
    }

    /**
     * **************************************************
     * Método para hacer login
     * **************************************************
     */
    public Usuario login(String correo, String clave) {

        Usuario usuario = null;
        TipoUsuario tipoUsuario = new TipoUsuario();

        String sql = """
        SELECT *
        FROM usuario
        WHERE correo = ?
        AND clave = ?
        AND estado = 'ACTIVO'
    """;

        try (
                Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql)) {

            pst.setString(1, correo);
            pst.setString(2, clave);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                usuario = new Usuario();

                usuario.setIdUsuario(
                        rs.getInt("id_usuario"));

                tipoUsuario.setIdTipoUsuario(rs.getInt("id_tipo_usuario"));
                usuario.setTipo_usuario(tipoUsuario);

                usuario.setNombres(
                        rs.getString("nombres"));

                usuario.setApellidos(
                        rs.getString("apellidos"));

                usuario.setCorreo(
                        rs.getString("correo"));

                usuario.setClave(
                        rs.getString("clave"));

                usuario.setEstado(
                        rs.getString("estado"));
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error en login: "
                    + e.getMessage());
        }

        return usuario;
    }

}
