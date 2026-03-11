package cl.td.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Obtenemos la sesión actual
        HttpSession session = request.getSession(false);

        if (session != null) {
            // 2. La destruimos (borra todos los atributos como usuarioSesion)
            session.invalidate();
        }

        // 3. Redirigimos al index.jsp (pantalla de login)
        response.sendRedirect("index.jsp");
    }
}
