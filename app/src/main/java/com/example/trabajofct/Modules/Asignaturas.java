package com.example.trabajofct.Modules;

public class Asignaturas {
    private String idAsignatura;
    private String nombre;
    private String curso;
    private String descripcion;
    private String urlImagenAsig;
    private String idUsuario;

    public  Asignaturas(){

    }
    public Asignaturas(String idAsignatura, String nombre, String curso, String descripcion, String urlImagenAsig, String idUsuario) {
        this.idAsignatura = idAsignatura;
        this.nombre = nombre;
        this.curso = curso;
        this.descripcion = descripcion;
        this.urlImagenAsig = urlImagenAsig;
        this.idUsuario = idUsuario;
    }

    public String getIdAsignatura() {
        return idAsignatura;
    }

    public void setIdAsignatura(String idAsignatura) {
        this.idAsignatura = idAsignatura;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrlImagenAsig() {
        return urlImagenAsig;
    }

    public void setUrlImagenAsig(String urlImagenAsig) {
        this.urlImagenAsig = urlImagenAsig;
    }
}
