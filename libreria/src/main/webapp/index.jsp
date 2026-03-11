<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %> 

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Login - Biblioteca Talento Digital</title>
    <style>
        /* Estilos para cumplir con la Lección 2 */
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #e9ecef;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .main-container {
            background-color: #ffffff;
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.1);
            width: 100%;
            max-width: 400px;
            text-align: center;
        }
        h2 {
            color: #2c3e50;
            margin-bottom: 25px;
        }
        .error-box {
            background-color: #c0392b;
            color: white;
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 15px;
            font-size: 0.9rem;
        }
        form {
            display: flex;
            flex-direction: column;
            text-align: left;
        }
        label {
            margin-bottom: 8px;
            color: #34495e;
            font-weight: bold;
        }
        input {
            padding: 12px;
            margin-bottom: 20px;
            border: 1px solid #dcdde1;
            border-radius: 6px;
            font-size: 1rem;
        }
        button {
            background-color: #2980b9;
            color: white;
            padding: 12px;
            border: none;
            border-radius: 6px;
            font-size: 1rem;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        button:hover {
            background-color: #1f6391;
        }
        hr {
            border: 0;
            border-top: 1px solid #eee;
            margin: 25px 0;
        }
        footer p {
            font-size: 0.85rem;
            color: #7f8c8d;
        }
    </style>
</head>
<body>

    <div class="main-container">
        <h2>Bienvenido a la Biblioteca Talento Digital</h2>

        <c:if test="${not empty error}">
            <div class="error-box">
                <c:out value="${error}" />
            </div>
        </c:if>

        <form action="LoginServlet" method="post">
            <label>Usuario (Email):</label>
            <input type="text" name="email" placeholder="ejemplo@correo.com" required>
            
            <label>Contraseña:</label>
            <input type="password" name="password" placeholder="••••••••" required>
            
            <button type="submit">Ingresar</button>
        </form>

        <hr>
        
        <footer>
            <p>Sistema gestionado por: <c:out value="Aula Digital Sence" /></p>
        </footer>
    </div>

</body>
</html>