package cl.td.servlet;

import cl.td.dao.PrestamoDAO;
import cl.td.dto.Prestamo;
import cl.td.dto.Libro;
import cl.td.dao.LibroDAO;
import cl.td.dto.Usuario;
import cl.td.service.LibroService;
import cl.td.service.PrestamoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/prestamos")
public class PrestamoServlet extends HttpServlet {
    private PrestamoDAO prestamoDAO;
    private LibroService libroService;
    private PrestamoService prestamoService;
    private LibroDAO libroDAO;
    @SuppressWarnings("unused")
    private Usuario usuario;

    @Override
    public void init() throws ServletException {
        prestamoDAO = new PrestamoDAO();
        libroService = new LibroService();
        prestamoService = new PrestamoService();
        libroDAO = new LibroDAO();
        this.usuario = new Usuario();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("ENTRANDO AL SERVLET - ACCION: " + request.getParameter("accion"));

        // 1. Validación de sesión
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioSesion") == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String accion = request.getParameter("accion");
        if (accion == null)
            accion = "listar";

        try {
            switch (accion) {

                case "preparar_prestamo":
                    try {
                        String idParam = request.getParameter("id");
                        cl.td.dto.Usuario userLogueado = (cl.td.dto.Usuario) session.getAttribute("usuarioSesion");

                        if (idParam == null || userLogueado == null) {
                            response.sendRedirect("libros?accion=listar&error=missing_data");
                            return;
                        }
                        int idCapturado = Integer.parseInt(idParam);
                        Libro libroEncontrado = libroDAO.findById(idCapturado);

                        if (libroEncontrado != null) {
                            request.setAttribute("libro", libroEncontrado); // Objeto con el ISBN
                            request.setAttribute("usuarioId", userLogueado.getId()); // ID del bibliotecario
                            request.setAttribute("seccion", "formulario_prestamo");
                            request.setAttribute("view", "formulario_prestamo.jsp");
                            request.getRequestDispatcher("gestor.jsp").forward(request, response);
                        } else {
                            response.sendRedirect("libros?accion=listar&error=not_found");
                        }
                        return;

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        response.sendRedirect("libros?accion=listar&error=1");
                    }
                    break;

                case "nuevo":
                    // Caso opcional por si entras manual: muestra todos los libros
                    List<Libro> libros = libroService.listarLibros(null, null);
                    request.setAttribute("listaLibros", libros);
                    request.setAttribute("view", "formulario_prestamo.jsp");
                    request.getRequestDispatcher("gestor.jsp").forward(request, response);
                    break;

                case "listar":
                    List<Prestamo> lista = prestamoDAO.findAll(null, null);
                    request.setAttribute("listaPrestamos", lista);
                    request.setAttribute("seccion", "prestamos"); // Para que entre al <c:when> de tu gestor
                    request.setAttribute("view", "datos_prestamo.jsp");
                    request.getRequestDispatcher("gestor.jsp").forward(request, response);
                    break;

                case "devolver":
                    int idPrestamo = Integer.parseInt(request.getParameter("id"));
                    String isbn = request.getParameter("isbn");

                    if (prestamoService.finalizarPrestamo(idPrestamo, isbn)) {
                        response.sendRedirect("prestamos?accion=listar&mensaje=devuelto");
                    } else {
                        response.sendRedirect("prestamos?accion=listar&error=devolucion");
                    }
                    break;

                default:
                    response.sendRedirect("libros?accion=listar");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("libros?accion=listar&error=1");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");

        if ("insertar".equals(accion)) {
            try {
                // 1. Captura de datos del formulario (deben coincidir con el 'name' en el JSP)
                String idLibro = request.getParameter("idLibro");
                String clienteNombre = request.getParameter("clienteNombre");
                String telefono = request.getParameter("telefono"); // Capturamos teléfono
                String email = request.getParameter("email"); // Capturamos email
                String fechaDevolucionStr = request.getParameter("fechaDevolucion");
                String observaciones = request.getParameter("observaciones");

                // 2. Datos automáticos
                int idUsuarioDummy = 1; // El bibliotecario
                boolean activo = true;

                // Convertimos las fechas de String a java.sql.Date
                java.sql.Date fechaSalida = new java.sql.Date(System.currentTimeMillis());
                java.sql.Date fechaDevolucion = java.sql.Date.valueOf(fechaDevolucionStr);

                // Crear y poblar el objeto Prestamo ---
                Prestamo nuevoPrestamo = new Prestamo();
                nuevoPrestamo.setUserId(idUsuarioDummy);
                nuevoPrestamo.setLibroId(idLibro);
                nuevoPrestamo.setFechaPrestamo(fechaSalida);
                nuevoPrestamo.setFechaDevolucion(fechaDevolucion);
                nuevoPrestamo.setActivo(activo);
                nuevoPrestamo.setClienteNombre(clienteNombre);
                nuevoPrestamo.setClienteTelefono(telefono);
                nuevoPrestamo.setClienteEmail(email);
                nuevoPrestamo.setObservaciones(observaciones);

                // 3. Guardar en BD (Usando los 9 parámetros del nuevo PrestamoDAO)
                // El orden DEBE ser: libroId, userId, fechaP, fechaD, activo, nombre, tel,
                // email, obs
                boolean exito = prestamoService.guardarPrestamo(nuevoPrestamo);

                if (exito) {
                    // Actualizamos disponibilidad en la tabla 'libro'
                    response.sendRedirect("libros?accion=listar&mensaje=prestamo_ok");
                } else {
                    response.sendRedirect("libros?accion=listar&error=libro_ya_prestado");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("libros?accion=listar&error=data");
            }
        }
    }
}
