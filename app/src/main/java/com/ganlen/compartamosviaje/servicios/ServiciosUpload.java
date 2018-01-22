package com.ganlen.compartamosviaje.servicios;

public class ServiciosUpload {
    public String nombre;
    public String url;

    public String getName() {
        return nombre;
    }

    public String getUrl() {
        return url;
    }

    public ServiciosUpload(String nombre, String url) {
        this.nombre = nombre;
        this.url = url;
    }

    public ServiciosUpload(){}
}