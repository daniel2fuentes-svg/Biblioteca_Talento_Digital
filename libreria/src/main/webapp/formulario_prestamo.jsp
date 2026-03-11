<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<style>
    .fila { display: flex; gap: 20px; margin-bottom: 20px; align-items: flex-end; }
    .columna { flex: 1; display: flex; flex-direction: column; }
    .seccion-libro { background: #f8f9fa; padding: 15px; border-radius: 8px; border-left: 5px solid #2980b9; margin-bottom: 25px; }
    label { font-weight: bold; margin-bottom: 5px; color: #34495e; font-family: sans-serif; font-size: 14px; }
    input, textarea { padding: 10px; border: 1px solid #ccc; border-radius: 4px; font-size: 15px; }
    .btn-principal { background: #27ae60; color: white; border: none; padding: 12px 30px; border-radius: 5px; cursor: pointer; font-weight: bold; }
</style>

<div style="max-width: 900px; margin: 20px auto; font-family: sans-serif;">
    <form action="${pageContext.request.contextPath}/prestamos" method="post">
        <input type="hidden" name="accion" value="insertar">
        <input type="hidden" name="idLibro" value="${libro.isbn}">

        <!-- LÍNEA 1: DATOS DEL LIBRO -->
        <div class="seccion-libro">
            <label>LIBRO SELECCIONADO</label>
           <div style="font-size: 18px; font-weight: bold; color: #2c3e50;">
                <c:out value="${libro.titulo}" default="Título no disponible" /> 
                <span style="color: #7f8c8d; font-size: 14px; margin-left: 15px;">
                    ISBN: <c:out value="${libro.isbn}" default="N/A" />
                </span>
            </div>
        </div>

        <!-- LÍNEA 2: DATOS CLIENTE -->
        <div class="fila">
            <div class="columna" style="flex: 2;">
                <label>Nombre del Cliente</label>
                <input type="text" name="clienteNombre" placeholder="Ej: Juan Pérez" required>
            </div>
            <div class="columna">
                <label>Teléfono</label>
                <input type="text" name="telefono" placeholder="+56 9...">
            </div>
            <div class="columna">
                <label>Email</label>
                <input type="email" name="email" placeholder="nombre@correo.com">
            </div>
        </div>

        <!-- LÍNEA 3: FECHAS -->
        <div class="fila">
            <div class="columna">
                <label>Fecha de Salida</label>
                <input type="date" name="fechaSalida" value="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>" readonly style="background: #eee;">
            </div>
            <div class="columna">
                <label style="color: #27ae60;">Fecha Devolución Prevista</label>
                <input type="date" name="fechaDevolucion" required style="border-color: #27ae60;">
            </div>
        </div>

        <!-- LÍNEA 4: OBSERVACIONES -->
        <div class="columna">
            <label>Observaciones (Estado de entrega)</label>
            <textarea name="observaciones" rows="3" placeholder="Escriba aquí notas sobre el estado físico del libro..."></textarea>
        </div>

        <div style="margin-top: 30px; text-align: right;">
            <a href="libros?accion=listar" style="text-decoration: none; color: #7f8c8d; margin-right: 20px;">Cancelar</a>
            <button type="submit" class="btn-principal">Confirmar Préstamo</button>
        </div>
    </form>
</div>
