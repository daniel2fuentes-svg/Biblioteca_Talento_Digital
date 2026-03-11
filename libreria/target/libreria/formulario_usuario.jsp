<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<style>
        .form-card { background: white; max-width: 500px; margin: 20px auto; padding: 30px; border-radius: 10px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); font-family: sans-serif; }
        label { display: block; margin-top: 15px; font-weight: bold; color: #34495e; }
        input { width: 100%; padding: 10px; margin: 5px 0 15px 0; border: 1px solid #ccc; border-radius: 5px; box-sizing: border-box; }
 
</style>

<div class="card shadow mb-4">
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>
    <div class="card-header py-3">
        <h2 style="color: var(--dark); margin: 0; white-space: nowrap;">${usuario.id > 0 ? 'Editar Usuario' : 'Registrar Nuevo Usuario'}</h2>
    </div>
    <div class="card-body">
        <form action="usuarios" method="POST">
            <input type="hidden" name="id" value="${usuario.id !=null ? usuario.id : 0}">
            <input type="hidden" name="accion" value="${usuario.id > 0 ? 'actualizar' : 'insertar'}">
            
            <div class="mb-3">
                <label class="form-label">Nombre Completo</label>
                <input type="text" name="nombre" value="${usuario.nombre}" class="form-control" required>
            </div>
            
            <div class="mb-3">
                <label class="form-label">Correo Electrónico</label>
                <input type="email" name="email" value="${usuario.email}" class="form-control" required>
            </div>
            
            <div class="mb-3">
                <label class="form-label">Contraseña</label>
                <input type="password" name="password" class="form-control" 
                    ${usuario.id > 0 ? '' : 'required'}>
                <c:if test="${usuario.id > 0}">
                    <small class="text-muted">Dejar en blanco para mantener la actual.</small>
                </c:if>
            </div>
            
            <div class="mb-3">
                <label class="form-label">Rol de Usuario</label>
                <select name="rol" class="form-select">
                    <option value="USUARIO" ${usuario.rol == 'USUARIO' ? 'selected' : ''}>Usuario Estándar</option>
                    <option value="ADMIN" ${usuario.rol == 'ADMIN' ? 'selected' : ''}>Administrador</option>
                    <option value="CONSULTOR" ${usuario.rol == 'CONSULTOR' ? 'selected' : ''}>Consultor</option>
                </select>
            </div>

            <div class="mt-4">
                <button type="submit" class="btn btn-primary">Guardar Usuario</button>
                <a href="usuarios?accion=listar" class="btn btn-secondary">Cancelar</a>
            </div>
        </form>
    </div>
</div>