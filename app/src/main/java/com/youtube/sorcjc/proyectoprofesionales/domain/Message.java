package com.youtube.sorcjc.proyectoprofesionales.domain;

public class Message {

    private String message;
    private String time;

    public Message(String message, String time) {
        this.message = message;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }
}
