package com.example.trabajofct.Modules;


import java.util.ArrayList;

public class Usuarios {
    private String idUsuario;
    private String urlImagen;
    private String nombre;
    private String apellidos;
    private String email;
    private int edad;
    private String contraseña;
    private String tipoUsuario;
    private String grupo;
    private ArrayList<String> asignaturas;

    public Usuarios() {
    }

    public Usuarios(String idUsuario, String urlImagen, String nombre, String apellidos, String email, int edad, String contraseña, String tipoUsuario, String grupo, ArrayList<String> asignaturas) {
        this.idUsuario = idUsuario;
        this.urlImagen = urlImagen;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.edad = edad;
        this.contraseña = contraseña;
        this.tipoUsuario = tipoUsuario;
        this.grupo = grupo;
        this.asignaturas = asignaturas;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public ArrayList<String> getAsignaturas() {
        return asignaturas;
    }

    public void setAsignaturas(ArrayList<String> asignaturas) {
        this.asignaturas = asignaturas;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipoUsuario() {

        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
}