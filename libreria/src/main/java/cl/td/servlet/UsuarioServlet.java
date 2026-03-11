package cl.td.servlet;

import cl.td.dao.UsuarioDAO;
import cl.td.dto.Usuario;
import cl.td.service.UsuarioService;

import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {

    // 1. Conexión Real: Instanciamos el DAO que ya tienes funcionando con MariaDB
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private UsuarioService usuarioService = new UsuarioService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 2. Acción: Detectamos qué quiere hacer el usuario (por defecto es listar)
        String accion = request.getParameter("accion");
        if (accion == null)
            accion = "listar";

        if (accion.equals("listar")) {
            // TRAEMOS LOS DATOS REALES DE TU BASE DE DATOS
            List<Usuario> lista = usuarioDAO.findAll();
            request.setAttribute("listaUsuarios", lista);

            // INDICAMOS QUE QUEREMOS VER LA SECCIÓN "usuarios" EN EL DASHBOARD
            request.setAttribute("seccion", "usuarios");

        } else if (accion.equals("buscar")) {
            String termino = request.getParameter("termino");
            List<Usuario> listaFiltrada = usuarioDAO.findByNombreOEmail(termino);
            request.setAttribute("listaUsuarios", listaFiltrada);
            request.setAttribute("seccion", "usuarios");

        } else if (accion.equals("nuevo")) {
            // Solo indicamos que el contenido dinámico sea el formulario
            request.setAttribute("seccion", "formulario_usuario");

        } else if (accion.equals("editar")) {
            // 1. Buscamos el usuario por su ID
            int id = Integer.parseInt(request.getParameter("id"));
            Usuario usuario = usuarioDAO.findByIdUsuario(id);

            if (usuario != null) {
                request.setAttribute("usuario", usuario);
                request.setAttribute("seccion", "formulario_usuario");
                List<Usuario> lista = usuarioDAO.findAll();
                request.setAttribute("listaUsuarios", lista);
            } else {
                response.sendRedirect("usuarios?accion=listar&error=UsuarioNoEncontrado");
                return;
            }
        } else if (accion.equals("eliminar")) {
            int id = Integer.parseInt(request.getParameter("id"));
            usuarioDAO.deleteLogico(id);
            response.sendRedirect("usuarios?accion=listar");
            return; // IMPORTANTE: Corta la ejecución aquí
        }
        // REDIRECCIÓN CRÍTICA:
        // response.sendRedirect("usuarios?accion=listar");
        request.getRequestDispatcher("gestor.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if (accion != null && (accion.equals("insertar") || accion.equals("actualizar"))) {
            // 1. CAPTURAMOS EL ID (Viene del <input type="hidden" name="id">)
            String idStr = request.getParameter("id");
            int id = (idStr != null && !idStr.isEmpty()) ? Integer.parseInt(idStr) : 0;
            // 2. CAPTURAMOS EL RESTO DE DATOS
            String email = request.getParameter("email");
            String nombre = request.getParameter("nombre");
            String password = request.getParameter("password");
            String rol = request.getParameter("rol");
            // 3. CREAMOS EL OBJETO
            Usuario usuario = new Usuario(id, email, nombre, password, rol, true);
            // 4. EL CEREBRO DECIDE SI ACTUALIZA O GUARDA
            boolean exito;
            if (id > 0) {
                exito = usuarioService.actualizarUsuario(usuario);
            } else {
                exito = usuarioService.guardarUsuario(usuario);
            }
            if (exito) {
                response.sendRedirect("usuarios?accion=listar");
            } else {
                // Si falla, devolvemos al form con un mensaje de error
                request.setAttribute("error", "No se pudo procesar la solicitud de crear usuario.");
                request.setAttribute("usuario", usuario); // Devolvemos el objeto para no borrar lo escrito
                request.setAttribute("seccion", "formulario_usuario");
                request.getRequestDispatcher("gestor.jsp").forward(request, response);
            }
        }
    }
}
