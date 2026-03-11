package cl.td.dto;

public class Usuario {
    private int id;
    private String email;
    private String nombre;
    private String password;
    private String rol;
    private Boolean activo;

    // Constructor vacío
    public Usuario() {
    }

    // Constructor con datos
    public Usuario(int id, String email, String nombre, String password, String rol, Boolean activo) {
        this.id = id;
        this.email = email;
        this.nombre = nombre;
        this.password = password;
        this.rol = rol;
        this.activo = activo;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

}