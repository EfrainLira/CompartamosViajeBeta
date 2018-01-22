package com.ganlen.compartamosviaje.productos;

public class ProductosUpload {
    public String nombre;
    public String url;

    public String getName() {
        return nombre;
    }

    public String getUrl() {
        return url;
    }

    public ProductosUpload(String nombre, String url) {
        this.nombre = nombre;
        this.url = url;
    }

    public ProductosUpload(){}
}