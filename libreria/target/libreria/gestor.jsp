<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ taglib prefix="c" uri="jakarta.tags.core" %> 
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %> 
<!DOCTYPE html> 
<html lang="es"> 
<head> 
    <meta charset="UTF-8"> 
    <title>Dashboard Biblioteca Talento Digital</title> 
    <style> 
        :root { 
            --primary: #2980b9; 
            --dark: #34495e; 
            --danger: #e74c3c; 
            --success: #2ecc71; 
            --bg: #f8f9fa; 
        } 
        body { 
            font-family: 'Segoe UI', sans-serif; 
            background-color: var(--bg); 
            margin: 0; 
            display: flex; /* Para el panel lateral */ 
            min-height: 100vh; 
        } 

        /* PANEL LATERAL */ 
        .sidebar { 
            width: 250px; 
            background-color: var(--dark); 
            color: white; 
            padding: 20px; 
            display: flex; 
            flex-direction: column; 
        } 
        .sidebar h1 { font-size: 1.2rem; margin-bottom: 30px; color: #3498db; } 
        .sidebar nav a { 
            color: #bdc3c7; 
            text-decoration: none; 
            padding: 12px; 
            display: block; 
            border-radius: 5px; 
            margin-bottom: 5px; 
        } 
        .sidebar nav a:hover { background: #2c3e50; color: white; } 
        .sidebar nav a.active { background: var(--primary); color: white; } 
        
        /* CONTENIDO PRINCIPAL */ 
        .main-content { 
            flex-grow: 1; 
            padding: 30px; 
            overflow-y: auto; 
        }

        /* INDICADORES SUPERIORES */ 
        .dashboard-grid { 
            display: grid; 
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); 
            gap: 20px; 
            margin-bottom: 30px; 
        } 
        .stat-card { 
            background: white; 
            padding: 20px; 
            border-radius: 10px; 
            box-shadow: 0 4px 6px rgba(0,0,0,0.05); 
            border-left: 5px solid var(--primary); 
        } 
        .stat-card h3 { margin: 0; font-size: 0.8rem; text-transform: uppercase; color: #7f8c8d; } 
        .stat-card p { margin: 10px 0 0; font-size: 1.8rem; font-weight: bold; color: var(--dark); } 

        /* CABECERA DE SECCIÓN */ 
        header { 
            display: flex; 
            justify-content: space-between; 
            align-items: center; 
            margin-bottom: 20px; 
        }

        /* BOTONES CRUD */ 
        .btn { 
            padding: 8px 15px; 
            border-radius: 5px; 
            text-decoration: none; 
            font-size: 0.9rem; 
            cursor: pointer; 
            border: none; 
            transition: 0.3s; 
            margin-right: 3px; 
        } 
        .btn-add { background: var(--primary); color: white; } 
        .btn-edit { background: #f1c40f; color: white; margin-right: 5px; } 
        .btn-delete { background: var(--danger); color: white; } 
        
        
        /* TABLA REFORMULADA */ 
        .table-container { 
            background: white; 
            padding: 20px; 
            border-radius: 12px; 
            box-shadow: 0 4px 15px rgba(0,0,0,0.05); 
        } 
        table { width: 100%; border-collapse: collapse; } 
        th { background-color: var(--dark); color: white; padding: 15px; text-align: left; } 
        td { padding: 12px 15px; border-bottom: 1px solid #eee; } 
        tr:hover { background-color: #f1f2f6; } 

        .badge { 
            padding: 4px 8px; 
            border-radius: 20px; 
            font-size: 0.75rem; 
            font-weight: bold; 
        } 
        .bg-success { background: var(--success); color: white; } 
        .bg-danger { background: var(--danger); color: white; } 
        .user-info { font-size: 0.85rem; color: #7f8c8d; text-align: right; margin-bottom: 10px; } 
    </style> 
</head> 
<body> 
    <!-- SIDEBAR: Navegación de usuario --> 
    <aside class="sidebar"> 
        <h1>Biblioteca Talento Digital</h1> 
        <nav> 
            <a href="libros?accion=listar" class="${empty seccion ? 'active' : ''}">Gestión Libros</a> 
            
            <!-- Solo ADMIN y USUARIO ven Préstamos --> 
            <c:if test="${usuarioSesion.rol != 'CONSULTOR'}"> 
                <a href="prestamos?accion=listar" class="${seccion == 'prestamos' ? 'active' : ''}">Préstamos</a> 
            </c:if> 
            
            <!-- SOLO el ADMIN ve la pestaña Usuarios --> 
            <c:if test="${usuarioSesion.rol == 'ADMIN'}"> 
                <a href="usuarios?accion=listar" class="${seccion == 'usuarios' ? 'active' : ''}">Usuarios</a> 
            </c:if> 
            
            <div style="margin-top: auto;"> 
                <a href="logout" style="color: var(--danger); text-decoration: none;">Cerrar Sesión</a> 
            </div> 
        </nav> 
    </aside> 

    <!-- CONTENIDO: Lo que ve el usuario al centro --> 
    <main class="main-content"> 
        <div class="user-info"> 
            Conectado como: <strong><c:out value="${usuarioSesion.nombre}" /></strong> 
        </div> 
        
        <!-- TARJETAS DE INDICADORES (Se mantienen fijas arriba) --> 
        <c:set var="disponibles" value="0" /> 
        <c:set var="prestados" value="0" /> 
        <c:forEach var="l" items="${listaLibros}"> 
            <c:choose>
                <c:when test="${l.disponible}"> 
                    <c:set var="disponibles" value="${disponibles + 1}" /> 
                </c:when> 
                <c:otherwise> 
                    <c:set var="prestados" value="${prestados + 1}" /> 
                </c:otherwise> 
            </c:choose> 
        </c:forEach> 

        <div class="dashboard-grid"> 
            <div class="stat-card"> 
                <h3>Total Libros</h3> 
                <p>${fn:length(listaLibros)}</p> 
            </div> 
            <div class="stat-card" style="border-left-color: var(--success);"> 
                <h3>Disponibles</h3> 
                <p>${disponibles}</p> 
            </div> 
            <div class="stat-card" style="border-left-color: var(--danger);"> 
                <h3>En Préstamo</h3> 
                <p>${prestados}</p> 
            </div> 
        </div> 

        <!-- CONTENEDOR DINÁMICO DE TABLAS -->
         <!--<h2>DEBUG: [${seccion}]</h2>-->
        <div class="table-container"> 
            
            <c:choose> 
                <%-- VISTA DE USUARIOS --%> 
                <c:when test="${seccion == 'usuarios'}"> 
                    <header style="display: flex; align-items: center; justify-content: space-between; gap: 15px;"> 
                        <h2 style="color: var(--dark); margin: 0; white-space: nowrap;">Gestión de Usuarios</h2> 
                        <c:if test="${usuarioSesion.rol == 'ADMIN' || usuarioSesion.rol == 'USUARIO'}"> 
                            <a href="usuarios?accion=nuevo" class="btn btn-add">+ Agregar Usuario</a> 
                        </c:if> 
                    </header> 
                    <jsp:include page="datos_usuario.jsp" />
                </c:when> 

                <%-- SECCIÓN: FORMULARIO DE USUARIO (NUEVO/EDITAR) --%>
                <c:when test="${seccion == 'formulario_usuario'}">
                    <jsp:include page="formulario_usuario.jsp" />
                </c:when>

                <%-- PRIORIDAD 3: VISTA DE PRÉSTAMOS (HISTORIAL) --%> 
                <c:when test="${seccion == 'prestamos'}"> 
                    <header style="display: flex; align-items: center; justify-content: space-between; gap: 15px;"> 
                        <h2 style="color: var(--dark); margin: 0; white-space: nowrap;">Registro de Préstamos</h2> 
                        
                        <c:if test="${usuarioSesion.rol == 'ADMIN' || usuarioSesion.rol == 'USUARIO'}"> 
                            <a href="prestamos?accion=listar" class="btn btn-add">Actualizar Lista</a> 
                        </c:if> 
                    </header> 
                    <jsp:include page="datos_prestamo.jsp" /> 
                </c:when> 
                <%-- SECCIÓN: FORMULARIO DE NUEVO PRÉSTAMO --%> 
                <c:when test="${seccion == 'formulario_prestamo'}">
                    <header style="display: flex; align-items: center; justify-content: space-between; gap: 15px; margin-bottom: 20px;"> 
                        <div style="display: flex; align-items: center; gap: 15px;">
                            <h2 style="color: var(--dark); margin: 0; white-space: nowrap;">Registrando Préstamo</h2>
                            <span class="badge bg-success" style="padding: 5px 10px; border-radius: 15px; font-size: 12px; color: white;">Ingrese información del Lector</span>
                        </div>
                        <div style="display: flex; gap: 10px;">
                            <%-- Botón para cancelar y volver a la lista de libros --%>
                            <a href="libros?accion=listar" class="btn" style="background: #95a5a6; color: white; text-decoration: none; padding: 8px 15px; border-radius: 5px; font-size: 14px; font-weight: bold;">
                                ← Cancelar y Volver
                            </a>
                         </div>
                    </header>              
                    <%-- INCLUSIÓN DEL FORMULARIO --%>
                    <div style="background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.05);">
                        <jsp:include page="formulario_prestamo.jsp">
                            <jsp:param name="libroTitulo" value="${libro.titulo}" />
                            <jsp:param name="libroIsbn" value="${libro.isbn}" />
                        </jsp:include> 
                    </div>
                </c:when>
        
                

                <%-- PRIORIDAD 5: VISTA POR DEFECTO (GESTIÓN DE LIBROS) --%> 
                <c:otherwise> 
                    
                    <header>
                        <h2 style="margin: 0; white-space: nowrap;">Gestión de Libros</h2> 
                        <div style="flex-grow: 1; display: flex; align-items: center; max-width: 600px; margin: 0 20px;"> 
                            <input type="text"  placeholder="Buscar por título o autor..." 
                                style="width: 100%; padding: 8px 15px; border-radius: 20px 0 0 20px; border: 1px solid #ccc; outline: none;"> 
                            <button type="button" style="padding: 8px 15px; border-radius: 0 20px 20px 0; border: 1px solid #ccc; background: white;">🔍</button>
                        </div>
                    <c:if test="${usuarioSesion.rol != 'CONSULTOR'}"> 
                        <a href="libros?accion=nuevo" class="btn btn-add">+ Agregar Libro</a> 
                    </c:if> 
                    </header>
                    
                    

                    <c:if test="${not empty error}"> 
                        <div style="color: var(--danger); background: #fadbd8; padding: 10px; border-radius: 5px; margin-bottom: 15px;"> 
                            ${error} 
                        </div> 
                    </c:if> 

                    <table> 
                        <thead> 
                            <tr> 
                                <th>Título / Autor</th> 
                                <th>ISBN</th> 
                                <th>Estado</th> 
                                <th>Acciones</th> 
                            </tr> 
                        </thead> 
                        <tbody> 
                            <c:forEach var="libro" items="${listaLibros}"> 
                                <tr> 
                                    <td><strong>${libro.titulo}</strong><br><small>${libro.autor}</small></td> 
                                    <td><code>${libro.isbn}</code></td> 
                                    <td> 
                                        <span class="badge ${libro.disponible ? 'bg-success' : 'bg-danger'}"> 
                                            ${libro.disponible ? 'Disponible' : 'Prestado'} 
                                        </span> 
                                    </td> 
                                    <td> 
                                        <c:if test="${usuarioSesion.rol != 'CONSULTOR'}"> 
                                            <a href="libros?accion=editar&id=${libro.id}" class="btn btn-edit">Editar</a> 
                                        </c:if> 
                                        <c:if test="${usuarioSesion.rol == 'ADMIN'}"> 
                                            <a href="libros?accion=eliminar&id=${libro.id}" class="btn btn-delete" 
                                            onclick="return confirm('¿Confirma eliminar?');">Eliminar</a> 
                                        </c:if> 
                                        <c:choose>
                                            <c:when test="${libro.disponible && usuarioSesion.rol != 'CONSULTOR'}">
                                                <a href="prestamos?accion=preparar_prestamo&id=${libro.id}" 
                                                class="btn" style="background: #27ae60; color:white; padding: 5px 10px; border-radius: 4px;">
                                                Registrar Préstamo
                                                </a>
                                            </c:when>
                                            <c:when test="${libro.disponible && usuarioSesion.rol == 'CONSULTOR'}">
                                                <button type="button" class="btn" style="background: #3498db; color:white;" 
                                                        onclick="alert('Solicite con ISBN: ${libro.isbn}')">Solicitar</button>
                                            </c:when>
                                        </c:choose>
                                    </td> 
                                </tr> 
                            </c:forEach> 
                        </tbody> 
                    </table> 
                </c:otherwise> 
            </c:choose>
        </div>
    </main> 
</body> 
</html>