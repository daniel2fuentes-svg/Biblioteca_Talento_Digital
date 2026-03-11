package cl.td.servlet;

import cl.td.dto.Usuario;
import cl.td.dao.UsuarioDAO;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private UsuarioDAO usuarioDAO = new UsuarioDAO(); // Instanciar el DAO

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // 1. Validar contra la BD
        Usuario usuario = usuarioDAO.validarLogin(email, password);

        if (usuario != null) {
            // 2. ÉXITO: Crear sesión y guardar al usuario
            HttpSession session = request.getSession();
            session.setAttribute("usuarioSesion", usuario); // Este nombre debe coincidir con el del PrestamoServlet

            // 3. Redirigir al catálogo
            response.sendRedirect("libros?accion=listar");
            // para probar el loginresponse.sendRedirect("index.jsp")<---- cambiado
            // momentaneamente
            // response.sendRedirect("index.jsp");
        } else {
            // 4. FALLO: Volver al login con error
            request.setAttribute("error", "Credenciales incorrectas");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
}