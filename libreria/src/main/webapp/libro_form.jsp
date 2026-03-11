<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>${libro != null ? 'Editar Libro' : 'Nuevo Libro'}</title>
    <style>
        .form-card { background: white; max-width: 500px; margin: 20px auto; padding: 30px; border-radius: 10px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); font-family: sans-serif; }
        label { display: block; margin-top: 15px; font-weight: bold; color: #34495e; }
        input { width: 100%; padding: 10px; margin: 5px 0 15px 0; border: 1px solid #ccc; border-radius: 5px; box-sizing: border-box; }
        .btn-save { background-color: #2980b9; color: white; border: none; padding: 12px 25px; cursor: pointer; border-radius: 5px; font-weight: bold; width: 100%; margin-top: 10px; }
        .btn-back { display: block; text-align: center; color: #7f8c8d; text-decoration: none; margin-top: 15px; font-size: 0.9rem; }
        .color-gestion { color: #2980b9; }
    </style>
</head>
<body>

<div class="form-card">
    <h2 class="color-gestion">
        ${libro != null ? 'Editar Libro' : 'Registrar Nuevo Libro'}
    </h2>
    
    <form action="libros" method="post">
        <%-- ID oculto: 0 para nuevo, ID real para editar --%>
        <input type="hidden" name="id" value="${libro != null ? libro.id : '0'}">
        
        <%-- Acción para el LibroServlet --%>
        <input type="hidden" name="accion" value="${libro != null ? 'actualizar' : 'insertar'}">

        <label>ISBN</label>
        <input type="text" name="isbn" value="${libro.isbn}" required>
        
        <label>Título</label>
        <input type="text" name="titulo" value="${libro.titulo}" required>
        
        <label>Autor</label>
        <input type="text" name="autor" value="${libro.autor}" required>

        <%-- Mantener disponibilidad por defecto si es nuevo --%>
        <input type="hidden" name="disponible" value="${libro != null ? libro.disponible : 'true'}">

        <button type="submit" class="btn-save">Guardar Libro</button>

        <a href="libros?accion=listar" class="btn-back">Cancelar y Volver</a>
    </form>
</div>

</body>
</html>