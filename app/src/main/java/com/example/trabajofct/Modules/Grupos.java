package com.example.trabajofct.Modules;

public class Grupos {
    private String idGrupo;
    private String nombre;
    private int numero;
    private String idUsuario;
    public Grupos(){

    }

    public Grupos(String idGrupo, String nombre, int numero, String idUsuario) {
        this.idGrupo = idGrupo;
        this.nombre = nombre;
        this.numero = numero;
        this.idUsuario= idUsuario;
    }

    public String getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(String idGrupo) {
        this.idGrupo = idGrupo;
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

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

}
