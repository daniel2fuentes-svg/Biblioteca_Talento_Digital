package cl.td.dao;

import cl.td.config.DatabaseConnection;
import cl.td.dto.Prestamo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {

    private LibroDAO libroDAO = new LibroDAO();

    // CREATE: Insertar un nuevo préstamo
    public boolean insert(Prestamo p) {

        String sql = "INSERT INTO prestamo (user_id, libro_id, fecha_prestamo, fecha_devolucion, activo, cliente_nombre, cliente_telefono, cliente_email, observaciones) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.createConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, p.getUserId());
            ps.setString(2, p.getLibroId());
            ps.setDate(3, new java.sql.Date(p.getFechaPrestamo().getTime()));
            ps.setDate(4, new java.sql.Date(p.getFechaDevolucion().getTime()));
            ps.setBoolean(5, p.isActivo());
            ps.setString(6, p.getClienteNombre());
            ps.setString(7, p.getClienteTelefono());
            ps.setString(8, p.getClienteEmail());
            ps.setString(9, p.getObservaciones());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Este método es el "traductor" para el Servlet
    public List<Prestamo> findAll() {
        return findAll(null, null); // Llama al otro método pasándole vacíos
    }

    // READ: Obtener todos los préstamos (Ajustado a tus columnas)
    public List<Prestamo> findAll(String filtro, String busqueda) {
        List<Prestamo> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT p.*, l.titulo AS libro_titulo "
                        + "FROM prestamo p "
                        + "JOIN libro l ON p.libro_id = l.isbn"); // Asegúrate que 'isbn' sea la PK en tu tabla libro);

        boolean tieneFiltro = filtro != null && !filtro.isEmpty() && busqueda != null && !busqueda.isEmpty();
        if (tieneFiltro) {
            sql.append(" WHERE ").append(filtro).append(" LIKE ?");
        }

        try (Connection conn = DatabaseConnection.createConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            if (tieneFiltro) {
                ps.setString(1, "%" + busqueda + "%");
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Prestamo p = mapResultSetToPrestamo(rs);
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // DELETE LÓGICO (El que te faltaba en la imagen)
    public boolean deleteLogico(int id) {
        String sql = "UPDATE prestamo SET activo = false, fecha_devolucion = CURDATE() WHERE id = ?";
        try (Connection conn = DatabaseConnection.createConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Prestamo findById(int id) {
        String sql = "SELECT * FROM prestamo WHERE id = ?";
        try (Connection conn = DatabaseConnection.createConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return mapResultSetToPrestamo(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Helper para no repetir código de mapeo
    private Prestamo mapResultSetToPrestamo(ResultSet rs) throws SQLException {
        Prestamo p = new Prestamo();
        p.setId(rs.getInt("id"));
        p.setUserId(rs.getInt("user_id"));
        p.setLibroId(rs.getString("libro_id"));
        p.setFechaPrestamo(rs.getDate("fecha_prestamo"));
        p.setFechaDevolucion(rs.getDate("fecha_devolucion"));
        p.setActivo(rs.getBoolean("activo"));
        p.setClienteNombre(rs.getString("cliente_nombre"));
        p.setClienteTelefono(rs.getString("cliente_telefono")); // Faltaba en tu findAll
        p.setClienteEmail(rs.getString("cliente_email")); // Faltaba en tu findAll
        p.setObservaciones(rs.getString("observaciones"));
        return p;
    }

    // UPDATE: actualizar un préstamo (ej. para marcarlo como devuelto o cambiar la
    // fecha de devolución)
    public boolean update(Prestamo p) {
        String sql = "UPDATE prestamo SET user_id = ?, libro_id = ?, fecha_prestamo = ?, fecha_devolucion = ?, activo = ?, cliente_nombre = ?, cliente_telefono = ?, cliente_email = ?, observaciones = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.createConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, p.getUserId());
            ps.setString(2, p.getLibroId());
            ps.setDate(3, new java.sql.Date(p.getFechaPrestamo().getTime()));
            // Manejo de nulo para fecha de devolución
            if (p.getFechaDevolucion() != null) {
                ps.setDate(4, new java.sql.Date(p.getFechaDevolucion().getTime()));
            } else {
                ps.setNull(4, Types.DATE);
            }
            ps.setBoolean(5, p.isActivo());
            ps.setString(6, p.getClienteNombre());
            ps.setString(7, p.getClienteTelefono());
            ps.setString(8, p.getClienteEmail());
            ps.setString(9, p.getObservaciones());
            ps.setInt(10, p.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método específico para marcar un préstamo como devuelto (cambia 'activo' a
    // false)
    public boolean devolverLibro(int idPrestamo, String idLibro) {
        // ACCIÓN 1: Actualizar el préstamo
        Prestamo p = findById(idPrestamo);
        p.setActivo(false);
        p.setFechaDevolucion(new java.util.Date()); // Fecha de devolución es hoy
        boolean prestamoActualizado = update(p);
        // ACCIÓN 2: Liberar el libro (Aquí el "cerebro" conecta los módulos)
        if (prestamoActualizado) {
            return libroDAO.actualizarDisponibilidadPorIsbn(idLibro, true); // idLibro es el ISBN, true para disponible
        }
        return false;
    }

    // Buscar si hay un préstamo activo para un libro específico (para validar antes
    // de insertar)
    public Prestamo findActivePrestamoByLibroId(String libroId) {
        Prestamo p = null;
        String sql = "SELECT * FROM prestamo WHERE libro_id = ? AND activo = true";
        try (Connection conn = DatabaseConnection.createConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, libroId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = new Prestamo();
                    p.setId(rs.getInt("id"));
                    p.setUserId(rs.getInt("user_id"));
                    p.setLibroId(rs.getString("libro_id"));
                    p.setFechaPrestamo(rs.getDate("fecha_prestamo"));
                    p.setFechaDevolucion(rs.getDate("fecha_devolucion"));
                    p.setActivo(rs.getBoolean("activo"));
                    p.setClienteNombre(rs.getString("cliente_nombre"));
                    p.setObservaciones(rs.getString("observaciones"));

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return p;
    }

}