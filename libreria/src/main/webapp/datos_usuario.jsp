<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<div class="container-fluid">
    <div class="card shadow mb-4">
        <div class="card-body">
            <div class="table-responsive">                
                <table class="table table-bordered" width="100%" cellspacing="0">
                    <thead class="thead-light">
                        <tr>
                            <th>ID</th>
                            <th>Nombre</th>
                            <th>Email</th>
                            <th>Rol</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${listaUsuarios}" var="u">
                            <tr>
                                <td>${u.id}</td>
                                <td><strong>${u.nombre}</strong></td>
                                <td>${u.email}</td>
                                <td>
                                    <span class="badge bg-info text-dark">${u.rol}</span>
                                </td>
                                <td>
                                    <span class="badge ${u.activo ? 'bg-success text-white' : 'bg-danger text-white'}">
                                        ${u.activo ? 'Activo' : 'Inactivo'}
                                    </span>
                                </td>
                                <td>
                                    <!-- Botón Editar -->
                                    <a href="usuarios?accion=editar&id=${u.id}" 
                                       class="btn btn-edit">
                                       <i class="fas fa-edit"></i> Editar
                                    </a>
                                    
                                    <!-- Botón Eliminar (Lógico) -->
                                    <c:if test="${u.activo}">
                                        <a href="usuarios?accion=eliminar&id=${u.id}" 
                                           class="btn btn-delete"
                                           onclick="return confirm('¿Está seguro de desactivar este usuario?')">
                                           Desactivar
                                        </a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty listaUsuarios}">
                            <tr>
                                <td colspan="6" class="text-center">No se encontraron usuarios registrados.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>