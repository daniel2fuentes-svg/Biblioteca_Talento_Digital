# Biblioteca Talento Digital 📚

Este es un proyecto de gestión de librería desarrollado en Java utilizando Maven.

## 📁 Estructura del Proyecto

- `src/`: Contiene el código fuente de la aplicación.
- `src/main/webapp/`: Aquí se encuentra el `index.html` y la interfaz web.
- `pom.xml`: Archivo de configuración de dependencias de Maven.

## ✨ Funcionalidades

- Administración de catálogo de libros.
- Interfaz web dinámica (ubicada en webapp).
- [Añade aquí otra función, ej: Conexión a base de datos]

## 🚀 Cómo ejecutarlo

1. Asegúrate de tener instalado **Maven** y un **JDK** (Java Development Kit).
2. Abre el proyecto en tu IDE preferido (VS Code, IntelliJ o Eclipse).
3. Ejecuta el servidor (como Tomcat) para visualizar el `index.html`.

## 🔐 Estructura de Roles y Permisos (RBAC)

El sistema utiliza un control de acceso basado en roles para segmentar las funcionalidades según el perfil del usuario conectado:

| Rol           | Tipo de Perfil | Alcance y Permisos                                                               |
| :------------ | :------------- | :------------------------------------------------------------------------------- |
| **ADMIN**     | Superusuario   | Control total. Acceso completo (CRUD) a Libros, Préstamos y Gestión de Usuarios. |
| **USUARIO**   | Operativo      | Gestión de catálogo de Libros y Préstamos. Sin acceso a gestión de usuarios.     |
| **CONSULTOR** | Consulta       | Solo lectura. Visualiza catálogo y fechas de devolución. No puede editar.        |

> **Nota Técnica:** El sistema valida estos roles mediante la columna `rol` de la tabla `usuario` en la base de datos, asegurando que la interfaz oculte o muestre opciones según el nivel de autorización.
> Usa el código con precaución.

## 🗄️ Base de Datos

El sistema utiliza **MariaDB** como motor de base de datos relacional.

### ⚙️ Configuración del Entorno:

Para conectar el proyecto, asegúrate de configurar los siguientes parámetros en tu entorno o archivo de propiedades:

- **Host:** `localhost`
- **Puerto:** `3306`
- **Base de Datos:** `biblioteca_db` (o el nombre que hayas definido)
- **Driver:** `com.mariadb.jdbc.Driver`

> **Nota:** Puedes encontrar el script SQL de creación de tablas en la carpeta `/database` (si lo tienes ahí) para replicar la estructura de roles mencionada abajo.

## 🔧 Configuración de Conexión (Singleton)

Para que el sistema se conecte a **MariaDB**, asegúrate de configurar tus credenciales en el archivo `ConexionDS.java` (o el nombre de tu clase Singleton):

- **URL:** `jdbc:mariadb://localhost:3306/biblioteca_db`
- **User:** `tu_usuario_local`
- **Password:** `tu_password_local`
