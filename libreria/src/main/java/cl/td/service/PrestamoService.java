package cl.td.service;

import cl.td.dao.LibroDAO;
import cl.td.dao.PrestamoDAO;
import cl.td.dto.Prestamo;
import java.util.List;

public class PrestamoService {
    private PrestamoDAO prestamoDAO = new PrestamoDAO();
    private LibroDAO libroDao = new LibroDAO();

    // Lógica para listar con filtros
    public List<Prestamo> listarPrestamos(String filtro, String busqueda) {
        return prestamoDAO.findAll(filtro, busqueda);
    }

    // Lógica para guardar (aquí puedes validar antes de insertar)
    public boolean guardarPrestamo(Prestamo prestamo) {
        // Regla: 1 registro 1 libro, no consideramos stock.
        // Regla de negocio: No se puede prestar un libro que ya está prestado
        if (prestamoDAO.findActivePrestamoByLibroId(prestamo.getLibroId()) != null) {
            return false;
        }
        if (prestamoDAO.insert(prestamo)) {
            return libroDao.actualizarDisponibilidadPorIsbn(prestamo.getLibroId(), false);
        }
        return false;
    }

    // Lógica para actualizar
    public boolean actualizarPrestamo(Prestamo prestamo) {
        return prestamoDAO.update(prestamo);
    }

    // Lógica para cerrar un préstamo (equivalente a devolver un libro)
    public boolean finalizarPrestamo(int idPrestamo, String idLibro) {
        Prestamo p = prestamoDAO.findById(idPrestamo);
        if (p == null) {
            return false; // No se encontró el préstamo
        }

        p.setActivo(false);
        // Convertimos java.util.Date a java.sql.Date para que sea compatible con la BD
        p.setFechaDevolucion(new java.sql.Date(System.currentTimeMillis()));

        if (prestamoDAO.update(p))

        {
            return libroDao.actualizarDisponibilidadPorIsbn(idLibro, true); // Liberar el libro
        }
        return false;
    }

    // Lógica para borrado lógico
    public boolean eliminarPrestamo(int id) {
        return prestamoDAO.deleteLogico(id);
    }

    // Buscar uno solo por ID
    public Prestamo obtenerPorId(int id) {
        return prestamoDAO.findById(id);
    }
}
