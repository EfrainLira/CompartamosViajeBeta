package com.ganlen.compartamosviaje.Objects;

public class Posts {
    private String message;
    private String full_picture;

    public Posts(String message, String full_picture) {
        this.message = message;
        this.full_picture = full_picture;
    }

    public String getMessage() {
        return message;
    }

    public String getFull_picture() {
        return full_picture;
    }
}