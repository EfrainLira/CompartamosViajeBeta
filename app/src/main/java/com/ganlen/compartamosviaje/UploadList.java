package com.ganlen.compartamosviaje;

public class UploadList {
    public String nombre;
    public String url;

    public String getName() {
        return nombre;
    }

    public String getUrl() {
        return url;
    }

    public UploadList(String nombre, String url) {
        this.nombre = nombre;
        this.url = url;
    }

    public UploadList(){}
}