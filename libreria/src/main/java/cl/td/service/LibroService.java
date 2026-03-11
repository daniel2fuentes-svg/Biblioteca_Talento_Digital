package cl.td.service;

import cl.td.dao.LibroDAO;
import cl.td.dto.Libro;
import java.util.List;

public class LibroService {
    private LibroDAO libroDAO;

    public LibroService() {
        this.libroDAO = new LibroDAO();
    }

    // Lógica para listar con filtros
    public List<Libro> listarLibros(String filtro, String busqueda) {
        return libroDAO.findAll(filtro, busqueda);
    }

    // Lógica para guardar (aquí puedes validar antes de insertar)
    public boolean guardarLibro(Libro libro) {
        // Ejemplo de regla de negocio: El ISBN debe ser único
        if (libroDAO.findByIsbn(libro.getIsbn()) != null) {
            System.out.println("Error: El ISBN ya existe.");
            return false;
        }
        return libroDAO.insert(libro);
    }

    // Lógica para actualizar
    public boolean actualizarLibro(Libro libro) {
        return libroDAO.update(libro);
    }

    // Lógica para borrado lógico
    public boolean eliminarLibro(int id) {
        return libroDAO.deleteLogico(id);
    }

    // Buscar uno solo por ID
    public Libro obtenerPorId(int id) {
        return libroDAO.findById(id);
    }
}