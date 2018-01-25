package com.ganlen.compartamosviaje;

public class Upload_Promocion {
    public String nombre;
    public String url;

    public String getName() {
        return nombre;
    }

    public String getUrl() {
        return url;
    }

    public Upload_Promocion(String nombre, String url) {
        this.nombre = nombre;
        this.url = url;
    }

    public Upload_Promocion(){}
}