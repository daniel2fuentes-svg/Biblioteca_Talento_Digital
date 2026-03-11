package cl.td.dao;

import cl.td.config.DatabaseConnection;
import cl.td.dto.Usuario; // Cambiado de User a Usuario para ser consistentes
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private Connection connection;

    public UsuarioDAO() {
        this.connection = DatabaseConnection.createConnection();
    }

    // 1. CREATE: Ahora incluimos el ROL al crear un usuario
    public boolean insert(Usuario usuario) {
        String sql = "INSERT INTO usuario (email, nombre, password, rol, activo) VALUES (?, ?, ?, ?, true)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuario.getEmail());
            statement.setString(2, usuario.getNombre());
            statement.setString(3, usuario.getPassword());
            statement.setString(4, usuario.getRol()); // "ADMIN", "USUARIO" o "CONSULTOR"
            statement.setBoolean(5, usuario.isActivo());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. READ: Solo obtenemos los usuarios que están ACTIBOS
    public List<Usuario> findAll() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario WHERE activo = 1";
        try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Usuario u = new Usuario();
                u.setId(resultSet.getInt("id"));
                u.setEmail(resultSet.getString("email"));
                u.setNombre(resultSet.getString("nombre"));
                u.setRol(resultSet.getString("rol")); // Importante traer el rol
                u.setActivo(resultSet.getBoolean("activo")); // Aseguramos el estado
                usuarios.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    // 3. UPDATE: Permitimos cambiar también el ROL
    public boolean update(Usuario usuario) {
        String sql = "UPDATE usuario SET email=?, nombre=?, password=?, rol=?, activo=1 WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuario.getEmail());
            statement.setString(2, usuario.getNombre());
            statement.setString(3, usuario.getPassword());
            statement.setString(4, usuario.getRol());
            statement.setInt(5, usuario.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. DELETE LÓGICO: No borramos de la BD, solo "apagamos" al usuario
    public boolean deleteLogico(int id) {
        String sql = "UPDATE usuario SET activo = false WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. LOGIN: El método que usará tu pantalla de entrada
    public Usuario validarLogin(String email, String password) {
        String sql = "SELECT * FROM usuario WHERE email = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, password);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id"));
                    u.setEmail(rs.getString("email"));
                    u.setNombre(rs.getString("nombre"));
                    u.setRol(rs.getString("rol")); // Esto es lo que define qué verá en el menú
                    return u;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Usuario findByEmail(String email) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuario WHERE email = ?";

        try (Connection conn = DatabaseConnection.createConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setEmail(rs.getString("email"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setPassword(rs.getString("password"));
                usuario.setRol(rs.getString("rol"));
                usuario.setActivo(rs.getBoolean("activo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    public Usuario findByIdUsuario(int id) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuario WHERE id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setPassword(rs.getString("password"));
                    usuario.setRol(rs.getString("rol"));
                    usuario.setActivo(rs.getBoolean("activo"));
                    System.out.println("DAO: Usuario encontrado -> " + usuario.getNombre());
                } else {
                    System.out.println("DAO: No se encontró el ID de Usuario-> " + id);
                }
            } // Cierra ResultSet
        } catch (SQLException e) {
            System.err.println("Error en findById: " + e.getMessage());
            e.printStackTrace();
        } // Cierra Connection y PreparedStatement

        return usuario;
    }

    public List<Usuario> findByNombreOEmail(String texto) {
        List<Usuario> lista = new ArrayList<>();
        // Usamos LIKE con % para que busque "que contenga" el texto
        String sql = "SELECT * FROM usuario WHERE (nombre LIKE ? OR email LIKE ?)";

        try (Connection conn = DatabaseConnection.createConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            // El % permite buscar coincidencias parciales (ej: "juan" encuentra "Juan
            // Pérez")
            String parametro = "%" + texto + "%";
            ps.setString(1, parametro);
            ps.setString(2, parametro);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id"));
                    u.setNombre(rs.getString("nombre"));
                    u.setEmail(rs.getString("email"));
                    u.setRol(rs.getString("rol"));
                    u.setActivo(rs.getBoolean("activo"));
                    lista.add(u);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

}
