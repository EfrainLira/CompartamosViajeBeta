package com.ganlen.compartamosviaje.lugares;

public class LugaresUpload {
    public String nombre;
    public String url;

    public String getName() {
        return nombre;
    }

    public String getUrl() {
        return url;
    }

    public LugaresUpload(String nombre, String url) {
        this.nombre = nombre;
        this.url = url;
    }

    public LugaresUpload(){}
}