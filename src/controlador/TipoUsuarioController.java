package controlador;

import conexion.Conexion;
import modelo.TipoUsuario;

import java.sql.*;
import java.util.ArrayList;

public class TipoUsuarioController {

    /**
     * **********************************************
     * Método para guardar un nuevo tipo de usuario
     * **********************************************
     */
    public boolean guardar(TipoUsuario objeto) {

        boolean respuesta = false;

        String sql = "INSERT INTO tipo_usuario "
                   + "(id_tipo_usuario, descripcion, estado) "
                   + "VALUES (?, ?, ?)";

        try (Connection cn = Conexion.conectar();
             PreparedStatement consulta = cn.prepareStatement(sql)) {

            consulta.setInt(1, objeto.getIdTipoUsuario());
            consulta.setString(2, objeto.getDescripcion());
            consulta.setString(3, objeto.getEstado());

            respuesta = consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al guardar tipo de usuario: " + e.getMessage());
        }

        return respuesta;
    }

    /**
     * **********************************************
     * Método para actualizar tipo de usuario
     * **********************************************
     */
    public boolean actualizar(TipoUsuario objeto, int idTipoUsuario) {

        boolean respuesta = false;

        String sql = "UPDATE tipo_usuario "
                   + "SET descripcion = ?, estado = ? "
                   + "WHERE id_tipo_usuario = ?";

        try (Connection cn = Conexion.conectar();
             PreparedStatement consulta = cn.prepareStatement(sql)) {

            consulta.setString(1, objeto.getDescripcion());
            consulta.setString(2, objeto.getEstado());
            consulta.setInt(3, idTipoUsuario);

            respuesta = consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar tipo de usuario: " + e.getMessage());
        }

        return respuesta;
    }

    /**
     * **********************************************
     * Método para eliminar tipo de usuario
     * **********************************************
     */
    public boolean eliminar(int idTipoUsuario) {

        boolean respuesta = false;

        String sql = "DELETE FROM tipo_usuario WHERE id_tipo_usuario = ?";

        try (Connection cn = Conexion.conectar();
             PreparedStatement consulta = cn.prepareStatement(sql)) {

            consulta.setInt(1, idTipoUsuario);

            respuesta = consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar tipo de usuario: " + e.getMessage());
        }

        return respuesta;
    }

    /**
     * **********************************************
     * Método para obtener todos los tipos de usuario
     * **********************************************
     */
    public ArrayList<TipoUsuario> obtenerTipoUsuarios() {

        ArrayList<TipoUsuario> listaTipoUsuarios = new ArrayList<>();

        String sql = "SELECT id_tipo_usuario, descripcion, estado "
                   + "FROM tipo_usuario";

        try (Connection cn = Conexion.conectar();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                TipoUsuario tipoUsuario = new TipoUsuario(
                        rs.getInt("id_tipo_usuario"),
                        rs.getString("descripcion"),
                        rs.getString("estado")
                );

                listaTipoUsuarios.add(tipoUsuario);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar tipos de usuario: " + e.getMessage());
        }

        return listaTipoUsuarios;
    }
}