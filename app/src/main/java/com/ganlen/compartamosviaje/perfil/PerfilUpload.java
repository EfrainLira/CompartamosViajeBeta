package com.ganlen.compartamosviaje.perfil;

public class PerfilUpload {
    public String nombre;
    public String url;

    public String getName() {
        return nombre;
    }

    public String getUrl() {
        return url;
    }

    public PerfilUpload(String nombre, String url) {
        this.nombre = nombre;
        this.url = url;
    }

    public PerfilUpload(){}
}