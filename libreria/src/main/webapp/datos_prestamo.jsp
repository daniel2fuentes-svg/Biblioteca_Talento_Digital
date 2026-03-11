<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<div class="container-fluid">
    <div class="card shadow mb-4">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-bordered" width="100%" cellspacing="0">
                    <thead class="thead-light">
                        <tr>
                            <th>ISBN</th>
                            <th>Lector/Email</th>
                            <th class="text-nowrap">Fecha Salida</th>
                            <th class="text-nowrap">Entrega Prevista</th>
                            <th class="text-nowrap">Fono</th>
                            <th class="text-nowrap">Estado</th>
                            <th class="text-nowrap">Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${listaPrestamos}" var="p">
                            <tr class="align-middle">
                                <td>
                                    <strong>${p.libroId}</strong><br>
                                    <small class="text-muted" style="font-size: 0.75rem;">OBS: ${p.observaciones}</small>
                                </td>
                                <td  style="max-width: 200px;">
                                    <div class="text-truncate" title="${p.clienteEmail}">${p.clienteNombre}</div>
                                    <small class="text-muted d-block" style="font-size: 0.8rem;">${p.clienteEmail}</small>
                                </td>
                                <td class="text-nowrap small text-secondary">${p.fechaPrestamo}</td>
                                <td class="text-nowrap small text-secondary">${p.fechaDevolucion}</td>
                                <td class="text-nowrap small text-secondary">${p.clienteTelefono}</td>
                                <td class="text-nowrap text-center">
                                    <small class="badge ${p.activo ? 'bg-success' : 'bg-danger'}">
                                        ${p.activo ? 'En Curso' : 'Devuelto'}
                                    </small>
                                </td>
                                 <td class="text-nowrap text-center">
                                    <c:if test="${p.activo}">
                                        <a href="prestamos?accion=devolver&id=${p.id}&isbn=${p.libroId}" 
                                           class="btn btn-delete"
                                           onclick="return confirm('¿Confirmar devolución del libro?')">
                                           Devolver
                                        </a>
                                    </c:if>
                                    <c:if test="${!p.activo}">
                                        <span class="badge bg-success">Sin acciones</span>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty listaPrestamos}">
                            <tr>
                                <td colspan="6" class="text-center">No hay registros de préstamos activos.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>