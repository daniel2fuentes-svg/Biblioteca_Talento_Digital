package cl.td.service;

import cl.td.dto.Usuario;
import cl.td.dao.UsuarioDAO;
import java.util.List;

public class UsuarioService {

    private UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    // Lógica para listar con filtros
    public List<Usuario> listaUsuarios(String busqueda) {
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            return usuarioDAO.findByNombreOEmail(busqueda);
        }
        return usuarioDAO.findAll(); // Si no hay búsqueda, muestra todos
    }

    // Lógica para listar todos
    public List<Usuario> listaUsuarios() {
        return usuarioDAO.findAll();
    }

    // Logica para buscar Usuarios
    public List<Usuario> buscarUsuarios(String termino) {
        if (termino != null && !termino.trim().isEmpty()) {
            return usuarioDAO.findByNombreOEmail(termino);
        }
        return usuarioDAO.findAll();
    }

    // Lógica para guardar (aquí puedes validar antes de insertar)
    public boolean guardarUsuario(Usuario usuario) {
        if (usuarioDAO.findByEmail(usuario.getEmail()) != null) {
            System.out.println("Error: El Email ya existe.");
            return false;
        }
        return usuarioDAO.insert(usuario);
    }

    // Lógica para actualizar
    public boolean actualizarUsuario(Usuario usuario) {
        return usuarioDAO.update(usuario);
    }

    // Lógica para borrado lógico
    public boolean desactivarUsuario(int id) {
        return usuarioDAO.deleteLogico(id);
    }

    // Buscar uno solo por email
    public Usuario obtenerPorEmail(String email) {
        return usuarioDAO.findByEmail(email);
    }

    // Buscar uno solo por ID
    public Usuario findByIdUsuario(int id) {
        return usuarioDAO.findByIdUsuario(id);
    }
}