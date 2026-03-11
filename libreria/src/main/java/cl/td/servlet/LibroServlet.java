package cl.td.servlet;

import cl.td.dto.Libro;
import cl.td.service.LibroService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/libros")
public class LibroServlet extends HttpServlet {
    private LibroService libroService = new LibroService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null)
            accion = "listar";

        String filtro = request.getParameter("filtro");
        String busqueda = request.getParameter("busqueda");

        switch (accion) {
            case "listar":
                List<Libro> libros = libroService.listarLibros(filtro, busqueda);
                request.setAttribute("listaLibros", libros);
                request.getRequestDispatcher("gestor.jsp").forward(request, response);
                break;

            case "nuevo":
                // Simplemente mandamos al form vacío
                request.getRequestDispatcher("libro_form.jsp").forward(request, response);
                break;

            case "editar":
                int idEditar = Integer.parseInt(request.getParameter("id"));
                Libro libroExistente = libroService.obtenerPorId(idEditar);
                request.setAttribute("libro", libroExistente);
                request.getRequestDispatcher("libro_form.jsp").forward(request, response);
                break;
            case "eliminar":
                int idEliminar = Integer.parseInt(request.getParameter("id"));
                libroService.eliminarLibro(idEliminar);
                response.sendRedirect("libros?accion=listar");
                break;

            default:
                response.sendRedirect("libros?accion=listar");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        int id = (idStr != null && !idStr.isEmpty()) ? Integer.parseInt(idStr) : 0;

        String titulo = request.getParameter("titulo");
        String autor = request.getParameter("autor");
        String isbn = request.getParameter("isbn");

        // Si es edición, mantenemos la disponibilidad que ya tenía el objeto
        // Si es nuevo, por defecto es true
        boolean disponible = true;
        if (id > 0) {
            Libro actual = libroService.obtenerPorId(id);
            disponible = actual.isDisponible();
        }

        Libro libro = new Libro(id, titulo, autor, isbn, disponible, true);

        boolean exito;
        if (id > 0) {
            exito = libroService.actualizarLibro(libro);
        } else {
            exito = libroService.guardarLibro(libro);
        }

        if (exito) {
            response.sendRedirect("libros?accion=listar");
        } else {
            // Si falla, devolvemos al form con un mensaje de error
            request.setAttribute("error", "No se pudo procesar la solicitud del libro.");
            request.setAttribute("libro", libro); // Devolvemos el objeto para no borrar lo escrito
            request.getRequestDispatcher("libro_form.jsp").forward(request, response);
        }
    }
}