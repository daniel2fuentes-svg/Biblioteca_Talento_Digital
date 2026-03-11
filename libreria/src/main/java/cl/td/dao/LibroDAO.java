package cl.td.dao;

import cl.td.config.DatabaseConnection;
import cl.td.dto.Libro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    // CONSTRUCTOR VACIO PARA INICIALIZAR LA CONEXIÓN
    public LibroDAO() {
    }

    // CREATE: Insertar un nuevo libro
    public boolean insert(Libro libro) {
        String sql = "INSERT INTO libro (titulo, autor, isbn, disponible, activo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.createConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, libro.getTitulo());
            statement.setString(2, libro.getAutor());
            statement.setString(3, libro.getIsbn());
            statement.setBoolean(4, libro.isDisponible());
            statement.setBoolean(5, libro.isActivo());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ: Obtener todos los libros
    // 1. findAll Básico (Sin parámetros): Trae todos lo que hay en la tabla
    public List<Libro> findAll() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libro";
        try (Connection conn = DatabaseConnection.createConnection();
                PreparedStatement statement = conn.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Libro libro = new Libro();
                libro.setId(resultSet.getInt("id"));
                libro.setTitulo(resultSet.getString("titulo"));
                libro.setAutor(resultSet.getString("autor"));
                libro.setIsbn(resultSet.getString("isbn"));
                libro.setDisponible(resultSet.getBoolean("disponible"));
                libro.setActivo(resultSet.getBoolean("activo")); // Aseguramos el estado
                libros.add(libro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return libros;
    }

    // UPDATE: Modificar un libro existente
    public boolean update(Libro libro) {
        String sql = "UPDATE libro SET titulo = ?, autor = ?, isbn = ?, disponible = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.createConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, libro.getTitulo());
            statement.setString(2, libro.getAutor());
            statement.setString(3, libro.getIsbn());
            statement.setBoolean(4, libro.isDisponible());
            statement.setInt(5, libro.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE pero cambiado a UPDATE para Borrado Lógico (No DELETE)
    public boolean deleteLogico(int id) {
        String sql = "UPDATE libro SET activo = false WHERE id = ?";
        try (Connection conn = DatabaseConnection.createConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Libro findByIsbn(String isbn) {
        Libro libro = null;
        String sql = "SELECT * FROM libro WHERE isbn = ?";

        try (Connection conn = DatabaseConnection.createConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                libro = new Libro();
                libro.setId(rs.getInt("id"));
                libro.setIsbn(rs.getString("isbn"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setAutor(rs.getString("autor"));
                libro.setDisponible(rs.getBoolean("disponible"));
                libro.setActivo(rs.getBoolean("activo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return libro;
    }

    // 2. FIND ALL con Filtro de Estado y Ordenamiento
    // Modificamos el findAll para aceptar un filtro (todos, disponibles, prestados)
    public List<Libro> findAll(String filtro, String busqueda) {
        List<Libro> libros = new ArrayList<>();
        // Base: Solo los que no han sido borrados lógicamente (activo = true)
        String sql = "SELECT * FROM libro WHERE activo = true";
        if (filtro != null) {
            if (filtro.equals("disponibles"))
                sql += " AND disponible = true";
            else if (filtro.equals("prestados"))
                sql += " AND disponible = false";
        }

        if (busqueda != null && !busqueda.isEmpty()) {
            sql += " AND (titulo LIKE ? OR autor LIKE ? OR isbn LIKE ?)";
        }

        // Orden por defecto: Disponibles primero y luego por ID descendente (última
        // creación)
        sql += " ORDER BY disponible DESC, id DESC";

        try (Connection conn = DatabaseConnection.createConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            if (busqueda != null && !busqueda.isEmpty()) {
                statement.setString(1, "%" + busqueda + "%");
                statement.setString(2, "%" + busqueda + "%");
                statement.setString(3, "%" + busqueda + "%");
            }
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Libro libro = new Libro();
                libro.setId(resultSet.getInt("id"));
                libro.setTitulo(resultSet.getString("titulo"));
                libro.setAutor(resultSet.getString("autor"));
                libro.setIsbn(resultSet.getString("isbn"));
                libro.setDisponible(resultSet.getBoolean("disponible"));
                libro.setActivo(resultSet.getBoolean("activo"));
                libros.add(libro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return libros;
    }

    public Libro findById(int id) {
        Libro libro = null;
        String sql = "SELECT * FROM libro WHERE id = ?";
        try (Connection conn = DatabaseConnection.createConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    libro = new Libro();
                    libro.setId(rs.getInt("id")); // Fila 1: id
                    libro.setTitulo(rs.getString("titulo")); // Fila 2: titulo
                    libro.setAutor(rs.getString("autor")); // Fila 3: autor
                    libro.setIsbn(rs.getString("isbn")); // Fila 4: isbn (Ojo aquí)
                    libro.setDisponible(rs.getBoolean("disponible")); // Fila 5: disponible
                    libro.setActivo(rs.getBoolean("activo")); // Fila 6: activo
                    System.out.println("DAO: Libro encontrado -> " + libro.getTitulo());
                } else {
                    System.out.println("DAO: No se encontró el ID -> " + id);
                }
            } // Cierra ResultSet
        } catch (SQLException e) {
            System.err.println("Error en findById: " + e.getMessage());
            e.printStackTrace();
        } // Cierra Connection y PreparedStatement

        return libro;
    }

    // Nuevo método para cambiar el estado de disponibilidad
    // (Préstamos/Devoluciones)
    public boolean actualizarDisponibilidadPorIsbn(String isbn, boolean estado) {
        String sql = "UPDATE libro SET disponible = ? WHERE isbn = ?";
        try (Connection conn = DatabaseConnection.createConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setBoolean(1, estado);
            statement.setString(2, isbn);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
